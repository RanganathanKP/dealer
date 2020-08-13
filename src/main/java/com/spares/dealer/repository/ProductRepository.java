package com.spares.dealer.repository;

import com.spares.dealer.entity.productEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<productEntity, Integer>{

    @Query(value = "SELECT * FROM test_spare.product where productcreationtime>(current_date()-1);", nativeQuery = true)
    List<productEntity> findlatestProduct ();

}
