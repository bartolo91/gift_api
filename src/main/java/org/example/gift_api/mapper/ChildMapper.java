package org.example.gift_api.mapper;

import lombok.experimental.UtilityClass;
import org.example.gift_api.model.command.CreateChildCommand;
import org.example.gift_api.model.command.UpdateChildCommand;
import org.example.gift_api.model.dto.ChildDTO;
import org.example.gift_api.model.entity.Child;

import java.util.HashSet;

@UtilityClass
public class ChildMapper {

    public static Child mapFromCommand(CreateChildCommand command) {
        return Child.builder()
                .firstName(command.getFirstName())
                .lastName(command.getLastName())
                .birthDate(command.getBirthDate())
                .build();
    }

    public static ChildDTO mapToDto(Child child) {
        return ChildDTO.builder()
                .id(child.getId())
                .firstName(child.getFirstName())
                .lastName(child.getLastName())
                .birthDate(child.getBirthDate())
                .presentCount(child.getPresents().stream().count())
                .build();
    }

    public static Child updateFromCommand(UpdateChildCommand updateCommand, Long id) {
        return Child.builder()
                .id(id)
                .firstName(updateCommand.getFirstName())
                .lastName(updateCommand.getLastName())
                .birthDate(updateCommand.getBirthDate())
                .build();
    }
}
