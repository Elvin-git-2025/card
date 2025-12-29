package az.kapitalbank.mb.bff.transfermobile.clients;

import az.kapitalbank.mb.bff.transfermobile.dtos.responses.AccountBalanceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(
        name = "account-service",
        url = "${account.service.url}"
)
public interface AccountClient {
    @GetMapping("/api/v1/accounts/{customerId}/balance")
    AccountBalanceResponse getBalance(
            @PathVariable("customerId") Long customerId
    );
}
