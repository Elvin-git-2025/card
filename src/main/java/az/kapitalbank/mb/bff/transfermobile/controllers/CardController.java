package az.kapitalbank.mb.bff.transfermobile.controllers;

import az.kapitalbank.mb.bff.transfermobile.dtos.requests.CreateCardRequest;
import az.kapitalbank.mb.bff.transfermobile.dtos.requests.CreditAccountRequest;
import az.kapitalbank.mb.bff.transfermobile.dtos.requests.DebitAccountRequest;
import az.kapitalbank.mb.bff.transfermobile.dtos.responses.CardResponse;
import az.kapitalbank.mb.bff.transfermobile.services.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import  org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import java.util.List;


@RestController
@RequestMapping("/api/v1/card")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    @PostMapping
    public CardResponse create(@Valid  @RequestBody CreateCardRequest request) {
        return cardService.create(request);
    }

    @GetMapping("/{cardNumber}")
    public CardResponse get(@PathVariable String cardNumber) {
        return cardService.getCard(cardNumber);
    }

    @GetMapping
    public List<CardResponse> getCards() {
        return cardService.getCards();
    }

    @GetMapping("/{cardId}/exists")
    public boolean exists(@PathVariable Long cardId) {
        return cardService.existsById(cardId);
    }

    @GetMapping("/customer/{customerId}")
    public Long getCardIdByCustomerId(@PathVariable Long customerId) {
        return cardService.getCardIdByCustomerId(customerId);
    }

    @PostMapping("/{cardId}/debit")
    public void debit(
            @PathVariable Long cardId,
            @RequestBody DebitAccountRequest request) {
        cardService.debit(cardId, request);
    }


    @PostMapping("/{cardId}/credit")
    public void credit(
            @PathVariable Long cardId,
            @RequestBody CreditAccountRequest request) {
        cardService.credit(cardId, request);
    }

    @PatchMapping("/{cardId}/block")
    public void blockCard(@PathVariable Long cardId) {
        cardService.blockCard(cardId);
    }

    @PatchMapping("/{cardId}/unblock")
    public void unblockCard(@PathVariable Long cardId) {
        cardService.unblockCard(cardId);
    }
}
