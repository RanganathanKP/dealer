package com.spares.dealer.controller;

import com.spares.dealer.CustomerServiceProxy;
import com.spares.dealer.entity.OrderDetailEntity;
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
    public ResponseEntity<ProductEntity> saveproduct(@RequestBody ProductEntity product, @RequestHeader String authorization) {
        ProductEntity productResponse = productService.saveProduct(product, authorization);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @PutMapping("/updateProduct/{productid}")
    @ResponseBody
    public ResponseEntity<ProductEntity> updateproduct(@RequestBody ProductEntity product, @PathVariable Integer productid, @RequestHeader String authorization) {
        ProductEntity productResponse = productService.updateProduct(product, productid, authorization);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/updatestatus/{orderdetailid}")
    @ResponseBody
    public ResponseEntity<OrderDetailEntity> updateOrderStatus(@PathVariable Integer orderdetailid, @RequestHeader String Authorization) {
        OrderDetailEntity response = productService.updateOrderStatus(orderdetailid, Authorization);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //Called in Customer Service/admin service(client)
    @GetMapping("/findAllProduct")
    @ResponseBody
    public ResponseEntity<List<ProductEntity>> findAllProduct() {
        List<ProductEntity> productResponse = productService.viewProduct();
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    //Called in Customer Service(client)
    @GetMapping("/findProduct/{productid}")
    @ResponseBody
    public ResponseEntity<ProductEntity> findProductByID(@PathVariable Integer productid) {
        ProductEntity productResponse = new ProductEntity();
        productResponse = productService.viewproductbyid(productid);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    //view order for dealer purpose
    @GetMapping("/getOrderOfDealer")
    @ResponseBody
    public ResponseEntity<List<OrderDetailEntity>> getOrderOfDealer(@RequestHeader String Authorization) {
        List<OrderDetailEntity> response = productService.allOrderOfDealer(Authorization);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    //view order for dealer purpose
    @GetMapping("/getOrderOfDealerPlaced")
    @ResponseBody
    public ResponseEntity<List<OrderDetailEntity>> getOrderOfDealerPlaced(@RequestHeader String Authorization) {
        List<OrderDetailEntity> response = productService.placedOrderOfDealer(Authorization);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
