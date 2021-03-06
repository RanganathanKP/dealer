package com.spares.dealer;


import com.spares.dealer.entity.OrderDetailEntity;
import com.spares.dealer.service.DealerFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name="customer-client",url = "localhost:8085",fallback = DealerFallBack.class)
public interface CustomerServiceProxy {


    @GetMapping("/sparecustomer/customer/getOrderDetail/{orderDetailID}")
    @ResponseBody
    public  ResponseEntity<OrderDetailEntity>getorderdetail(@PathVariable Integer orderDetailID);

    @PostMapping("/sparecustomer/customer/updatestatus/")
    @ResponseBody
    public  ResponseEntity<OrderDetailEntity> updateOrderStatus(@RequestHeader String authorization,@RequestBody OrderDetailEntity orderDetailEntity);

    @GetMapping("/sparecustomer/orderDetailEntities/search/findByDealerID?userid={dealerID}")
    @ResponseBody
    public CollectionModel<OrderDetailEntity> findByDealerID(@PathVariable Integer dealerID);

    @GetMapping("/sparecustomer/orderDetailEntities/search/findByOrderDetailStatusAndDealerID?status={status}&userid={dealerID}")
    @ResponseBody
    public  CollectionModel<OrderDetailEntity>findByOrderDetailStatusAndDealerID(@PathVariable String status,@PathVariable Integer dealerID);


}
