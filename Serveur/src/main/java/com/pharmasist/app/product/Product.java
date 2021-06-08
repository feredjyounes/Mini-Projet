package com.pharmasist.app.product;

import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.pharmasist.app.user.User;

@Entity
@Table(name="product") 
public class Product {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int productId;
	private String productName;
	private String productForm;
	private String productCntr;
	private int productQuantity;
	private double productPrice;
	private Date productExpiration;
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;
	
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductForm() {
		return productForm;
	}
	public void setProductForm(String productForm) {
		this.productForm = productForm;
	}
	public String getProductCntr() {
		return productCntr;
	}
	public void setProductCntr(String productCntr) {
		this.productCntr = productCntr;
	}
	public int getProductQuantity() {
		return productQuantity;
	}
	public void setProductQuantity(int productQuantity) {
		this.productQuantity = productQuantity;
	}
	public double getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Date getProductExpiration() {
		return productExpiration;
	}
	public void setProductExpiration(Date productExpiration) {
		this.productExpiration = productExpiration;
	}
	
	public Product(int productId, String productName, String productForm, String productCntr, int productQuantity,
			double productPrice, Date productExpiration, User user) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.productForm = productForm;
		this.productCntr = productCntr;
		this.productQuantity = productQuantity;
		this.productPrice = productPrice;
		this.productExpiration = productExpiration;
		this.user = user;
	}
	
	public Product(String productName, String productForm, String productCntr, int productQuantity, double productPrice,
			Date productExpiration, User user) {
		super();
		this.productName = productName;
		this.productForm = productForm;
		this.productCntr = productCntr;
		this.productQuantity = productQuantity;
		this.productPrice = productPrice;
		this.productExpiration = productExpiration;
		this.user = user;
	}
	public Product() {
		super();
	}
	public Product(User user) {
		super();
		this.user = user;
	}
	
}
