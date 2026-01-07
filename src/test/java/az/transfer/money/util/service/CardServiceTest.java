package az.transfer.money.util.service;

import az.transfer.money.clients.AccountClient;
import az.transfer.money.dtos.requests.CreateCardRequest;
import az.transfer.money.dtos.requests.CreditAccountRequest;
import az.transfer.money.dtos.requests.DebitAccountRequest;
import az.transfer.money.dtos.responses.AccountBalanceResponse;
import az.transfer.money.dtos.responses.CardResponse;
import az.transfer.money.entities.Card;
import az.transfer.money.enums.CardStatus;
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
import java.util.List;
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

    @Test
    void getListOfCards_shouldReturnListOfCards() {

        Card card1 = new Card();
        Card card2 = new Card();

        CardResponse response1 = new CardResponse();
        CardResponse response2 = new CardResponse();

        List<Card> cards = List.of(card1, card2);
        List<CardResponse> responses = List.of(response1, response2);

        when(cardRepository.findAll()).thenReturn(cards);
        when(cardMapper.toResponseList(cards)).thenReturn(responses);

        List<CardResponse> result=cardService.getCards();
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).isEqualTo(responses);

        verify(cardRepository).findAll();
        verify(cardMapper).toResponseList(cards);
    }

    @Test
    void getListOfCards_shouldReturnListOfCards_whenCardDoesNotExist() {
        List<Card> cards = List.of();
        List<CardResponse> responses = List.of();

        when(cardRepository.findAll()).thenReturn(cards);
        when(cardMapper.toResponseList(cards)).thenReturn(responses);

        List<CardResponse> result = cardService.getCards();

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(0);

        verify(cardRepository).findAll();
        verify(cardMapper).toResponseList(cards);
    }

    @Test
    void credit_shouldReturnCardResponse_whenCardExists() {
        Long cardId = 1L;
        Card card = new Card();
        card.setId(cardId);
        card.setBalance(new BigDecimal("150.00"));

        CreditAccountRequest request = new CreditAccountRequest();
        request.setAmount(new BigDecimal("50.00"));

        when(cardRepository.findById(card.getId())).thenReturn(Optional.of(card));
        cardService.credit(cardId, request);

        assertThat(card.getBalance()).isEqualByComparingTo(new BigDecimal("200.00"));
        verify(cardRepository).findById(cardId);
        verify(cardRepository).save(card);
    }

    @Test
    void credit_shouldThrowException_whenCardDoesNotExist() {
        Long cardId = 99L;
        when(cardRepository.findById(cardId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> cardService.credit(cardId, new CreditAccountRequest()))
        .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Card not found");
        verify(cardRepository).findById(cardId);
    }

    @Test
    void block_shouldReturnCardResponse_whenCardExists() {
        Long cardId = 1L;
        Card card = new Card();
        card.setId(cardId);
        card.setStatus(CardStatus.ACTIVE);

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        cardService.blockCard(cardId);

        assertThat(card.getStatus()).isEqualTo(CardStatus.BLOCKED);
        verify(cardRepository).findById(cardId);
        verify(cardRepository).save(card);
    }

    @Test
    void block_shouldThrowException_whenCardDoesNotExist() {
        Long cardId = 99L;
        when(cardRepository.findById(cardId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> cardService.blockCard(cardId))
        .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Card not found");
        verify(cardRepository).findById(cardId);
    }

    @Test
    void unblock_shouldReturnCardResponse_whenCardExists() {
        Long cardId = 1L;
        Card card = new Card();
        card.setId(cardId);
        card.setStatus(CardStatus.BLOCKED);
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        cardService.unblockCard(cardId);
        assertThat(card.getStatus()).isEqualTo(CardStatus.ACTIVE);
        verify(cardRepository).findById(cardId);
        verify(cardRepository).save(card);
    }
    @Test
    void unblock_shouldThrowException_whenCardDoesNotExist() {
        Long cardId = 99L;
        when(cardRepository.findById(cardId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> cardService.unblockCard(cardId))
        .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Card not found");
        verify(cardRepository).findById(cardId);
    }
}
