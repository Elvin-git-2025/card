package az.transfer.money.services;

import az.transfer.money.clients.AccountClient;
import az.transfer.money.dtos.requests.CreateCardRequest;
import az.transfer.money.dtos.requests.CreditAccountRequest;
import az.transfer.money.dtos.requests.DebitAccountRequest;
import az.transfer.money.dtos.responses.AccountBalanceResponse;
import az.transfer.money.dtos.responses.CardResponse;
import az.transfer.money.entities.Card;
import az.transfer.money.enums.CardStatus;
import az.transfer.money.exceptions.CardNotFoundException;
import az.transfer.money.exceptions.CustomerNotFoundException;
import az.transfer.money.mappers.CardMapper;
import az.transfer.money.repositories.CardRepository;
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

    public AccountBalanceResponse getBalance(Long cardId) {
        return cardRepository.findById(cardId)
                .map(card -> {
                    AccountBalanceResponse response = new AccountBalanceResponse();
                    response.setBalance(card.getBalance());
                    return response;
                })
                .orElseThrow(() -> new RuntimeException("Card not found with id: " + cardId));
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
