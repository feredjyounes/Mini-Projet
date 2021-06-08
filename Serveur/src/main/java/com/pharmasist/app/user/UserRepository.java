package com.pharmasist.app.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer>{

	List<User> findByUsernameAndPass(String username, String pass);
	List<User> findByUsername(String username);
	List<User> findByPhone(String phone);

}
