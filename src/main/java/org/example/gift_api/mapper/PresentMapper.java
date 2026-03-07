package org.example.gift_api.mapper;

import lombok.experimental.UtilityClass;
import org.example.gift_api.model.command.CreatePresentCommand;
import org.example.gift_api.model.command.UpdatePresentCommand;
import org.example.gift_api.model.dto.PresentDTO;
import org.example.gift_api.model.entity.Child;
import org.example.gift_api.model.entity.Present;

@UtilityClass
public class PresentMapper {

    public static Present mapFromCommand(CreatePresentCommand command) {
        return Present.builder()
                .name(command.getName())
                .price(command.getPrice())
                .build();
    }

    public static PresentDTO mapToDto(Present present) {
        return PresentDTO.builder()
                .id(present.getId())
                .name(present.getName())
                .price(present.getPrice())
                .build();
    }

    public static Present updateFromCommand(UpdatePresentCommand updateCommand, Long presentId, Child child) {
        return Present.builder()
                .id(presentId)
                .name(updateCommand.getName())
                .price(updateCommand.getPrice())
                .child(child)
                .build();
    }

    public static Present updateFromCommand(Present present, UpdatePresentCommand updateCommand) {
        present.setName(updateCommand.getName());
        present.setPrice(updateCommand.getPrice());
        return present;
    }

}
