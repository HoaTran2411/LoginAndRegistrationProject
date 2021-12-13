package vn.techmaster.user_account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.techmaster.user_account.model.Role;

public interface RoleRepo extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
