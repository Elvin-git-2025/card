package az.kapitalbank.mb.bff.transfermobile.repositories;

import az.kapitalbank.mb.bff.transfermobile.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByCustomerId(Long customerId);
}