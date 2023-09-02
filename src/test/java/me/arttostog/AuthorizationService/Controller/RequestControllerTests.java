package me.arttostog.AuthorizationService.Controller;

import me.arttostog.AuthorizationService.Repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RequestControllerTests {
	@Autowired
	private MockMvc mvc;
	@Autowired
	private UserRepo Repo;

	@Test
	public void Registration_UserIsNull() throws Exception {
		MvcResult Result;

		Result = mvc.perform(post("/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\": \"\", \"password\": \"pass\"}"))
				.andExpect(status().isOk())
				.andReturn();
		if (!Result.getResponse().getContentAsString().equals("User is null!")) throw new Exception("User is not null!");

		Result = mvc.perform(post("/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\": \"user\", \"password\": \"\"}"))
				.andExpect(status().isOk())
				.andReturn();
		if (!Result.getResponse().getContentAsString().equals("User is null!")) throw new Exception("User is not null!");

		Result = mvc.perform(post("/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\": \"user\", \"password\": \"pass\", \"id\": \"1\"}"))
				.andExpect(status().isOk())
				.andReturn();
		if (!Result.getResponse().getContentAsString().equals("User is null!")) throw new Exception("User is not null!");
	}

	@Test
	public void RegistrationAndLogin_AlreadyRegisteredAndSuccess() throws Exception {
		MvcResult Result;

		String user = UUID.randomUUID().toString();
		String pass = UUID.randomUUID().toString();

		Result = mvc.perform(post("/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\": \""+ user +"\", \"password\": \""+ pass +"\"}"))
				.andExpect(status().isOk())
				.andReturn();
		if (!Result.getResponse().getContentAsString().equals("Registration completed successfully!")) throw new Exception("Registration completed is not successfully!");

		Result = mvc.perform(post("/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\": \""+ user +"\", \"password\": \""+ pass +"\"}"))
				.andExpect(status().isOk())
				.andReturn();
		if (!Result.getResponse().getContentAsString().equals("This username is already registered!")) throw new Exception("This username is not already registered!");

		Result = mvc.perform(post("/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\": \""+ user +"\", \"password\": \""+ pass +"\"}"))
				.andExpect(status().isOk())
				.andReturn();
		if (!Result.getResponse().getContentAsString().equals("Login successful!")) throw new Exception("Login is not successful!");

		Repo.deleteById(Repo.findByUsername(user).getId());
	}

	@Test
	public void Login_UserIsNull() throws Exception {
		MvcResult Result;

		mvc.perform(post("/login"))
				.andExpect(status().isBadRequest());

		Result = mvc.perform(post("/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\": \"\", \"password\": \"password\"}"))
				.andExpect(status().isOk())
				.andReturn();
		if (!Result.getResponse().getContentAsString().equals("User is null!")) throw new Exception("User is not null!");

		Result = mvc.perform(post("/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\": \"user\", \"password\": \"\"}"))
				.andExpect(status().isOk())
				.andReturn();
		if (!Result.getResponse().getContentAsString().equals("User is null!")) throw new Exception("User is not null!");
	}

	@Test
	public void Login_WrongUsernameOrPassword() throws Exception {
		MvcResult Result = mvc.perform(post("/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\": \""+ UUID.randomUUID() +"\", \"password\": \""+ UUID.randomUUID() +"\"}"))
				.andExpect(status().isOk())
				.andReturn();
		if (!Result.getResponse().getContentAsString().equals("Wrong username or password!")) throw new Exception("Not wrong username or password!");
	}
}
