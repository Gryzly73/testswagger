package ru.example.testswagger.controller.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@JsonPropertyOrder({ "id" })
@Schema(title = "Customer", description = "Customer's data")
public class CustomersDTO extends NewCustomersDTO {

    @NotNull
    @Schema(description = "Id")
    private UUID id;

    @Schema(description = "deleted")
    private boolean deleted;

    @Schema(description = "date and time of creation")
    private LocalDateTime created;

    @Schema(description = "date and time of modification")
    private LocalDateTime modified;
}
