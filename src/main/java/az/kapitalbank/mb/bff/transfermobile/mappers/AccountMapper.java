package az.kapitalbank.mb.bff.transfermobile.mappers;

import az.kapitalbank.mb.bff.transfermobile.dtos.requests.CreateAccountRequest;
import az.kapitalbank.mb.bff.transfermobile.entities.Account;
import org.mapstruct.Mapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Mapper(componentModel = "spring", imports = {BigDecimal.class,LocalDateTime.class})
public interface AccountMapper {
    default Account toEntity(CreateAccountRequest request) {
        if (request == null) {
            return null;
        }

        Account account = new Account();
        account.setCustomerId(request.getCustomerId());
        account.setBalance(BigDecimal.ZERO);
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());
        return account;
    }
}
