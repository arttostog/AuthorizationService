package me.arttostog.AuthorizationService.User.Repository;

import me.arttostog.AuthorizationService.User.User;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UserRepo extends CrudRepository<User, UUID> {
	User findByUsername(String Username);

	User findByMail(String Mail);

	User findByAccessToken(Long accessToken);
}
