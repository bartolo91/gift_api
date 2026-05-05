package org.example.gift_api.mapper;

import lombok.experimental.UtilityClass;
import org.example.gift_api.model.command.CreateChildCommand;
import org.example.gift_api.model.command.UpdateChildCommand;
import org.example.gift_api.model.dto.ChildDTO;
import org.example.gift_api.model.entity.Child;
import org.example.gift_api.model.entity.ChildView;

import java.time.LocalDate;
import java.time.Period;

@UtilityClass
public class ChildMapper {

    public static Child mapFromCommand(CreateChildCommand command) {
        return Child.builder()
                .firstName(command.getFirstName())
                .lastName(command.getLastName())
                .birthDate(command.getBirthDate())
                .email(command.getEmail())
                .build();
    }

    public static ChildDTO mapToDto(Child child) {
        return ChildDTO.builder()
                .id(child.getId())
                .firstName(child.getFirstName())
                .lastName(child.getLastName())
                .birthDate(child.getBirthDate())
                .presentsCount((long) child.getPresents().size())
                .version(child.getVersion())
                .email(child.getEmail())
                .build();
    }

//    public static Child updateFromCommand(UpdateChildCommand updateCommand, Long id) {
//        return Child.builder()
//                .id(id)
//                .firstName(updateCommand.getFirstName())
//                .lastName(updateCommand.getLastName())
//                .birthDate(updateCommand.getBirthDate())
//                .build();
//    }

    public static Child updateFromCommand(long id, UpdateChildCommand command) {
        return Child.builder()
                .id(id)
                .version(command.getVersion())
                .firstName(command.getFirstName())
                .lastName(command.getLastName())
                .birthDate(command.getBirthDate())
                .email(command.getEmail())
                .build();
    }

    public static ChildView mapToView(Child child) {
        return new ChildView(
                child.getId(),
                child.getFirstName(),
                child.getLastName(),
                child.getBirthDate(),
                calculateAge(child.getBirthDate()),
                child.getPresents() != null ? child.getPresents().size() : 0
        );
    }

    private static Integer calculateAge(LocalDate birthDate) {
        if (birthDate == null)
            return null;
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}
