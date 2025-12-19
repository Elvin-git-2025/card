package az.kapitalbank.mb.bff.transfermobile.transfer.dtos.requests;

import az.kapitalbank.mb.bff.transfermobile.transfer.enums.TransferType;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateTransferRequest {
    BigDecimal amount;
    Long customerId;
    TransferType type;
    String payee;
}
