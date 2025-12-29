package az.kapitalbank.mb.bff.transfermobile.mappers;

import az.kapitalbank.mb.bff.transfermobile.dtos.requests.CreateCardRequest;
import az.kapitalbank.mb.bff.transfermobile.dtos.responses.CardResponse;
import az.kapitalbank.mb.bff.transfermobile.entities.Card;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardMapper {
    Card toEntity(CreateCardRequest request);

    CardResponse toResponse(Card card);
}
