package com.dryunka.webshop.category.request;

import com.dryunka.webshop.category.entity.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveCategoryRequest {

    @NotBlank(message = "Name must not be empty")
    @Size(min = 3, max = Category.MAX_NAME_LENGTH, message = "Name must be between {min} and {max} characters")
    private String name;

    @Positive(message = "Parent ID must be a positive number")
    private Long parentId;

}
