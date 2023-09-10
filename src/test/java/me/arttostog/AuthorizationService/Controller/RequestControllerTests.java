package me.arttostog.AuthorizationService.Controller;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import me.arttostog.AuthorizationService.User.Repository.UserRepo;
import me.arttostog.AuthorizationService.User.Response;
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
	private UserRepo repo;
	@Autowired
	private Gson gson;

	@Test
	public void registration_UserIsNull() throws Exception {
		MvcResult Result;

		mvc.perform(post("/register"))
				.andExpect(status().isBadRequest());

		Result = mvc.perform(post("/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\": \"\", \"password\": \"pass\", \"mail\": \"mail\"}"))
				.andExpect(status().isOk())
				.andReturn();
		if (!Result.getResponse().getContentAsString().equals(
				gson.toJson(new Response(400,"User is null!")))) throw new Exception("User is not null!");

		Result = mvc.perform(post("/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\": \"user\", \"password\": \"\", \"mail\": \"mail\"}"))
				.andExpect(status().isOk())
				.andReturn();
		if (!Result.getResponse().getContentAsString().equals(
				gson.toJson(new Response(400,"User is null!")))) throw new Exception("User is not null!");

		Result = mvc.perform(post("/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\": \"user\", \"password\": \"pass\", \"mail\": \"\"}"))
				.andExpect(status().isOk())
				.andReturn();
		if (!Result.getResponse().getContentAsString().equals(
				gson.toJson(new Response(400,"User is null!")))) throw new Exception("User is not null!");
	}

	@Test
	public void registration_AlreadyRegistered_Login_Success() throws Exception {
		MvcResult Result;

		String user = UUID.randomUUID().toString(), pass = UUID.randomUUID().toString(), mail = UUID.randomUUID().toString();

		Result = mvc.perform(post("/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\": \""+ user +"\", \"password\": \""+ pass +"\", \"mail\": \""+ mail +"\"}"))
				.andExpect(status().isOk())
				.andReturn();
		if (!Result.getResponse().getContentAsString().equals(
				gson.toJson(new Response(200, "Registration completed successfully!"))))
			throw new Exception("Registration completed is not successfully!");

		Result = mvc.perform(post("/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\": \""+ user +"\", \"password\": \""+ pass +"\", \"mail\": \""+ mail +"\"}"))
				.andExpect(status().isOk())
				.andReturn();
		if (!Result.getResponse().getContentAsString().equals(
				gson.toJson(new Response(400, "This mail or username is already registered!"))))
			throw new Exception("This username or password is not already registered!");

		Result = mvc.perform(post("/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\": \""+ user +"\", \"password\": \""+ pass +"\"}"))
				.andExpect(status().isOk())
				.andReturn();
		if (JsonParser.parseString(Result.getResponse().getContentAsString()).getAsJsonObject().get("code").getAsInt() != 200) throw new Exception("Login is not successful!");

		repo.deleteById(repo.findByUsername(user).getId());
	}

	@Test
	public void login_UserIsNull() throws Exception {
		MvcResult Result;

		mvc.perform(post("/login"))
				.andExpect(status().isBadRequest());

		Result = mvc.perform(post("/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\": \"\", \"password\": \"password\"}"))
				.andExpect(status().isOk())
				.andReturn();
		if (!Result.getResponse().getContentAsString().equals(
				gson.toJson(new Response(400, "User is null!")))) throw new Exception("User is not null!");

		Result = mvc.perform(post("/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\": \"user\", \"password\": \"\"}"))
				.andExpect(status().isOk())
				.andReturn();
		if (!Result.getResponse().getContentAsString().equals(
				gson.toJson(new Response(400, "User is null!")))) throw new Exception("User is not null!");
	}

	@Test
	public void login_WrongUsernameOrPassword() throws Exception {
		MvcResult Result = mvc.perform(post("/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\": \""+ UUID.randomUUID() +"\", \"password\": \""+ UUID.randomUUID() +"\"}"))
				.andExpect(status().isOk())
				.andReturn();
		if (!Result.getResponse().getContentAsString().equals(
				gson.toJson(new Response(400, "Wrong username or password!"))))
			throw new Exception("Not wrong username or password!");
	}
}
