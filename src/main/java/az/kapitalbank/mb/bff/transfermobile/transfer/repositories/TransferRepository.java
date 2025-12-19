package az.kapitalbank.mb.bff.transfermobile.transfer.repositories;


import az.kapitalbank.mb.bff.transfermobile.transfer.entities.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
}
