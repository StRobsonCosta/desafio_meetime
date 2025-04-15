package com.meetime.desafio.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ContactDto(@NotBlank String firstname,
                         @NotBlank String lastname,
                         @Email String email)
{}
