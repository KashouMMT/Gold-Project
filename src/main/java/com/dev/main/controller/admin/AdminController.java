package com.dev.main.controller.admin;

import java.util.HashMap;
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
public class AdminController {

	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
	
	private UserService userService;
	
	private OtherUtility otherUtility;
	
	public AdminController(UserService userService,OtherUtility otherUtility) {
		this.userService = userService;
		this.otherUtility = otherUtility;
	}
	
	@GetMapping("/user-admin")
	public String adminListController(Model model,Authentication authentication) {
		logger.info("GET request received for /admin/user-admin");
		
		String username = otherUtility.authentication(authentication);
		
		if (username.equals("failed")) {
			return "redirect:/auth/login";
		}
		
		List<String> columns = List.of("ID","Name","Email","Enabled","Actions");
		List<User> users = userService.getAllUsers();
	    Map<String, Object> buttonActions = new HashMap<>();
	    buttonActions.put("deleteAction", "/admin/delete-user/{id}");
	    buttonActions.put("deleteActionText", "Delete User");
		List<Map<String,Object>> items = users.stream()
			    .filter(u -> u.getRoles().stream()
			        .anyMatch(r -> r.getRoleName().equals("ROLE_ADMIN")))
			    .map(u -> {
			    	  Map<String,Object> m = new LinkedHashMap<>();
			    	  m.put("id", u.getId());
			    	  m.put("name", u.getName());
			    	  m.put("email", u.getEmail());
			    	  m.put("enabled", u.isEnabled());
			    	  m.put("actions", buttonActions);
			    	  return m;
			    }).collect(Collectors.toList());
	    model.addAttribute("addAction", "/admin/add-user");
	    model.addAttribute("addActionText", "Add New Admin");
		model.addAttribute("items",items);
		model.addAttribute("columns",columns);
		model.addAttribute("tableName","Admin Table");
		model.addAttribute("username",username);
		model.addAttribute("content","admin/content/admin-tables");
		
		return "admin/admin-layout";
	}
}
