package com.adityasamant.learnings.common.domain.customers;

import jakarta.validation.constraints.NotEmpty;

public record CustomerDTO(Integer id, @NotEmpty String firstName, @NotEmpty String lastName, String country) {}
