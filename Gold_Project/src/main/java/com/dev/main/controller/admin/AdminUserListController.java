package com.dev.main.controller.admin;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dev.main.model.User;
import com.dev.main.service.UserService;
import com.dev.main.utils.OtherUtility;

@Controller
@RequestMapping("/admin")
public class AdminUserListController {

	private static final Logger logger = LoggerFactory.getLogger(AdminUserListController.class);

	private UserService userService;
	
	private OtherUtility otherUtility;
	
	public AdminUserListController(UserService userService, OtherUtility otherUtility) {
		this.userService = userService;
		this.otherUtility = otherUtility;
	}
	
	@GetMapping("/user")
	public String userListPage(Model model, Authentication authentication) {
		
		logger.info("GET request received for /admin/user");
		
		String username = otherUtility.authentication(authentication);
		
		if (username.equals("failed")) {
			return "redirect:/auth/login";
		}
		
		List<String> columns = List.of("ID","Name","Email","Enabled");
		logger.debug("ProductList Page Columns defined: {}", columns);
		
		List<User> users = userService.getAllUsers();
		logger.info("Retrieved {} users from userService", users.size());
		
		List<Map<String,Object>> items = users.stream()
			    .filter(u -> u.getRoles().stream()
			        .anyMatch(r -> r.getRoleName().equals("ROLE_USER")))
			    .map(u -> {
			    	  Map<String,Object> m = new LinkedHashMap<>();
			    	  m.put("id", u.getId());
			    	  m.put("name", u.getName());
			    	  m.put("email", u.getEmail());
			    	  m.put("enabled", u.isEnabled());
			    	  return m;
			    })
			    .collect(Collectors.toList());
		logger.debug("Transformed {} users into display maps", items.size());

		model.addAttribute("items",items);
		logger.debug("Added attribute to model: key='items', value='{}'", items.toString());
		model.addAttribute("columns",columns);
		logger.debug("Added attribute to model: key='columns', value='{}'", columns.toString());
		model.addAttribute("table_name","User Table");
		logger.debug("Added attribute to model: key='table_name', value='{}'", "User Table");
		model.addAttribute("username",username);
		logger.debug("Added attribute to model: key='username', value='{}'", username);
		logger.info("Rendering admin dashboard for user: {}", username);
		
		model.addAttribute("content","admin/content/admin-tables");
				
		return "admin/admin-layout";
	}
}
