package com.spares.dealer.controller;

import com.spares.dealer.entity.UserEntity;
import com.spares.dealer.entity.productEntity;
import com.spares.dealer.repository.ProductRepository;
import com.spares.dealer.repository.UserRepository;
import com.spares.dealer.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class DealerController {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private MyUserDetailsService myuserDetailsService;



	@PostMapping("dealer/saveProduct")
	@ResponseBody
	public productEntity saveproduct(@RequestBody productEntity product, @RequestHeader Integer userid ){
		UserEntity user=userRepo.getOne(userid);
		product.setProductDealername(user.getCompanyName());
		return productRepository.save(product);
	}

	@PutMapping("dealer/updateProduct/{productid}")
	@ResponseBody
	public productEntity updateproduct(@RequestBody productEntity product,@PathVariable int productid, @RequestHeader Integer userid ){
		UserEntity user=userRepo.getOne(userid);
		product.setProductId(productid);
		product.setProductDealername(user.getCompanyName());
		return productRepository.save(product);
	}

	@GetMapping("/dealer/findAllProduct")
	@ResponseBody
	public List<productEntity> findAllProduct(){
		return productRepository.findAll();
	}

	@GetMapping("/dealer/findNewProduct")
	@ResponseBody
	public List<productEntity> findNewProduct(){
		return productRepository.findlatestProduct();
	}

	@GetMapping("/dealer/findProduct/{productid}")
	@ResponseBody
	public productEntity findProductByID(@PathVariable int productid){
		return productRepository.findById(productid).get();
	}




}
