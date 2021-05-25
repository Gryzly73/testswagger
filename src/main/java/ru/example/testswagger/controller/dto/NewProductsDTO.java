package ru.example.testswagger.controller.dto;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@Schema(title = "New product", description = "New product's data")
public class NewProductsDTO {

    @NotNull
    @Schema(description = "title")
    private String title;

    @Schema(description = "description")
    private String description;

    @Schema(description = "decimal")
    @Min(0)
    private Double decimal;

    @Hidden
    @Schema(description = "Id customer")
    private UUID customerId;
}
