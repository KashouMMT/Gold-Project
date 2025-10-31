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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dev.main.dto.UserDto;
import com.dev.main.model.User;
import com.dev.main.service.UserService;
import com.dev.main.utils.FieldSpec;
import com.dev.main.utils.AuthenticationUtility;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
	
	private UserService userService;
	
	private AuthenticationUtility otherUtility;
	
	public AdminController(UserService userService,AuthenticationUtility otherUtility) {
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
		
		List<String> columns = List.of("ID","Name","Email","Enabled");
		List<User> users = userService.getAllUsers();
		List<Map<String,Object>> items = users.stream()
			    .filter(u -> u.getRoles().stream()
			        .anyMatch(r -> r.getRoleName().equals("ROLE_ADMIN")))
			    .map(u -> {
			    	  Map<String,Object> m = new LinkedHashMap<>();
			    	  m.put("id", u.getId());
			    	  m.put("name", u.getName());
			    	  m.put("email", u.getEmail());
			    	  m.put("enabled", u.isEnabled());
			    	  return m;
			    }).collect(Collectors.toList());
	    model.addAttribute("addAction", "/admin/add-admin");
	    model.addAttribute("addActionText", "Add New Admin");
		model.addAttribute("items",items);
		model.addAttribute("columns",columns);
		model.addAttribute("tableName","Admin Table");
		model.addAttribute("username",username);
		model.addAttribute("content","admin/content/admin-tables");
		
		return "admin/admin-layout";
	}
	
	@GetMapping("/add-admin")
	public String addAdminPage(Model model) {
		
		UserDto userDto = new UserDto();
		model.addAttribute("objectDto",userDto);
		
		List<FieldSpec> fields = List.of(
			new FieldSpec("name", "Name", "text", true, 150, null, null),
			new FieldSpec("email", "Email", "email",true,150,null,null),
			new FieldSpec("password","Password","password",true,150,null,null)
		);
		model.addAttribute("fields",fields);
		
		model.addAttribute("formAction","/admin/add-admin");
		model.addAttribute("submitLabel","Save Admin");
		model.addAttribute("formTitle","Add Admin");
		model.addAttribute("tableName","Admin Table");
		model.addAttribute("tableLink","/admin/user-admin");
		
		model.addAttribute("content","admin/content/admin-form");
		
		return "admin/admin-layout";
	}
	
	@PostMapping("/add-admin")
	public String postAddAdminPage(@Valid @ModelAttribute("objectDto")UserDto userDto,
		BindingResult result, Model model) {
		
		if(result.hasErrors()) {
			model.addAttribute("objectDto",userDto);
			List<FieldSpec> fields = List.of(
				new FieldSpec("name", "Name", "text", true, 150, null, null),
				new FieldSpec("email", "Email", "email",true,150,null,null),
				new FieldSpec("password","Password","password",true,150,null,null)
			);
			model.addAttribute("fields",fields);
			model.addAttribute("formAction","/admin/add-admin");
			model.addAttribute("submitLabel","Save Admin");
			model.addAttribute("formTitle","Add Admin");
			model.addAttribute("tableName","Admin Table");
			model.addAttribute("tableLink","/admin/user-admin");
			model.addAttribute("content","admin/content/admin-form");
			
			return "admin/admin-layout";
		}
		userService.createUser(userDto, true);
		
		return "redirect:/admin/user-admin";
	}
}
