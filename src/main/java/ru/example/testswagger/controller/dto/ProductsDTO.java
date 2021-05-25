package ru.example.testswagger.controller.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data transfer object with data about existing department.
 */
@Getter
@Setter
@JsonPropertyOrder({ "id" })
@Schema(title = "Product", description = "Product's data")
public class ProductsDTO extends NewProductsDTO {

    @NotNull
    @Schema(description = "Id")
    private UUID id;

    @Schema(description = "date and time of creation")
    private LocalDateTime created;

    @Schema(description = "date and time of modification")
    private LocalDateTime modified;

    @Schema(description = "deleted")
    private boolean deleted;
}
