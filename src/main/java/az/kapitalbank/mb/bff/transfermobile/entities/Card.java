package az.kapitalbank.mb.bff.transfermobile.entities;

import az.kapitalbank.mb.bff.transfermobile.enums.CardStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.EnumType;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Card {
    @Id
    Long id;

    String cardNumber;

    Long customerId;

    @Enumerated(EnumType.STRING)
    CardStatus status;

    LocalDateTime createdAt;
}
