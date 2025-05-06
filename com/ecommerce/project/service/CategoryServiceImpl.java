package com.ecommerce.project.service;

//import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repositories.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

//	private List<Category> categories = new ArrayList<>();
//	private Long nextId = 1L;

	@Autowired
	private CategoryRepository categoryRepository;
	
	
	@Autowired
	private ModelMapper modelMapper;
	
	
	@Override
//	public List<Category> getAllCategories() 
	
	public CategoryResponse getAllCategories() {
		List<Category> categories = categoryRepository.findAll();
		if(categories.isEmpty())
			throw new APIException("No Category created till now");
//		return categoryRepository.findAll();
		
		List<CategoryDTO> categoryDTOS = categories.stream()
				.map(category -> modelMapper.map(category,CategoryDTO.class))
				.toList();
		CategoryResponse categoryResponse = new CategoryResponse();
		categoryResponse.setContent(categoryDTOS);
		return categoryResponse;
	}

	@Override
	public CategoryDTO createCategory(CategoryDTO categoryDTO) {
		
//		category.setCategoryId(nextId++);
//		categories.add(category);
		
		Category category = modelMapper.map(categoryDTO, Category.class);
		Category categoryFromDb = categoryRepository.findByCategoryName(category.getCategoryName());
		if(categoryFromDb!=null)
			throw new APIException("Category with the name" +category.getCategoryName()+" already exists!!!!");
//		categoryRepository.save(category);
		Category savedCategory = categoryRepository.save(category);
//		CategoryDTO savedCategoryDTO = modelMapper.map(savedCategory, CategoryDTO.class);
		return modelMapper.map(savedCategory, CategoryDTO.class);
	}

	@Override
	public CategoryDTO deleteCategory(Long categoryId) {

		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));

//		List<Category> categories = categoryRepository.findAll();
//
//		Category category = categories.stream().filter(c -> c.getCategoryId().equals(categoryId)).findFirst()
//				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rescource Not Found"));

//		if (category == null) 
//			return "Category not found ";

//		categories.remove(category);
		categoryRepository.delete(category);
//		return "Category with CategoryId: " + categoryId + " deleted Successfully";
		return modelMapper.map(category, CategoryDTO.class);
	}

	@Override
	public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {

//		List<Category> categories = categoryRepository.findAll();
		Optional<Category> savedCategoryOptional = categoryRepository.findById(categoryId);
//
//		Category savedCategory = savedCategoryOptional
//				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rescource Not Found"));
//		
		Category savedCategory = savedCategoryOptional
				.orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));

		Category category = modelMapper.map(categoryDTO,Category.class);
		category.setCategoryId(categoryId);
		savedCategory = categoryRepository.save(category);
		return modelMapper.map(savedCategory,CategoryDTO.class);
//		Optional<Category> optionalCategory = categories.stream()
//				.filter(c->c.getCategoryId().equals(categoryId))
//				.findFirst();

//		if(optionalCategory.isPresent()) {
//			Category existingCategory = optionalCategory.get();
////			existingCategory.setCategoryName(category.getCategoryName());
//			Category savedCategory = categoryRepository.save(existingCategory);
//			return savedCategory;
//		}else {
//			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Category not found");
//		}

	}

}
