package az.kapitalbank.mb.bff.transfermobile.services;

import az.kapitalbank.mb.bff.transfermobile.clients.AccountClient;
import az.kapitalbank.mb.bff.transfermobile.dtos.requests.CreateCardRequest;
import az.kapitalbank.mb.bff.transfermobile.dtos.responses.AccountBalanceResponse;
import az.kapitalbank.mb.bff.transfermobile.dtos.responses.CardResponse;
import az.kapitalbank.mb.bff.transfermobile.entities.Card;
import az.kapitalbank.mb.bff.transfermobile.exceptions.CardNotFoundException;
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
            throw new RuntimeException("Customer has no active account");
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
}
