package az.kapitalbank.mb.bff.transfermobile.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerResponse {
    Long id;
    String firstName;
    String lastName;
    String pin;
    Integer age;
    LocalDate dateOfBirth;
    LocalDateTime createdAt;
}
