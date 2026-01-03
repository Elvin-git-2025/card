package az.kapitalbank.mb.bff.transfermobile.services;

import az.kapitalbank.mb.bff.transfermobile.clients.AccountClient;
import az.kapitalbank.mb.bff.transfermobile.dtos.requests.CreateCardRequest;
import az.kapitalbank.mb.bff.transfermobile.dtos.requests.CreditAccountRequest;
import az.kapitalbank.mb.bff.transfermobile.dtos.requests.DebitAccountRequest;
import az.kapitalbank.mb.bff.transfermobile.dtos.responses.AccountBalanceResponse;
import az.kapitalbank.mb.bff.transfermobile.dtos.responses.CardResponse;
import az.kapitalbank.mb.bff.transfermobile.entities.Card;
import az.kapitalbank.mb.bff.transfermobile.enums.CardStatus;
import az.kapitalbank.mb.bff.transfermobile.exceptions.CardNotFoundException;
import az.kapitalbank.mb.bff.transfermobile.exceptions.CustomerNotFoundException;
import az.kapitalbank.mb.bff.transfermobile.mappers.CardMapper;
import az.kapitalbank.mb.bff.transfermobile.repositories.CardRepository;
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

        AccountBalanceResponse balance =
                accountClient.getBalance(request.getCustomerId());
        if (balance == null || balance.getBalance() == null) {
            throw new CustomerNotFoundException(request.getCustomerId());
        }
        Card card = cardMapper.toEntity(request);
        Card saved = cardRepository.save(card);
        return cardMapper.toResponse(saved);
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

    public boolean existsById(Long cardId) {
        if (cardId == null || cardId <= 0) {
            throw new IllegalArgumentException("Card ID must be positive");
        }
        return cardRepository.existsById(cardId);
    }


    public Long getCardIdByCustomerId(Long customerId) {
        return cardRepository.findByCustomerId(customerId)
                .map(Card::getId)
                .orElseThrow(() -> new RuntimeException(
                        "No active card found for customer id: " + customerId
                ));
    }

    @Transactional
    public void debit(Long cardId, DebitAccountRequest request) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        validateCardStatus(card);
        if (card.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient funds on card: " + cardId);
        }

        card.setBalance(card.getBalance().subtract(request.getAmount()));
        cardRepository.save(card);
    }

    @Transactional
    public void credit(Long cardId, CreditAccountRequest request) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        validateCardStatus(card);
        card.setBalance(card.getBalance().add(request.getAmount()));
        cardRepository.save(card);
    }

    private void validateCardStatus(Card card) {
        if (card.getStatus() == CardStatus.BLOCKED) {
            throw new RuntimeException("Transaction failed: Card is blocked.");
        }
        if (card.getStatus() == CardStatus.EXPIRED) {
            throw new RuntimeException("Transaction failed: Card has expired.");
        }
    }

    @Transactional
    public void blockCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        card.setStatus(CardStatus.BLOCKED);
        cardRepository.save(card);
    }

    @Transactional
    public void unblockCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        // Logic: Perhaps only allow unblocking if it wasn't expired
        card.setStatus(CardStatus.ACTIVE);
        cardRepository.save(card);
    }
}
