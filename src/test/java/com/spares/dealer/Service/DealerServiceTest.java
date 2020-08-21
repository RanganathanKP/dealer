package com.spares.dealer.Service;

import com.spares.dealer.CustomerServiceProxy;
import com.spares.dealer.entity.*;
import com.spares.dealer.exception.ProductException;
import com.spares.dealer.exception.UserNotFoundException;
import com.spares.dealer.repository.ProductRepository;
import com.spares.dealer.repository.UserRepository;
import com.spares.dealer.service.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
@RunWith(SpringRunner.class)
public class DealerServiceTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CustomerServiceProxy customerServiceProxy;

    private ProductService productService;

    @Before
    public void init(){
        productService= new ProductService( userRepo,  productRepository, customerServiceProxy);
    }

    @Test
    public void saveProductTest(){
        when(userRepo.findByUserName("Dealer")).thenReturn(getuser());
        when(productRepository.save(any())).thenReturn(getprods().get(0));
        ProductEntity response = productService.saveProduct(getprods().get(0),"Basic RGVhbGVyOmRlYWxlcnBhc3N3b3Jk");
        assertEquals(1,response.getProductId());
    }

    @Test
    public void updateProductTest(){
        when(userRepo.findByUserName("Dealer")).thenReturn(getuser());
        Optional<ProductEntity> list=getprods().stream().findFirst();
        when(productRepository.findById(any())).thenReturn(list);
        when(productRepository.save(any())).thenReturn(getprods().get(0));
        ProductEntity response = productService.updateProduct(getprods().get(0),1,"Basic RGVhbGVyOmRlYWxlcnBhc3N3b3Jk");
        assertEquals(1,response.getProductId());
    }

    @Test
    public void updateProductNegativeTest() {
        when(userRepo.findByUserName("Dealer")).thenReturn(getuser());
        Optional<ProductEntity> list = getprods().stream().findFirst();
        list.get().setProductUserID(5);
        when(productRepository.findById(any())).thenReturn(list);
        when(productRepository.save(any())).thenReturn(getprods().get(0));
        try {
            ProductEntity response = productService.updateProduct(getprods().get(0), 1, "Basic RGVhbGVyOmRlYWxlcnBhc3N3b3Jk");
        } catch (UserNotFoundException e) {
            assertEquals(null, e.getMessage());
        }
    }

    @Test
    public void updateOrderStatusNegativeTest(){
        when(userRepo.findByUserName("Dealer")).thenReturn(getuser());
        ResponseEntity<OrderDetailEntity> proxyresponse = new ResponseEntity<>(getdetails().get(0), HttpStatus.OK);
        proxyresponse.getBody().setOrderdetailId(null);
        when(customerServiceProxy.getorderdetail(any())).thenReturn(proxyresponse);
        when(customerServiceProxy.updateOrderStatus(any(), any())).thenReturn(proxyresponse);
        try {
            OrderDetailEntity response = productService.updateOrderStatus(1, "Basic RGVhbGVyOmRlYWxlcnBhc3N3b3Jk");
        }catch(ProductException e){
            assertEquals("Enter valid order Detail ID.",e.getMessage());
        }
    }

    @Test
    public void updateOrderStatusNegativeTest2(){
        when(userRepo.findByUserName("Dealer")).thenReturn(getuser());
        ResponseEntity<OrderDetailEntity> proxyresponse = new ResponseEntity<>(getdetails().get(0), HttpStatus.OK);
        proxyresponse.getBody().setDealerID(5);
        when(customerServiceProxy.getorderdetail(any())).thenReturn(proxyresponse);
        when(customerServiceProxy.updateOrderStatus(any(), any())).thenReturn(proxyresponse);
        try {
            OrderDetailEntity response = productService.updateOrderStatus(1, "Basic RGVhbGVyOmRlYWxlcnBhc3N3b3Jk");
        }catch(ProductException e){
            assertEquals("Order does not belong to the User.",e.getMessage());
        }
    }

    @Test
    public void updateOrderStatusNegativeTest3(){
        Optional<UserEntity> user = getuser();
        user.get().setUserRole("customer");
        when(userRepo.findByUserName("Dealer")).thenReturn(user);
        ResponseEntity<OrderDetailEntity> proxyresponse = new ResponseEntity<>(getdetails().get(0), HttpStatus.OK);
        when(customerServiceProxy.getorderdetail(any())).thenReturn(proxyresponse);
        when(customerServiceProxy.updateOrderStatus(any(), any())).thenReturn(proxyresponse);
        try {
            OrderDetailEntity response = productService.updateOrderStatus(1, "Basic RGVhbGVyOmRlYWxlcnBhc3N3b3Jk");
        }catch(ProductException e){
            assertEquals("Only Dealer can update The Order",e.getMessage());
        }
    }

    @Test
    public void updateOrderStatusTest(){
        when(userRepo.findByUserName("Dealer")).thenReturn(getuser());
        ResponseEntity<OrderDetailEntity> proxyresponse = new ResponseEntity<>(getdetails().get(0), HttpStatus.OK);
        when(customerServiceProxy.getorderdetail(any())).thenReturn(proxyresponse);
        when(customerServiceProxy.updateOrderStatus(any(), any())).thenReturn(proxyresponse);
        OrderDetailEntity response = productService.updateOrderStatus(1,"Basic RGVhbGVyOmRlYWxlcnBhc3N3b3Jk");
        assertEquals(1,response.getOrderdetailId());
    }

    @Test
    public void viewProductTest(){
        when(productRepository.findAll()).thenReturn(getprods());
        List<ProductEntity> response = productService.viewProduct();
        assertEquals(1,response.get(0).getProductId());
    }

    @Test
    public void viewproductbyidTest(){
        when(productRepository.findById(any())).thenReturn(getprods().stream().findFirst());
        ProductEntity response = productService.viewproductbyid(1);
        assertEquals(1,response.getProductId());
    }


    @Test
    public void allOrderOfDealerTest(){
        when(userRepo.findByUserName("Dealer")).thenReturn(getuser());
        when(customerServiceProxy.findByDealerID(any())).thenReturn(null);
        try {
            List<OrderDetailEntity> response = productService.allOrderOfDealer("Basic RGVhbGVyOmRlYWxlcnBhc3N3b3Jk");
        }catch(ProductException e){
            assertEquals("No Orders Placed",e.getMessage());
        }
    }

    @Test
    public void allOrderOfDealerTesttwo(){
        when(userRepo.findByUserName("Dealer")).thenReturn(getuser());
        when(customerServiceProxy.findByDealerID(any())).thenReturn(null);
        try {
            List<OrderDetailEntity> response = productService.allOrderOfDealer("Basic RGVhbGVyOmRlYWxlcnBhc3N3b3Jk");
        }catch(ProductException e){
            assertEquals("No Orders Placed",e.getMessage());
        }
    }


    @Test
    public void placedOrderOfDealerTest(){
        Collection c= getdetails();
        CollectionModel response = new CollectionModel(c);
        when(userRepo.findByUserName("Dealer")).thenReturn(getuser());
        when(customerServiceProxy.findByOrderDetailStatusAndDealerID(any(),any())).thenReturn(null);
        try {
            List<OrderDetailEntity> responseService = productService.placedOrderOfDealer("Basic RGVhbGVyOmRlYWxlcnBhc3N3b3Jk");
        }catch(ProductException e){
            assertEquals("No Orders Placed",e.getMessage());
        }
    }

    @Test
    public void placedOrderOfDealerTesttwo(){
        Collection c= getdetails();
        CollectionModel response = new CollectionModel(c);
        when(userRepo.findByUserName("Dealer")).thenReturn(getuser());
        when(customerServiceProxy.findByOrderDetailStatusAndDealerID(any(),any())).thenReturn(response);
        List<OrderDetailEntity> responseService = productService.placedOrderOfDealer("Basic RGVhbGVyOmRlYWxlcnBhc3N3b3Jk");
        assertEquals(1,responseService.get(0).getOrderdetailId());
    }

    public List<RatingEntity> getratings(){
        RatingEntity rating= new RatingEntity();
        List<RatingEntity>ratings= new ArrayList();
        rating.setProductid(1);
        rating.setRatingId(1);
        rating.setUserid(1);
        rating.setRating(1);
        ratings.add(rating);
        return ratings;
    }
    public Optional<UserEntity> getuser(){
        UserEntity user= new UserEntity();
        user.setUserId(1);
        user.setUserName("user");
        user.setUserRole("dealer");
        user.setPassword("cGFzcw==");
        return Optional.of(user);
    }

    public List<OrderEntity> getorder(){
        List<OrderEntity> orders= new ArrayList();
        OrderEntity order = new OrderEntity();
        order.setOrderId(1);
        order.setUserid(1);
        order.setOrdertotaltmount(200);
        order.setOrderDetailEntityList(getdetails());
        orders.add(order);
        return orders;
    }

    public List<OrderDetailEntity>getdetails(){
        List<OrderDetailEntity> orders= new ArrayList();
        OrderDetailEntity order = new OrderDetailEntity();
        order.setDealerID(1);
        order.setOrderDetailStatus("PLACED");
        order.setUserID(1);
        order.setProductID(1);
        order.setOrderdetailId(1);
        order.setOrderDetailQuantity(1);
        order.setOrderID(1);
        orders.add(order);
        return orders;
    }

    public List<ProductEntity>getprods() {
        List<ProductEntity> prods = new ArrayList();
        ProductEntity prod = new ProductEntity();
        prod.setProductId(1);
        prod.setProductAmount(100);
        prod.setProductUserID(1);
        prod.setProductName("prod name");
        prod.setProductDescription("prod desc");
        prods.add(prod);
        return prods;
    }
}
