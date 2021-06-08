package com.pharmasist.app.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pharmasist.app.user.User;

public interface ProductRepository extends JpaRepository<Product, Integer>{
	List<Product> findByUserOrderByProductExpiration(User user);
	List<Product> findByUserOrderByProductName(User user);
	List<Product> findByUserOrderByProductPrice(User user);
	List<Product> findByUser(User user);
	@Query(value="select * from Product p where CURRENT_DATE <= p.product_expiration and p.product_name like %:keyword% order by p.product_price asc", nativeQuery = true)
	List<Product> findPoductByKeyword(@Param("keyword") String keyword);
	@Query(value="select * from Product p where CURRENT_DATE <= p.product_expiration and p.product_name like %:keyword% order by p.product_name asc", nativeQuery = true)
	List<Product> findPoductByKeyword2(@Param("keyword") String keyword);
}
