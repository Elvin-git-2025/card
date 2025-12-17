package az.kapitalbank.mb.bff.transfermobile.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String firstName;
    String lastName;
    @Transient
    Integer age;
    String pin;
    LocalDate dateOfBirth;
    LocalDateTime createdAt;

    public Integer getAge() {
        if (dateOfBirth == null) return 0;
        return java.time.Period.between(dateOfBirth, java.time.LocalDate.now()).getYears();
    }

}
