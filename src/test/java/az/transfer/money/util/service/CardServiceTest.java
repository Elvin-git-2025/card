package az.transfer.money.util.service;

import az.transfer.money.clients.AccountClient;
import az.transfer.money.dtos.requests.CreateCardRequest;
import az.transfer.money.dtos.responses.AccountBalanceResponse;
import az.transfer.money.dtos.responses.CardResponse;
import az.transfer.money.entities.Card;
import az.transfer.money.mappers.CardMapper;
import az.transfer.money.repositories.CardRepository;
import az.transfer.money.services.CardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private AccountClient accountClient;

    @Mock
    private CardMapper cardMapper;

    @InjectMocks
    private CardService cardService;

    @Test
    void create_shouldReturnCardResponse_whenCustomerExists() {

        CreateCardRequest request = new CreateCardRequest();
        request.setCustomerId(1L);

        AccountBalanceResponse balanceResponse = new AccountBalanceResponse();
        balanceResponse.setBalance(BigDecimal.valueOf(100));
        when(accountClient.getBalance(1L)).thenReturn(balanceResponse);

        Card cardEntity = new Card();
        when(cardMapper.toEntity(request)).thenReturn(cardEntity);

        Card savedCard = new Card();
        savedCard.setId(10L);
        when(cardRepository.save(cardEntity)).thenReturn(savedCard);

        CardResponse cardResponse = new CardResponse();
        cardResponse.setId(10L);
        when(cardMapper.toResponse(savedCard)).thenReturn(cardResponse);

        CardResponse result = cardService.create(request);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(10L);

        verify(accountClient).getBalance(1L);
        verify(cardMapper).toEntity(request);
        verify(cardRepository).save(cardEntity);
        verify(cardMapper).toResponse(savedCard);
    }

}
