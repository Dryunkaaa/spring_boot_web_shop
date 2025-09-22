package webshop.category.exception.handler;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import webshop.category.exception.CategoryNotFoundException;
import webshop.category.exception.CircularDependencyException;
import webshop.category.exception.DuplicateCategoryException;

@RestControllerAdvice
public class CategoryExceptionHandler {

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCategoryNotFoundException(CategoryNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(DuplicateCategoryException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateCategoryException(DuplicateCategoryException ex) {
        return ResponseEntity.badRequest()
                .body(Map.of("name", ex.getMessage()));
    }

    @ExceptionHandler(CircularDependencyException.class)
    public ResponseEntity<Map<String, String>> handleCircularDependencyException(CircularDependencyException ex) {
        return ResponseEntity.badRequest()
                .body(Map.of("parentId", ex.getMessage()));
    }

}
