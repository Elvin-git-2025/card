package az.kapitalbank.mb.bff.transfermobile.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAccountRequest {
    private Long customerId;
}