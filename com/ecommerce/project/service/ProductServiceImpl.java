package com.ecommerce.project.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.repositories.CategoryRepository;
import com.ecommerce.project.repositories.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private FileService fileService;
	
	@Value("${project.image}")
	private String path;
	
	@Override
	public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
		boolean isProductNotPresent = true;
		
		List<Product> products = category.getProducts();
//		for(int i=0;i<products.size();i++) {
		for(Product value:products) {
			
//			if(products.get(i).getProductName().equals(productDTO.getProductName()))
			if(value.getProductName().equals(productDTO.getProductName())){
				isProductNotPresent = false;
				break;
			}
		}
		

		if(isProductNotPresent) {
				Product product = modelMapper.map(productDTO, Product.class);
				product.setImage("default.png");
				product.setCategory(category);
				double specialPrice = product.getPrice() - (product.getDiscount() * 0.01) * product.getPrice();
				product.setSpecialPrice(specialPrice);
				Product savedProduct = productRepository.save(product);
				return modelMapper.map(savedProduct, ProductDTO.class);
		}else {
			throw new APIException("Product alreday exists");
		}
	}

	@Override
	public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
				? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();
		
		Pageable pageDetails = PageRequest.of(pageNumber, pageSize,sortByAndOrder);
		Page<Product> pageProducts = productRepository.findAll(pageDetails);
		
		
		
		List<Product> products = pageProducts.getContent();
		
		
		
		List<ProductDTO> productDTOS = products.stream().map(product -> modelMapper.map(product, ProductDTO.class))
				.collect(Collectors.toList());
		
//		if(products.isEmpty()) {
//			throw new APIException("No Products Exists");
//		}

		ProductResponse productResponse = new ProductResponse();
		productResponse.setContent(productDTOS);
		productResponse.setPageNumber(pageProducts.getNumber());
		productResponse.setPageSize(pageProducts.getSize());
		productResponse.setTotalElements(pageProducts.getTotalElements());
		productResponse.setTotalPages(pageProducts.getTotalPages());
		productResponse.setLastPage(pageProducts.isLast());
		return productResponse;
	}

	@Override
	public ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
		
		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
				? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();
		
		
		Pageable pageDetails = PageRequest.of(pageNumber, pageSize,sortByAndOrder);
		Page<Product> pageProducts = productRepository.findByCategoryOrderByPriceAsc(category,pageDetails);
		
		List<Product> products = pageProducts.getContent();
		

		if(products.size()== 0) {
			throw new APIException(category.getCategoryName()+" category does not  have any products");
		}
		
//		List<Product> products = productRepository.findByCategoryOrderByPriceAsc(category);
		List<ProductDTO> productDTOS = products.stream().map(product -> modelMapper.map(product, ProductDTO.class))
				.collect(Collectors.toList());
		



		ProductResponse productResponse = new ProductResponse();
		productResponse.setContent(productDTOS);
		productResponse.setPageNumber(pageProducts.getNumber());
		productResponse.setPageSize(pageProducts.getSize());
		productResponse.setTotalElements(pageProducts.getTotalElements());
		productResponse.setTotalPages(pageProducts.getTotalPages());
		productResponse.setLastPage(pageProducts.isLast());
		return productResponse;
	}

	@Override
	public ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy,
			String sortOrder) {
		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
				? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();
		
		
		Pageable pageDetails = PageRequest.of(pageNumber, pageSize,sortByAndOrder);
		
		
		Page<Product> pageProducts = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%',pageDetails);
		
		List<Product> products = pageProducts.getContent();
		
		List<ProductDTO> productDTOS = products.stream().map(product -> modelMapper.map(product, ProductDTO.class))
				.collect(Collectors.toList());
		
		if(products.size()== 0) {
			throw new APIException("Products not found with keyword"+keyword);
		}

		ProductResponse productResponse = new ProductResponse();
		productResponse.setContent(productDTOS);
		productResponse.setPageNumber(pageProducts.getNumber());
		productResponse.setPageSize(pageProducts.getSize());
		productResponse.setTotalElements(pageProducts.getTotalElements());
		productResponse.setTotalPages(pageProducts.getTotalPages());
		productResponse.setLastPage(pageProducts.isLast());
		return productResponse;
	}

	@Override
	public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
		
		//Get the  existing product from DB
		
		Product productFromDb = productRepository.findById(productId)
				.orElseThrow(()-> new ResourceNotFoundException("Product","productId",productId));
		
		Product product = modelMapper.map(productDTO,Product.class);
		//Update the product info with the one in request body
		productFromDb.setProductName(product.getProductName());
		productFromDb.setDescription(product.getDescription());
		productFromDb.setQuantity(product.getQuantity());
		productFromDb.setDiscount(product.getDiscount());
		productFromDb.setPrice(product.getPrice());
		productFromDb.setSpecialPrice(product.getSpecialPrice());
		
		//Save to database
		Product savedProduct = productRepository.save(productFromDb);
		
		return modelMapper.map(savedProduct,ProductDTO.class);
		
	}

	@Override
	public ProductDTO deleteProduct(Long productId) {
		Product product = productRepository.findById(productId)
				.orElseThrow(()-> new ResourceNotFoundException("Product","productId",productId));
		productRepository.delete(product);
		return modelMapper.map(product, ProductDTO.class);
	}

	@Override
	public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
		
		Product productFromDb = productRepository.findById(productId)
								.orElseThrow(()->new ResourceNotFoundException("Product","productId",productId));
		//Upload image to server
		//Get the file name of uploaded image
//		String path = "/images";
//		String path = "images/";
		
		String fileName = fileService.uploadImage(path,image);
		
		//Updating the new file name to product
		productFromDb.setImage(fileName);
		
		//Save updated product
		Product updatedProduct = productRepository.save(productFromDb);
		//return DTO after mapping product to DTO
		return modelMapper.map(updatedProduct, ProductDTO.class);
	}

//	private String uploadImage(String path, MultipartFile file) throws IOException {
//		//File names of current/original file
//		String originalFileName = file.getOriginalFilename();
//		
//		//Generate a unique file name
//		String randomId = UUID.randomUUID().toString();
//		//mat.jpg ---> 1234 --> 1234.jpg
//		String fileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));
//		String filePath = path + File.separator + fileName;
//		// path + "/" + filePath
//		
//		// Check if path exist and create
//		File folder = new File(path);
//		if(!folder.exists()) {
//			folder.mkdir();
//		}
//		
//		
//		//upload to server
//		Files.copy(file.getInputStream(), Paths.get(filePath));
//		//returning file name
//		return fileName;
//	}

//	public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId) {
//		ProductResponse productResponse = productService.searchByCategory(categoryId);
//		return new ResponseEntity<>(productResponse, HttpStatus.OK);
//	}

}
