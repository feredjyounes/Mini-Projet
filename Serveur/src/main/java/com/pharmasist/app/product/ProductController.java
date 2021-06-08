package com.pharmasist.app.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pharmasist.app.user.User;

@Controller
public class ProductController {
	
	@Autowired
	ProductRepository productRepo;

	@RequestMapping(path="/Products")
	public @ResponseBody Iterable<Product> findAll() {
		return productRepo.findAll();
	}
	
	@RequestMapping(path="/Products/{id}")
	public @ResponseBody Optional<Product> findProduct(@PathVariable int id) {
		return productRepo.findById(id);
	}
	
	@RequestMapping(path="/ProductsUser/{id}/{order}")
	public @ResponseBody List<Product> findProductByUser(@PathVariable int id, @PathVariable int order) {
		User user = new User(id);
		List<Product> product = null;
		switch(order) {
			case 1:
				product = productRepo.findByUserOrderByProductName(user);
				break;
			case 2:
				product = productRepo.findByUserOrderByProductPrice(user);
				break;
			case 3:
				product = productRepo.findByUserOrderByProductExpiration(user);
				break;
		}
		return product;
	}
	
	@RequestMapping(path="/ProductsName/{pname}/{order}")
	public @ResponseBody List<Product> findProductByName(@PathVariable String pname, @PathVariable int order) {
		if(order == 1)
			return productRepo.findPoductByKeyword2(pname);
		else
			return productRepo.findPoductByKeyword(pname);
	}
	
	@RequestMapping(path="/ProductsList/{list}/{order}")
	public @ResponseBody List<Product> findProductByList(@PathVariable String list, @PathVariable int order) {
		List<Product> finalList = new ArrayList<>();
		String productsList[] = list.split(","); 
		if(order == 1) {
			for(int i=0; i<productsList.length; i++) {
				Product product = productRepo.findPoductByKeyword2(productsList[i].trim()).get(0);
				finalList.add(product);
			}
			return finalList;
		}else {
			for(int i=0; i<productsList.length; i++) {
				Product product = productRepo.findPoductByKeyword(productsList[i].trim()).get(0);
				finalList.add(product);
			}
			return finalList;
		}
	}
	
	@PostMapping(path="/Products/Add")
	public @ResponseBody String addProduct(@ModelAttribute("pr") Product product) {
		Product pr = new Product(product.getProductName(), product.getProductForm(), product.getProductCntr(),
				product.getProductQuantity(), product.getProductPrice(), product.getProductExpiration(), product.getUser());
		productRepo.save(pr);
		return "{\"response\":0}";
	}
	
	@RequestMapping(path="/Products/Update", method=RequestMethod.PUT)
	public @ResponseBody String updateProduct(@ModelAttribute("pr") Product product) {
		Product pr = new Product(product.getProductId(), product.getProductName(), product.getProductForm(), product.getProductCntr(),
				product.getProductQuantity(), product.getProductPrice(), product.getProductExpiration(), product.getUser());
		productRepo.save(pr);
		return "{\"response\":0}";
	}
	
	@RequestMapping(path="/Products/Delete/{id}", method=RequestMethod.DELETE)
	public @ResponseBody String deleteProduct(@PathVariable int id) {
		productRepo.deleteById(id);
		return "{\"response\":0}";
	}
	
}
