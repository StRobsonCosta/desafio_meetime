package com.meetime.desafio.domain.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Contact {

    @NotNull
    private String firstname, lastname, email;

}
