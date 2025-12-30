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


    @Column(nullable = false, unique = true)
    String cardNumber;

    @Column(nullable = false)
    Long customerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    CardStatus status;

    @Column(nullable = false, updatable = false)
    LocalDateTime createdAt;
}
