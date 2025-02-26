package com.hafsa.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CategoryDTO {
    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    private List<ProductDTO> products = new ArrayList<>();
}