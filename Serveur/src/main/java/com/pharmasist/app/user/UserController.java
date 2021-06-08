package com.pharmasist.app.user;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.pharmasist.app.product.Product;
import com.pharmasist.app.product.ProductRepository;

@Controller
public class UserController {
	
	@Autowired /* Auto generated */
	UserRepository userRepository;
	@Autowired
	ProductRepository productRepo;
	
	/* Find all users */
	@RequestMapping(path="/Users")
	public @ResponseBody Iterable<User> findAll() {
		return userRepository.findAll();
	}
	
	/* Find one user */
	@RequestMapping(path="/Users/{id}")
	public @ResponseBody Optional<User> findUser(@PathVariable int id) {
		return userRepository.findById(id);
	}
	
	/* Find user by username and password */
	@PostMapping(path="/Users/Check")
	public @ResponseBody List<User> findByUsernameAndPass(@RequestParam("username") String username, @RequestParam("pass") String pass) {
		List<User> user = userRepository.findByUsernameAndPass(username, pass);
		return user;
	}
	
	/* Add user */
	@PostMapping(path="/Users/Add")
	public @ResponseBody String addUser(@ModelAttribute("u") User user) {
		
		if(userRepository.findByPhone(user.getPhone()).size() <= 0) {
			if(userRepository.findByUsername(user.getUsername()).size() <= 0) {
				User u = new User(user.getFname(), user.getLname(), user.getPhone(), user.getUsername(), user.getPass(),
						user.getLocationx(), user.getLocationy(), user.getMoorningStart(), user.getMoorningEnd(),
						user.getEveningStart(), user.getEveningEnd());
				userRepository.save(u);
				return "{\"response\":0}";
			}else {
				return "{\"response\":1}";
			}
		}else {
			return "{\"response\":2}";
		}
		
	}
	
	/* Update user */
	@RequestMapping(path="/Users/Update", method=RequestMethod.PUT)
	public @ResponseBody String updateUser(@ModelAttribute("u") User user) {
		User u = new User(user.getId(), user.getFname(), user.getLname(), user.getPhone(), user.getUsername(), user.getPass(),
				user.getLocationx(), user.getLocationy(), user.getMoorningStart(), user.getMoorningEnd(),
				user.getEveningStart(), user.getEveningEnd());
		userRepository.save(u);
		return "{\"response\":0}";
	}
	
	/* Delete user by id */
	@RequestMapping(path="/Users/Delete/{id}", method=RequestMethod.DELETE)
	public @ResponseBody String deleteUser(@PathVariable int id) {
		deleteUserProducts(id);
		userRepository.deleteById(id);
		return "{\"response\":0}";
	}
	
	public void deleteUserProducts(int id) {
		User user = new User(id);
		List<Product> productsList = productRepo.findByUser(user);
		for(Product p : productsList) {
			productRepo.deleteById(p.getProductId());
		}
	}
}
