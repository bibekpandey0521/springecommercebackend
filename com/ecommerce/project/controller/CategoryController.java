package com.ecommerce.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class CategoryController {

//	private List<Category> categories = new ArrayList<>();
	@Autowired
	private CategoryService categoryService;
	
	
	

//	public CategoryController(CategoryService categoryService) {
//		this.categoryService = categoryService;
//	}

//	@GetMapping("/echo")
//
//	public ResponseEntity<String> echoMessage(@RequestParam(name="message" , defaultValue="Hello World") String message)
//	public ResponseEntity<String> echoMessage(@RequestParam(name="message" , required= false) String message){
//		return new ResponseEntity<>("Echoed message: "+message,HttpStatus.OK);
//	}

	@GetMapping("/public/categories")
//	@RequestMapping(value="/public/categories",method=RequestMethod.GET)
	public ResponseEntity<CategoryResponse> getAllCategories(
			@RequestParam(name ="pageNumber",defaultValue=AppConstants.PAGE_NUMBER, required=false) Integer pageNumber,
			@RequestParam(name ="pageSize",defaultValue=AppConstants.PAGE_SIZE) Integer pageSize,
			@RequestParam(name="sortBy",defaultValue=AppConstants.SORT_CATEGORIES_BY, required=false) String sortBy,
			@RequestParam(name="sortOrder",defaultValue=AppConstants.SORT_DIR, required=false) String sortOrder		
			) {

//		return categoryService.getAllCategories();
		
//		List<Category> categories = categoryService.getAllCategories();
//		return new ResponseEntity<>(categories,HttpStatus.OK);
		CategoryResponse categoryResponse = categoryService.getAllCategories(pageNumber,pageSize,sortBy,sortOrder);
		return new ResponseEntity<>(categoryResponse,HttpStatus.OK);
	}

	@PostMapping("/public/categories")
//	@RequestMapping(value="/public/categories",method=RequestMethod.POST)
	public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
//		categoryService.createCategory(categoryDTO);
		
//		return "Category Added successfully";
		
//		return new ResponseEntity<>("Category added successfully",HttpStatus.CREATED);
		
		CategoryDTO savedCategoryDTO = categoryService.createCategory(categoryDTO);
		
		return new ResponseEntity<>(savedCategoryDTO,HttpStatus.CREATED);
		
	}

	@DeleteMapping("/admin/categories/{categoryId}")

	public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId) {
//		try {
			CategoryDTO deletedCategory = categoryService.deleteCategory(categoryId);
			return new ResponseEntity<>(deletedCategory, HttpStatus.OK);
//			return ResponseEntity<>(status);
//			return ResponseEntity.status(HttpStatus.OK).body(status);
//		} catch (ResponseStatusException e) {
//			return new ResponseEntity<>(e.getReason(), e.getStatusCode());
//		}
	}
	
	@PutMapping("/public/categories/{categoryId}")
	public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO,
				@PathVariable Long categoryId){
//		try {
			CategoryDTO savedCategoryDTO = categoryService.updateCategory(categoryDTO,categoryId);
//			return new ResponseEntity<>("Category with category id: "+ categoryId, HttpStatus.OK);
			return new ResponseEntity<>(savedCategoryDTO,HttpStatus.OK);
//		}catch(ResponseStatusException e) {
//			return new ResponseEntity<>(e.getReason(),e.getStatusCode());
//		}
		
	}
}
