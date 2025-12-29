package az.kapitalbank.mb.bff.transfermobile.services;

import az.kapitalbank.mb.bff.transfermobile.clients.AccountClient;
import az.kapitalbank.mb.bff.transfermobile.dtos.requests.CreateCardRequest;
import az.kapitalbank.mb.bff.transfermobile.dtos.responses.CardResponse;
import az.kapitalbank.mb.bff.transfermobile.entities.Card;
import az.kapitalbank.mb.bff.transfermobile.mappers.CardMapper;
import az.kapitalbank.mb.bff.transfermobile.repositories.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final AccountClient accountClient;
    private final CardMapper cardMapper;

    public CardResponse create(CreateCardRequest request) {
        accountClient.getBalance(request.getCustomerId());
        Card card = cardRepository.save(cardMapper.toEntity(request));
        return cardMapper.toResponse(card);
    }
}
