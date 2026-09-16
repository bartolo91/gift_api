package org.example.gift_api.service.operations;

import org.example.gift_api.model.command.CreateChildCommand;
import org.example.gift_api.model.dto.BoyDTO;
import org.example.gift_api.model.entity.Boy;
import org.example.gift_api.model.entity.Child;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
public class BoyOperations implements ChildOperations {

    @Override
    public Boy create(CreateChildCommand command) {
        Map<String, String> params = command.getParams();
        return Boy.builder()
                .firstName(params.get("firstName"))
                .lastName(params.get("lastName"))
                .birthDate(LocalDate.parse(params.get("birthDate")))
                .pipeLength(Double.parseDouble(params.get("pipeLength")))
                .build();
    }

    @Override
    public BoyDTO mapToDTO(Child child) {
        Boy boy = (Boy) child;
        return BoyDTO.builder()
                .id(boy.getId())
                .firstName(boy.getFirstName())
                .lastName(boy.getLastName())
                .birthDate(boy.getBirthDate())
                .pipeLength(boy.getPipeLength())
                .build();
    }
}
