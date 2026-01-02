package az.kapitalbank.mb.bff.transfermobile.repositories;

import az.kapitalbank.mb.bff.transfermobile.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findByCardNumber(String cardNumber);
    Optional<Card> findByCustomerId(Long customerId);
}
