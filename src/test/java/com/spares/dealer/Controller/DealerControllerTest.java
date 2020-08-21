package com.spares.dealer.Controller;

import com.spares.dealer.controller.ControllerAdvisor;
import com.spares.dealer.controller.DealerController;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;
import com.spares.dealer.entity.*;
import com.spares.dealer.service.ProductService;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
@RunWith(SpringRunner.class)
public class DealerControllerTest {

    @Mock
    private ControllerAdvisor controllerAdvisor;

    @InjectMocks
    private DealerController dealerController;

    @Mock
    private ProductService productService;



    @Test
    public void saveproductTest(){
        when(productService.saveProduct(any(),any())).thenReturn(getprods().get(0));
        ResponseEntity<ProductEntity> serviceResponse = dealerController.saveproduct(getprods().get(0),"auth");
        assertEquals(1,serviceResponse.getBody().getProductId());
    }


    @Test
    public void updateproductTest(){
        when( productService.updateProduct(any(),any(),any())).thenReturn(getprods().get(0));
        ResponseEntity<ProductEntity> serviceResponse = dealerController.updateproduct(getprods().get(0),1,"auth");
        assertEquals(1,serviceResponse.getBody().getProductId());
    }


    @Test
    public void updateOrderStatusTest(){
        when( productService.updateOrderStatus(any(),any())).thenReturn(getdetails().get(0));
        ResponseEntity<OrderDetailEntity> serviceResponse = dealerController.updateOrderStatus(1,"auth");
        assertEquals(1,serviceResponse.getBody().getOrderdetailId());
    }


    @Test
    public void findAllProductTest(){
        when( productService.viewProduct()).thenReturn(getprods());
        ResponseEntity<List<ProductEntity>> serviceResponse = dealerController.findAllProduct();
        assertEquals(1,serviceResponse.getBody().get(0).getProductId());
    }


    @Test
    public void findProductByIDTest(){
        when( productService.viewproductbyid(any())).thenReturn(getprods().get(0));
        ResponseEntity<ProductEntity> serviceResponse = dealerController.findProductByID(1);
        assertEquals(1,serviceResponse.getBody().getProductId());
    }

    @Test
    public void getOrderOfDealerTest(){
        when( productService.allOrderOfDealer(any())).thenReturn(getdetails());
        ResponseEntity<List<OrderDetailEntity>> serviceResponse = dealerController.getOrderOfDealer("auth");
        assertEquals(1,serviceResponse.getBody().get(0).getOrderdetailId());
    }



    @Test
    public void getOrderOfDealerPlacedTest(){
        when( productService.placedOrderOfDealer(any())).thenReturn(getdetails());
        ResponseEntity<List<OrderDetailEntity>> serviceResponse = dealerController.getOrderOfDealerPlaced("auth");
        assertEquals(1,serviceResponse.getBody().get(0).getOrderdetailId());
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
