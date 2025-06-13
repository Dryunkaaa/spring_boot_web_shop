package webshop.category.service;

import java.util.List;
import java.util.Optional;

import webshop.category.dto.CategoryDTO;
import webshop.category.request.SaveCategoryRequest;

public interface CategoryService {

    List<CategoryDTO> getAll();

    CategoryDTO create(SaveCategoryRequest saveCategoryRequest);

    Optional<CategoryDTO> findById(Long id);

    CategoryDTO update(Long id, SaveCategoryRequest saveCategoryRequest);

    void delete(Long id);
}
