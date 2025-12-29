package az.kapitalbank.mb.bff.transfermobile.dtos.responses;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountBalanceResponse {
    private BigDecimal balance;
}