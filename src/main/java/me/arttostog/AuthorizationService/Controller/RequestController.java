package me.arttostog.AuthorizationService.Controller;

import me.arttostog.AuthorizationService.Entity.User;
import me.arttostog.AuthorizationService.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class RequestController {
	@Autowired
	private UserRepo Repo;

	@PostMapping("/register")
	public ResponseEntity<?> Register(@RequestBody User user) {
		if (user != null && user.getId() == null && !user.getUsername().isEmpty() && !user.getPassword().isEmpty()) {
			if (Repo.findByUsername(user.getUsername()) == null) {
				Repo.save(new User(user.getUsername(), user.getPassword()));
				return ResponseEntity.ok("Registration completed successfully!");
			}
			return ResponseEntity.ok("This username is already registered!");
		}
		return ResponseEntity.ok("User is null!");
	}

	@PostMapping("/login")
	public ResponseEntity<?> Login(@RequestBody User user) {
		if (user != null && !user.getUsername().isEmpty() && !user.getPassword().isEmpty()) {
			User OriginalUser = Repo.findByUsername(user.getUsername());
			if (OriginalUser != null && OriginalUser.getPassword().equals(user.getPassword())) {
				return ResponseEntity.ok("Login successful!");
			}
			return ResponseEntity.ok("Wrong username or password!");
		}
		return ResponseEntity.ok("User is null!");
	}
}
