package az.kapitalbank.mb.bff.transfermobile.transfer.services;

import az.kapitalbank.mb.bff.transfermobile.customer.services.CustomerService;
import az.kapitalbank.mb.bff.transfermobile.transfer.dtos.requests.CreateTransferRequest;
import az.kapitalbank.mb.bff.transfermobile.transfer.entities.Transfer;
import az.kapitalbank.mb.bff.transfermobile.transfer.enums.TransferStatus;
import az.kapitalbank.mb.bff.transfermobile.transfer.enums.TransferType;
import az.kapitalbank.mb.bff.transfermobile.transfer.exceptions.InvalidTransferException;
import az.kapitalbank.mb.bff.transfermobile.transfer.mappers.TransferMapper;
import az.kapitalbank.mb.bff.transfermobile.transfer.repositories.TransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class TransferService {

    private final TransferRepository transferRepository;
    private final CustomerService customerService;
    private final TransferMapper transferMapper;

    public Transfer createTransfer(CreateTransferRequest request) {


        Transfer transfer = transferMapper.convertToEntity(request);

        BigDecimal tariff = calculateTariff(request.getType());
        BigDecimal commission = calculateCommission(request.getAmount(), request.getType());


        transfer.setTariff(tariff);
        transfer.setCommission(commission);
        transfer.setStatus(TransferStatus.PENDING);
        transfer.setCreatedAt(LocalDateTime.now());

        return transferRepository.save(transfer);
    }


    private void validate(CreateTransferRequest transfer) {

        if (transfer.getAmount() == null) {
            throw new InvalidTransferException("Amount is required");
        }

        if (transfer.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransferException("Amount must be greater than zero");
        }

        if (transfer.getType() == null) {
            throw new InvalidTransferException("Transfer type is required");
        }

        if (transfer.getCustomerId() == null) {
            throw new InvalidTransferException("Customer id is required");
        }

        if (transfer.getPayee() == null || transfer.getPayee().isBlank()) {
            throw new InvalidTransferException("Payee is required");
        }
    }
    private BigDecimal calculateTariff(TransferType type) {
        return switch (type) {
            case CARD_TO_CARD -> new BigDecimal("1.00");
            case ACCOUNT_TO_CARD -> new BigDecimal("0.50");
        };
    }

    private BigDecimal calculateCommission(BigDecimal amount, TransferType type) {
        BigDecimal rate = switch (type) {
            case CARD_TO_CARD -> new BigDecimal("0.02");
            case ACCOUNT_TO_CARD -> new BigDecimal("0.01");
        };
        return amount.multiply(rate);
    }
}
