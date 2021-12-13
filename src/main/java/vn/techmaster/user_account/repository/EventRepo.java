package vn.techmaster.user_account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.techmaster.user_account.model.Event;

public interface EventRepo extends JpaRepository<Event, Long> {
}
