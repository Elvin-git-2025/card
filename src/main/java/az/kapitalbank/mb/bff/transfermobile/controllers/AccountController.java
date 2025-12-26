package az.kapitalbank.mb.bff.transfermobile.controllers;

import az.kapitalbank.mb.bff.transfermobile.dtos.requests.CreateAccountRequest;
import az.kapitalbank.mb.bff.transfermobile.dtos.requests.CreditAccountRequest;
import az.kapitalbank.mb.bff.transfermobile.dtos.requests.DebitAccountRequest;
import az.kapitalbank.mb.bff.transfermobile.dtos.responses.AccountBalanceResponse;
import az.kapitalbank.mb.bff.transfermobile.entities.Account;
import az.kapitalbank.mb.bff.transfermobile.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;


    @PostMapping
    public ResponseEntity<Account> createAccount(
            @RequestBody CreateAccountRequest request) {

        Account account = accountService.createBalance(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @GetMapping("/{customerId}/balance")
    public AccountBalanceResponse getBalance(
            @PathVariable Long customerId
    ) {
        return new AccountBalanceResponse(
                accountService.getBalance(customerId)
        );
    }

    @PostMapping("/{customerId}/debit")
    public void debit(
            @PathVariable Long customerId,
            @RequestBody DebitAccountRequest request
    ) {
        accountService.debit(customerId, request.getAmount());
    }

    @PostMapping("/{customerId}/credit")
    public void credit(
            @PathVariable Long customerId,
            @RequestBody CreditAccountRequest request
    ) {
        accountService.credit(customerId, request.getAmount());
    }
}
