package az.kapitalbank.mb.bff.transfermobile.services;

import az.kapitalbank.mb.bff.transfermobile.clients.AccountClient;
import az.kapitalbank.mb.bff.transfermobile.dtos.requests.CreateCardRequest;
import az.kapitalbank.mb.bff.transfermobile.dtos.responses.CardResponse;
import az.kapitalbank.mb.bff.transfermobile.entities.Card;
import az.kapitalbank.mb.bff.transfermobile.exceptions.CustomerNotFoundException;
import az.kapitalbank.mb.bff.transfermobile.mappers.CardMapper;
import az.kapitalbank.mb.bff.transfermobile.repositories.CardRepository;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
