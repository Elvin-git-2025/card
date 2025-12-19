package az.kapitalbank.mb.bff.transfermobile.transfer.controllers;

import az.kapitalbank.mb.bff.transfermobile.transfer.dtos.requests.CreateTransferRequest;
import az.kapitalbank.mb.bff.transfermobile.transfer.entities.Transfer;
import az.kapitalbank.mb.bff.transfermobile.transfer.services.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transfer")
@RequiredArgsConstructor
public class TransferController {
    private final TransferService transferService;

    @PostMapping("/create")
    public Transfer createTransfer(CreateTransferRequest request) {
        return transferService.createTransfer(request);
    }


}
