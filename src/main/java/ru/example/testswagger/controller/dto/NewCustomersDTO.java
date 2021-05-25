package ru.example.testswagger.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
@Getter
@Setter
@Schema(title = "New customer", description = "New customer's data")
public class NewCustomersDTO {

    @NotNull
    @Schema(description = "title")
    private String title;
}
