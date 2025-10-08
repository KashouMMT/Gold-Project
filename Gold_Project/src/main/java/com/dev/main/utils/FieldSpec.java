package com.dev.main.utils;

public record FieldSpec(
    String name,       // e.g. "title", "price", "category"
    String label,      // e.g. "Title"
    String type,       // "text", "number", "textarea", "select", "checkbox", etc.
    boolean required,  // mirror @NotBlank/@NotNull for HTML hints
    Integer maxLength, // optional for text
    String step,       // e.g. "0.01" for decimals
    String optionsKey  // for selects: key to look up options in model (e.g. "categories")
) {}