package az.kapitalbank.mb.bff.transfermobile.dtos.responses;

import az.kapitalbank.mb.bff.transfermobile.enums.CardStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CardResponse {
    Long id;
    String cardNumber;
    Long customerId;
    CardStatus status;
    LocalDateTime createdAt;
}