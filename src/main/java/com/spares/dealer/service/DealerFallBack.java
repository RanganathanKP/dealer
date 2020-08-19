package com.spares.dealer.service;

import com.spares.dealer.CustomerServiceProxy;
import com.spares.dealer.entity.OrderDetailEntity;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


@Component
public class DealerFallBack implements CustomerServiceProxy {


    @Override
    public ResponseEntity<OrderDetailEntity> getorderdetail(Integer orderDetailID) {
        return new ResponseEntity<>(new OrderDetailEntity(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<OrderDetailEntity> updateOrderStatus(String Authorization, OrderDetailEntity orderDetailEntity) {
        return new ResponseEntity<>(new OrderDetailEntity(), HttpStatus.OK);
    }

    @Override
    public CollectionModel<OrderDetailEntity> findByDealerID(Integer dealerID) {
        return null;
    }

    @Override
    public CollectionModel<OrderDetailEntity> findByOrderDetailStatusAndDealerID(String status, Integer dealerID) {
        return null;
    }
}
