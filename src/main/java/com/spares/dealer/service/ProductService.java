package com.spares.dealer.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.spares.dealer.CustomerServiceProxy;
import com.spares.dealer.entity.OrderDetailEntity;
import com.spares.dealer.entity.UserEntity;
import com.spares.dealer.entity.ProductEntity;
import com.spares.dealer.exception.ProductException;
import com.spares.dealer.exception.ProductNotFoundException;
import com.spares.dealer.exception.UserNotFoundException;
import com.spares.dealer.repository.ProductRepository;
import com.spares.dealer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerServiceProxy customerServiceProxy;

    public ProductEntity saveProduct(ProductEntity product, String authorization) {
        UserEntity user = getLoginedInUser(authorization);
        product.setProductUserID(user.getUserId());
        return productRepository.save(product);
    }

    public ProductEntity updateProduct(ProductEntity product, Integer productID, String authorization) {
        ProductEntity response = new ProductEntity();
        UserEntity user = getLoginedInUser(authorization);
        ProductEntity productDetails = productRepository.findById(productID).orElseThrow(() -> new ProductNotFoundException());
        if (productDetails.getProductUserID() == user.getUserId()) {
            product.setProductId(productID);
            product.setProductUserID(user.getUserId());
            response = productRepository.save(product);
        } else {
            throw new UserNotFoundException();
        }
        return response;
    }

    public OrderDetailEntity updateOrderStatus(Integer orderDetailID, String auth) {
        UserEntity user = getLoginedInUser(auth);
        OrderDetailEntity response = new OrderDetailEntity();
        if(user.getUserRole().equalsIgnoreCase("Dealer")) {
            ResponseEntity<OrderDetailEntity> orderDetail = customerServiceProxy.getorderdetail(orderDetailID);
            if (!(Optional.ofNullable(orderDetail.getBody()).isPresent() && Optional.ofNullable(orderDetail.getBody().getOrderdetailId()).isPresent())) {
                throw new ProductException("Enter valid order Detail ID.");
            } else {
                if (user.getUserId() == orderDetail.getBody().getDealerID()) {
                    orderDetail.getBody().setOrderDetailStatus("DISPATCHED");
                    response = customerServiceProxy.updateOrderStatus(auth, orderDetail.getBody()).getBody();
                } else {
                    throw new ProductException("Order does not belong to the User.");
                }
            }
            return response;
        }else{
            throw new ProductException("Only Dealer can update The Order");
        }
    }

    public List<ProductEntity> viewProduct() {
        List<ProductEntity> response;
        response = Optional.ofNullable(productRepository.findAll()).orElseThrow(() -> new ProductNotFoundException());
        return response;
    }


    public ProductEntity viewproductbyid(Integer productid) {
        ProductEntity productResponse = productRepository.findById(productid).orElseThrow(() -> new ProductNotFoundException());
        if (!Optional.ofNullable(productResponse).isPresent()) {
            throw new ProductNotFoundException();
        }
        return productResponse;
    }


    public List<OrderDetailEntity> allOrderOfDealer(String auth) {
        List<OrderDetailEntity> response = new ArrayList<>();
        UserEntity user = getLoginedInUser(auth);
        CollectionModel<OrderDetailEntity> orderDetail = customerServiceProxy.findByDealerID(user.getUserId());
        if(Optional.ofNullable(orderDetail).isPresent()){
            response = new ArrayList(orderDetail.getContent());
        if (CollectionUtils.isEmpty(response)) {
            throw new ProductException("No Orders Placed");
        } else {
            return response;
        }
        }else{
            throw new ProductException("No Orders Placed");
        }
    }

    public List<OrderDetailEntity> placedOrderOfDealer(String auth) {
        List<OrderDetailEntity> response = new ArrayList<>();
        UserEntity user = getLoginedInUser(auth);
        ObjectMapper mapper = new ObjectMapper();
        CollectionModel<OrderDetailEntity> orderDetail = customerServiceProxy.findByOrderDetailStatusAndDealerID("PLACED", user.getUserId());
        if(Optional.ofNullable(orderDetail).isPresent()) {
            response = new ArrayList(orderDetail.getContent());
            if (CollectionUtils.isEmpty(response)) {
                throw new ProductException("No Orders Placed");
            } else {
                return response;
            }
        }else{
            throw new ProductException("No Orders Placed");
        }
    }


    private UserEntity getLoginedInUser(@RequestHeader String authorization) {
        String encodedPass = authorization.substring("Basic".length()).trim();
        byte[] actualByte = Base64.getDecoder().decode(encodedPass);
        String usenamePassword = new String(actualByte);
        String username = usenamePassword.substring(0, usenamePassword.indexOf(":"));
        String password = usenamePassword.substring(usenamePassword.indexOf(":") + 1);
        return userRepo.findByUserName(username).orElseThrow(() -> new UserNotFoundException());
    }

}
