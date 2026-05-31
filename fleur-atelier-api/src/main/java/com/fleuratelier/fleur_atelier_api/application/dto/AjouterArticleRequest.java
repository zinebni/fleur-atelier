// File: src/main/java/com/fleuratelier/fleur_atelier_api/application/dto/AjouterArticleRequest.java
package com.fleuratelier.fleur_atelier_api.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AjouterArticleRequest {

    @NotNull
    @Positive
    private Long articleId;

    @NotNull
    @Min(1)
    private Integer quantite;
}
