package me.arttostog.AuthorizationService.Repository;


import me.arttostog.AuthorizationService.Entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User, Long> {
	User findByUsername(String Username);
}
