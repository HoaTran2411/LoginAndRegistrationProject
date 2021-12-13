package vn.techmaster.user_account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.techmaster.user_account.model.User;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
