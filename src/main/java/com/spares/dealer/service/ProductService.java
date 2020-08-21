package com.spares.dealer.service;


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

    static final String NO_ORDER_PLACED="No Orders Placed";

    public ProductService(UserRepository userRepo, ProductRepository productRepository, CustomerServiceProxy customerServiceProxy) {
        this.userRepo = userRepo;
        this.productRepository = productRepository;
        this.customerServiceProxy = customerServiceProxy;
    }

    public ProductEntity saveProduct(ProductEntity product, String authorization) {
        UserEntity user = getLoginedInUser(authorization);
        product.setProductUserID(user.getUserId());
        return productRepository.save(product);
    }

    public ProductEntity updateProduct(ProductEntity product, Integer productID, String authorization) {
        UserEntity user = getLoginedInUser(authorization);
        ProductEntity productDetails = productRepository.findById(productID).orElseThrow(ProductNotFoundException::new);
        if (productDetails.getProductUserID().equals(user.getUserId())) {
            product.setProductId(productID);
            product.setProductUserID(user.getUserId());
            return  productRepository.save(product);
        } else {
            throw new UserNotFoundException();
        }

    }

    public OrderDetailEntity updateOrderStatus(Integer orderDetailID, String auth) {
        UserEntity user = getLoginedInUser(auth);
        if(user.getUserRole().equalsIgnoreCase("Dealer")) {
            ResponseEntity<OrderDetailEntity> orderDetail = customerServiceProxy.getorderdetail(orderDetailID);
            if (!(Optional.ofNullable(orderDetail.getBody()).isPresent() && Optional.ofNullable(orderDetail.getBody().getOrderdetailId()).isPresent())) {
                throw new ProductException("Enter valid order Detail ID.");
            } else {
                if (user.getUserId().equals(orderDetail.getBody().getDealerID())) {
                    orderDetail.getBody().setOrderDetailStatus("DISPATCHED");
                    return  customerServiceProxy.updateOrderStatus(auth, orderDetail.getBody()).getBody();
                } else {
                    throw new ProductException("Order does not belong to the User.");
                }
            }
        }else{
            throw new ProductException("Only Dealer can update The Order");
        }
    }

    public List<ProductEntity> viewProduct() {
        List<ProductEntity> response;
        response = Optional.ofNullable(productRepository.findAll()).orElseThrow(ProductNotFoundException::new);
        return response;
    }


    public ProductEntity viewproductbyid(Integer productid) {
        return productRepository.findById(productid).orElseThrow(ProductNotFoundException::new);
    }


    public List<OrderDetailEntity> allOrderOfDealer(String auth) {
        UserEntity user = getLoginedInUser(auth);
        CollectionModel<OrderDetailEntity> orderDetail = customerServiceProxy.findByDealerID(user.getUserId());
        if(Optional.ofNullable(orderDetail).isPresent()){
            return new ArrayList<>(orderDetail.getContent());
        }else{
            throw new ProductException(NO_ORDER_PLACED);
        }
    }

    public List<OrderDetailEntity> placedOrderOfDealer(String auth) {
        UserEntity user = getLoginedInUser(auth);
        CollectionModel<OrderDetailEntity> orderDetail = customerServiceProxy.findByOrderDetailStatusAndDealerID("PLACED", user.getUserId());
        if(Optional.ofNullable(orderDetail).isPresent()) {
                return new ArrayList<>(orderDetail.getContent());
        }else{
            throw new ProductException(NO_ORDER_PLACED);
        }
    }


    private UserEntity getLoginedInUser(@RequestHeader String authorization) {
        String encodedPass = authorization.substring("Basic".length()).trim();
        byte[] actualByte = Base64.getDecoder().decode(encodedPass);
        String usenamePassword = new String(actualByte);
        String username = usenamePassword.substring(0, usenamePassword.indexOf(":"));
        return userRepo.findByUserName(username).orElseThrow(ProductNotFoundException::new);
    }

}
