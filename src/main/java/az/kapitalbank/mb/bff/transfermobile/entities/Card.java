package az.kapitalbank.mb.bff.transfermobile.entities;

import az.kapitalbank.mb.bff.transfermobile.enums.CardStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String cardNumber;

    Long customerId;

    @Enumerated(EnumType.STRING)
    CardStatus status;

    LocalDateTime createdAt;
}
