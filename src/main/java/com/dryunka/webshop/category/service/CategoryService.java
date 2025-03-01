package com.dryunka.webshop.category.service;

import java.util.List;
import java.util.Optional;

import com.dryunka.webshop.category.dto.CategoryDTO;
import com.dryunka.webshop.category.request.SaveCategoryRequest;

public interface CategoryService {

    List<CategoryDTO> getAll();

    CategoryDTO create(SaveCategoryRequest saveCategoryRequest);

    Optional<CategoryDTO> findById(Long id);

    CategoryDTO update(Long id, SaveCategoryRequest saveCategoryRequest);

    void delete(Long id);
}
