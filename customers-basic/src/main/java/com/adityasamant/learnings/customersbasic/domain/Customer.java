package com.adityasamant.learnings.customersbasic.domain;

import jakarta.validation.constraints.NotEmpty;

public record Customer(Integer id, @NotEmpty String firstName, @NotEmpty String lastName, String country) {}
