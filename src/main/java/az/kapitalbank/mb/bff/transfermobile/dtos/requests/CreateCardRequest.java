package az.kapitalbank.mb.bff.transfermobile.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateCardRequest {

    @NotNull
    Long customerId;

    @NotBlank
    String cardNumber;

    @NotNull
    LocalDate createdAt;
}
