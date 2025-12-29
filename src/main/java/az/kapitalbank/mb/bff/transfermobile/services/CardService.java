package az.kapitalbank.mb.bff.transfermobile.services;

import az.kapitalbank.mb.bff.transfermobile.clients.AccountClient;
import az.kapitalbank.mb.bff.transfermobile.dtos.requests.CreateCardRequest;
import az.kapitalbank.mb.bff.transfermobile.dtos.responses.CardResponse;
import az.kapitalbank.mb.bff.transfermobile.entities.Card;
import az.kapitalbank.mb.bff.transfermobile.enums.CardStatus;
import az.kapitalbank.mb.bff.transfermobile.exceptions.CardNotFoundException;
import az.kapitalbank.mb.bff.transfermobile.exceptions.CustomerNotFoundException;
import az.kapitalbank.mb.bff.transfermobile.mappers.CardMapper;
import az.kapitalbank.mb.bff.transfermobile.repositories.CardRepository;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final AccountClient accountClient;
    private final CardMapper cardMapper;

    @Transactional
    public CardResponse create(CreateCardRequest request) {
        try {
            accountClient.getBalance(request.getCustomerId());
        } catch (FeignException.NotFound ex) {
            throw new CustomerNotFoundException(request.getCustomerId());
        } catch (FeignException ex) {
            throw new RuntimeException("Account service unavailable", ex);
        }

        Card card = cardRepository.save(cardMapper.toEntity(request));
        return cardMapper.toResponse(card);
    }

    public Card validateActiveCard(String cardNumber) {
        Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        if (card.getStatus() != CardStatus.ACTIVE) {
            throw new RuntimeException("Card is not active");
        }

        return card;
    }

    public CardResponse getCard(String cardNumber) {
        Card card = cardRepository
                .findByCardNumber(cardNumber)
                .orElseThrow(() ->
                        new CardNotFoundException(cardNumber)
                );

        return cardMapper.toResponse(card);
    }

    public List<CardResponse> getCards() {
        List<Card> cards = cardRepository.findAll();
        return cardMapper.toResponseList(cards);
    }
}
