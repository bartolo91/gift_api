package org.example.gift_api.service;

import lombok.RequiredArgsConstructor;
import org.example.gift_api.mapper.ChildMapper;
import org.example.gift_api.model.command.CreateChildCommand;
import org.example.gift_api.model.command.UpdateChildCommand;
import org.example.gift_api.model.command.UpdatePresentCommand;
import org.example.gift_api.model.dto.ChildDTO;
import org.example.gift_api.model.dto.PresentDTO;
import org.example.gift_api.model.entity.Child;
import org.example.gift_api.model.entity.ChildView;
import org.example.gift_api.repository.ChildRepository;
import org.example.gift_api.repository.ChildViewRepository;
import org.example.gift_api.specification.ChildSpecificationBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.example.gift_api.exceptions.GiftApiException.badRequest;
import static org.example.gift_api.exceptions.GiftApiException.notFound;
import static org.example.gift_api.mapper.ChildMapper.mapFromCommand;
import static org.example.gift_api.mapper.ChildMapper.mapToDto;
import static org.example.gift_api.mapper.ChildMapper.updateFromCommand;


@Service
@RequiredArgsConstructor
public class ChildService {

    private final ChildRepository childRepository;
    private final PresentService presentService;
    private final ChildViewRepository childViewRepository;

    public ChildDTO createChild(CreateChildCommand childCommand) {
        return mapToDto(childRepository.save(mapFromCommand(childCommand)));
    }

    public ChildDTO getChildById(Long id) {
        return childRepository.findByIdWithPresentCount(id)
                .map(ChildMapper::mapToDto)
                .orElseThrow(() -> notFound(Child.class, id));
    }

    public Page<ChildDTO> getAll(Pageable pageable) {
        return childRepository.findAllWithPresentCount(pageable);
    }

    public void deleteById(Long id) {
        if (childRepository.findById(id).isPresent()) {
            childRepository.deleteById(id);
        }
    }

    @Transactional
    public ChildDTO updateChild(Long id, UpdateChildCommand updateCommand) {
        childRepository.findById(id)
                .orElseThrow(() -> notFound(Child.class, id));
        try {
            Child updated = updateFromCommand(id, updateCommand);
            return mapToDto(childRepository.save(updated));
        } catch (ObjectOptimisticLockingFailureException e) {
            throw badRequest();
        }
    }

    public List<PresentDTO> findPresentsByChildId(Long childId) {
        return presentService.findAllByChildId(childId);
    }

    public void removePresent(Long childId, Long presentId) {
        presentService.deleteById(childId, presentId);
    }

    public PresentDTO getPresentByChildAndPresentId(Long childId, Long presentId) {
        return presentService.findPresentByPresentAndChildId(presentId, childId);
    }

    @Transactional
    public PresentDTO updatePresent(Long childId, Long presentId, UpdatePresentCommand updateCommand) {
        return presentService.update(childId, presentId, updateCommand);
    }

    public Page<ChildView> search(String search, Pageable pageable) {
        ChildSpecificationBuilder builder = new ChildSpecificationBuilder();

        if (search != null && !search.isEmpty()) {
            String[] parts = search.split(",");

            for (String part : parts) {
                if (part.contains("|")) {
                    String[] orParts = part.split("\\|");
                    for (int i = 0; i < orParts.length; i++) {
                        parseCondition(builder, orParts[i], i > 0);
                    }
                } else {
                    parseCondition(builder, part, false);
                }
            }
        }
        Specification<ChildView> spec = builder.build();

        return childViewRepository.findAll(spec, pageable);
    }

    private void parseCondition(ChildSpecificationBuilder builder, String input, boolean orPredicate) {
        Pattern pattern = Pattern.compile("(\\w+?)(:|>=|<=|>|<)(\\w+)");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            builder.with(
                    matcher.group(1),
                    matcher.group(2),
                    matcher.group(3),
                    orPredicate
            );
        }
    }
}
