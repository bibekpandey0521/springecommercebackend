package com.ecommerce.project.service;

//import java.util.List;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;

public interface CategoryService {

//	List<Category> getAllCategories();
	
	CategoryResponse getAllCategories();
	
	CategoryDTO createCategory(CategoryDTO categoryDTO);
	
//	void createCategory(Category category);

//	String deleteCategory(Long categoryId);
	
	CategoryDTO deleteCategory(Long categoryId);

//	Category updateCategory(Category category, Long categoryId);

	CategoryDTO updateCategory(CategoryDTO categoryDTO,Long categoryId);
	
}
