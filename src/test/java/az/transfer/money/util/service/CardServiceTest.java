package az.transfer.money.util.service;

import az.transfer.money.clients.AccountClient;
import az.transfer.money.dtos.requests.CreateCardRequest;
import az.transfer.money.dtos.requests.DebitAccountRequest;
import az.transfer.money.dtos.responses.AccountBalanceResponse;
import az.transfer.money.dtos.responses.CardResponse;
import az.transfer.money.entities.Card;
import az.transfer.money.exceptions.CardNotFoundException;
import az.transfer.money.mappers.CardMapper;
import az.transfer.money.repositories.CardRepository;
import az.transfer.money.services.CardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.any;



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

    @Test
    void getCard_shouldReturnCardResponse_whenCustomerExists() {

        String cardNumber = "1234567890";
        Card card = new Card();
        card.setId(10L);
        CardResponse cardResponse = new CardResponse();
        cardResponse.setId(10L);

        when(cardRepository.findByCardNumber(cardNumber)).thenReturn(Optional.of(card));
        when(cardMapper.toResponse(card)).thenReturn(cardResponse);

        CardResponse result = cardService.getCard(cardNumber);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(10L);

        verify(cardRepository).findByCardNumber(cardNumber);
        verify(cardMapper).toResponse(card);
    }

    @Test
    void getCard_shouldReturnCardResponse_whenCustomerDoesNotExist() {
        String cardNumber = "1234567890";

        when(cardRepository.findByCardNumber(cardNumber)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> cardService.getCard(cardNumber))
                .isInstanceOf(CardNotFoundException.class)
                .hasMessageContaining(cardNumber);
        verify(cardRepository).findByCardNumber(cardNumber);
        verify(cardMapper, never()).toResponse(any());
    }

    @Test
    void getBalance_shouldReturnBalance_whenCardExists() {

        Long cardId = 1L;
        Card card = new Card();
        card.setId(cardId);
        card.setBalance(new BigDecimal("150.00"));

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        AccountBalanceResponse result = cardService.getBalance(cardId);

        assertThat(result).isNotNull();
        assertThat(result.getBalance()).isEqualByComparingTo(new BigDecimal("150.00"));

        verify(cardRepository).findById(cardId);
    }

    @Test
    void getBalance_shouldReturnBalance_whenCardDoesNotExist() {
        Long cardId = 99L;

        when(cardRepository.findById(cardId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cardService.getBalance(cardId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Card not found with id: " + cardId);

        verify(cardRepository).findById(cardId);
    }

    @Test
    void debit_shouldReturnCardResponse_whenCardExists() {
       Long cardId = 1L;
       Card card = new Card();
        card.setId(cardId);
        card.setBalance(new BigDecimal("150.00"));

        DebitAccountRequest request = new DebitAccountRequest();
        request.setAmount(new BigDecimal("50.00"));
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        cardService.debit(cardId, request);

        assertThat(card.getBalance()).isEqualByComparingTo(new BigDecimal("100.00"));
        verify(cardRepository).findById(cardId);
        verify(cardRepository).save(card);
    }

    @Test
    void debit_shouldReturnCardResponse_whenCardDoesNotExist() {
        Long cardId = 99L;
        when(cardRepository.findById(cardId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> cardService.getBalance(cardId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Card not found with id: " + cardId);

        verify(cardRepository).findById(cardId);
    }


}
