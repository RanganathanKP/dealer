package com.spares.dealer.repository;

import com.spares.dealer.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer>{

	
	Optional<UserEntity> findByUserName(String userName);

	Optional<UserEntity> findByUserNameAndPassword(String username, String password);
}
