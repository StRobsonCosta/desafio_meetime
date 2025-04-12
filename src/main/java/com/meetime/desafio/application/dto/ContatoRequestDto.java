package com.meetime.desafio.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ContatoRequestDto(@NotBlank String nome,
                                @NotBlank String sobreNome,
                                @Email String email)
{}
