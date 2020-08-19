package com.spares.dealer.service;

import com.spares.dealer.CustomerServiceProxy;
import com.spares.dealer.entity.OrderDetailEntity;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


@Component
public class DealerFallBack implements CustomerServiceProxy {


    @Override
    public ResponseEntity<OrderDetailEntity> getorderdetail(Integer orderDetailID) {
        return null;
    }

    @Override
    public ResponseEntity<OrderDetailEntity> updateOrderStatus(String Authorization, OrderDetailEntity orderDetailEntity) {
        return null;
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
