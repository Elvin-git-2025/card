package az.kapitalbank.mb.bff.transfermobile.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CreateCustomerRequest {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotNull
    private Integer age;
    @NotBlank
    private String pin;
    @NotNull
    private LocalDate dateOfBirth;
    @NotNull
    private LocalDateTime createdAt;
}
