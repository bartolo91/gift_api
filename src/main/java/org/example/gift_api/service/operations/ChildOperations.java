package org.example.gift_api.service.operations;

import org.example.gift_api.model.command.CreateChildCommand;
import org.example.gift_api.model.dto.ChildDTO;
import org.example.gift_api.model.entity.Child;

public interface ChildOperations {

    String SUFFIX = "Operations";

    Child create(CreateChildCommand command);

    ChildDTO mapToDTO(Child child);
}
