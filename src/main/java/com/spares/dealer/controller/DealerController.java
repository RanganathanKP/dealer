package com.spares.dealer.controller;

import com.spares.dealer.entity.ProductEntity;
import com.spares.dealer.repository.ProductRepository;
import com.spares.dealer.repository.UserRepository;
import com.spares.dealer.service.MyUserDetailsService;
import com.spares.dealer.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/dealer")
@RestController
public class DealerController {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private MyUserDetailsService myuserDetailsService;

	@Autowired
	private ProductService productService;



	@PostMapping("/saveProduct")
	@ResponseBody
	public ResponseEntity<ProductEntity> saveproduct(@RequestBody ProductEntity product, @RequestHeader String authorization){
		ProductEntity productResponse= productService.saveProduct(product, authorization);
		return new ResponseEntity<>(productResponse, HttpStatus.OK);
	}

	@PutMapping("/updateProduct/{productid}")
	@ResponseBody
	public ResponseEntity<ProductEntity> updateproduct(@RequestBody ProductEntity product, @PathVariable Integer productid, @RequestHeader String authorization ){
		ProductEntity productResponse= productService.updateProduct( product, productid,  authorization);
		return new ResponseEntity<>(productResponse, HttpStatus.OK);
	}

	@GetMapping("/findAllProduct")
	@ResponseBody
	public ResponseEntity<List<ProductEntity>>findAllProduct(@RequestParam(value="userID",required = false) Integer userid){
		List<ProductEntity> productResponse= productService.viewProduct(userid);
		return new ResponseEntity<>(productResponse, HttpStatus.OK);
	}

	@GetMapping("/findNewProduct")
	@ResponseBody
	public ResponseEntity<List<ProductEntity>> findNewProduct(@RequestParam(value="userID",required = false) Integer userid){
		List<ProductEntity> productResponse= productService.viewLatestProduct(userid);
		return new ResponseEntity<>(productResponse, HttpStatus.OK);
	}

	@GetMapping("/findProduct/{productid}")
	@ResponseBody
	public ResponseEntity<ProductEntity> findProductByID(@PathVariable int productid){
		ProductEntity productResponse=  productRepository.findById(productid).get();
		return new ResponseEntity<>(productResponse, HttpStatus.OK);

	}

	// service of Status update must be added




}
