package com.spares.dealer.repository;

import com.spares.dealer.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer>{

    @Query(value = "SELECT * FROM test_spare.product where productcreationtime>(current_date()-1)", nativeQuery = true)
    List<ProductEntity> findlatestProduct();


    List<ProductEntity> findByProductUserID(Integer userid);
}
