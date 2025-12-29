package az.kapitalbank.mb.bff.transfermobile.controllers;

import az.kapitalbank.mb.bff.transfermobile.dtos.requests.CreateCardRequest;
import az.kapitalbank.mb.bff.transfermobile.dtos.responses.CardResponse;
import az.kapitalbank.mb.bff.transfermobile.services.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/card")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    @PostMapping
    public CardResponse create(@RequestBody CreateCardRequest request) {
        return cardService.create(request);
    }

    @GetMapping("/{cardNumber}")
    public CardResponse get(@PathVariable String cardNumber) {
        return cardService.getCard(cardNumber);
    }
}
