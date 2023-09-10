package me.arttostog.AuthorizationService.Controller;

import com.google.gson.Gson;
import me.arttostog.AuthorizationService.User.Response;
import me.arttostog.AuthorizationService.User.User;
import me.arttostog.AuthorizationService.User.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Random;

@Controller
public class RequestController {
	@Autowired
	private UserRepo repo;

	@Autowired
	private Gson gson;

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody User user) {
		if (register_ifNull(user)) {
			return register_registerUser(user);
		}
		return ResponseEntity.ok(gson.toJson(new Response(400,"User is null!")));
	}

	private boolean register_ifNull(User user) {
		return user != null && user.getUsername() != null && user.getPassword() != null && user.getMail() != null
				&& !user.getUsername().isEmpty() && !user.getPassword().isEmpty() && !user.getMail().isEmpty();
	}

	private ResponseEntity<?> register_registerUser(User user) {
		if (register_ifCoincidences(user)) {
			repo.save(new User(user.getMail(), user.getUsername(), user.getPassword()));
			return ResponseEntity.ok(gson.toJson(new Response(200, "Registration completed successfully!")));
		}
		return ResponseEntity.ok(gson.toJson(new Response(400, "This mail or username is already registered!")));
	}

	private boolean register_ifCoincidences(User user) {
		return repo.findByUsername(user.getUsername()) == null && repo.findByMail(user.getMail()) == null;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody User user) {
		if (login_ifNull(user)) {
			return login_loginUser(user);
		}
		return ResponseEntity.ok(gson.toJson(new Response(400, "User is null!")));
	}

	private boolean login_ifNull(User user) {
		return user != null && user.getUsername() != null && user.getPassword() != null
				&& !user.getUsername().isEmpty() && !user.getPassword().isEmpty();
	}

	private ResponseEntity<?> login_loginUser(User user) {
		User OriginalUser = repo.findByUsername(user.getUsername());
		if (OriginalUser != null && OriginalUser.getPassword().equals(user.getPassword())) {
			OriginalUser.setAccessToken(createToken());
			repo.save(OriginalUser);
			return ResponseEntity.ok(gson.toJson(new Response(200, OriginalUser)));
		}
		return ResponseEntity.ok(gson.toJson(new Response(400, "Wrong username or password!")));
	}

	private Long createToken() {
		Long accessToken = new Random().nextLong();
		if (repo.findByAccessToken(accessToken) == null) {
			return accessToken;
		}
		return createToken();
	}
}
