package az.kapitalbank.mb.bff.transfermobile.repositories;

import az.kapitalbank.mb.bff.transfermobile.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {

}
