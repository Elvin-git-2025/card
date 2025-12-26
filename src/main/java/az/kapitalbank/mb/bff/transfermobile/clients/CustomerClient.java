package az.kapitalbank.mb.bff.transfermobile.clients;

import az.kapitalbank.mb.bff.transfermobile.dtos.CustomerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "customer-service",
        url = "${customer.service.url}"
)
public interface CustomerClient {

    @GetMapping("/customers/{id}")
    CustomerResponse getCustomerById(@PathVariable("id") Long customerId);
}