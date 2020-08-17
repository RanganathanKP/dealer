package com.spares.dealer.service;


import com.spares.dealer.entity.UserEntity;
import com.spares.dealer.entity.ProductEntity;
import com.spares.dealer.exception.ProductNotFoundException;
import com.spares.dealer.exception.UserNotFoundException;
import com.spares.dealer.repository.ProductRepository;
import com.spares.dealer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ProductRepository productRepository;

    public ProductEntity saveProduct(ProductEntity product, String authorization) {
        UserEntity user = getLoginedInUser(authorization);
        product.setProductUserID(user.getUserId());
        return productRepository.save(product);
    }

    public List<ProductEntity> viewProduct(Integer userID) {
        List<ProductEntity> response;
        response= Optional.ofNullable(productRepository.findAll()).orElseThrow(() -> new ProductNotFoundException());
        if(Optional.of(userID).isPresent()) {
            response = response.stream().filter(product -> product.getProductUserID() == userID).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(response)){
                throw new ProductNotFoundException();
            }
        }
        return response;
    }

    public ProductEntity updateProduct(ProductEntity product, Integer productID, String authorization) {
            ProductEntity response=new ProductEntity();
            UserEntity user = getLoginedInUser(authorization);
            ProductEntity productDetails = productRepository.findById(productID).orElseThrow(() -> new ProductNotFoundException());
            if (productDetails.getProductUserID()==user.getUserId()) {
                product.setProductId(productID);
                product.setProductUserID(user.getUserId());
                response= productRepository.save(product);
            } else {
                throw new UserNotFoundException();
            }
            return response;
    }

    public List<ProductEntity> viewLatestProduct(Integer userID) {
        List<ProductEntity> response=Optional.ofNullable(productRepository.findlatestProduct()).orElseThrow(() -> new ProductNotFoundException());
        if(Optional.ofNullable(userID).isPresent()){
            response= response.stream().filter(product->product.getProductUserID()==userID).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(response)){
                throw new ProductNotFoundException();
            }
        }
        return response;
    }


    private UserEntity getLoginedInUser(@RequestHeader String authorization) {
        String encodedPass = authorization.substring("Basic".length()).trim();
        byte[] actualByte = Base64.getDecoder().decode(encodedPass);
        String usenamePassword = new String(actualByte);
        String username = usenamePassword.substring(0, usenamePassword.indexOf(":"));
        String password = usenamePassword.substring(usenamePassword.indexOf(":") + 1);
        return userRepo.findByUserName(username).orElseThrow(()->new UserNotFoundException());
    }

}
