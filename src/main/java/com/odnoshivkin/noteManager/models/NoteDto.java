package com.odnoshivkin.noteManager.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NoteDto {
    private Long id;

    @NotEmpty(message = "Title must not be empty")
    @Size(max = 50)
    private String title;

    @NotEmpty
    private String content;
}
