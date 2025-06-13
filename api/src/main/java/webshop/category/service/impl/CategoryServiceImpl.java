package webshop.category.service.impl;

import java.util.List;
import java.util.Optional;

import webshop.category.dto.CategoryDTO;
import webshop.category.entity.Category;
import webshop.category.exception.CategoryNotFoundException;
import webshop.category.exception.DuplicateCategoryException;
import webshop.category.repository.CategoryRepository;
import webshop.category.request.SaveCategoryRequest;
import webshop.category.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryDTO> getAll() {
        return categoryRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDTO create(SaveCategoryRequest saveCategoryRequest) {
        checkIfNameUnique(saveCategoryRequest.getName());

        Category parentCategory = resolveParentCategory(saveCategoryRequest.getParentId());
        Category category = new Category(saveCategoryRequest.getName(), parentCategory);

        return mapToDTO(categoryRepository.save(category));
    }

    @Override
    public CategoryDTO update(Long id, SaveCategoryRequest saveCategoryRequest) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: %d".formatted(id)));

        if (!saveCategoryRequest.getName().equals(category.getName())) {
            checkIfNameUnique(saveCategoryRequest.getName());
            category.setName(saveCategoryRequest.getName());
        }

        Category parentCategory = resolveParentCategory(saveCategoryRequest.getParentId());

        if (parentCategory != null) {
            if (parentCategory.getId().equals(category.getId())) {
                throw new IllegalArgumentException("category cannot be its own parent");
            }

            if (parentCategory.getParentCategory() != null && parentCategory.getParentCategory().getId().equals(category.getId())) {
                throw new IllegalArgumentException("Circular dependency: a category cannot be a parent of its parent");
            }
        }

        category.setParentCategory(parentCategory);
        return mapToDTO(categoryRepository.save(category));
    }

    private void checkIfNameUnique(String name) {
        if (categoryRepository.existsByNameIgnoreCase(name)) {
            throw new DuplicateCategoryException("Category with name '%s' already exists"
                    .formatted(name));
        }
    }

    private Category resolveParentCategory(Long parentId) {
        if (parentId == null) {
            return null;
        }

        return categoryRepository.findById(parentId)
                .orElseThrow(() -> new CategoryNotFoundException("Parent category not found with ID: %d"
                        .formatted(parentId)));
    }

    @Override
    public Optional<CategoryDTO> findById(Long id) {
        return categoryRepository.findById(id).map(this::mapToDTO);
    }

    // TODO: use @Mapper instead ?
    private CategoryDTO mapToDTO(Category category) {
        Long parentId = Optional.ofNullable(category.getParentCategory())
                .map(Category::getId)
                .orElse(null);

        return new CategoryDTO(category.getId(), category.getName(), parentId);
    }

}
