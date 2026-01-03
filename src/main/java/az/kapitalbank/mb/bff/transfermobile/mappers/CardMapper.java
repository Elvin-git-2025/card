package az.kapitalbank.mb.bff.transfermobile.mappers;

import az.kapitalbank.mb.bff.transfermobile.dtos.requests.CreateCardRequest;
import az.kapitalbank.mb.bff.transfermobile.dtos.responses.CardResponse;
import az.kapitalbank.mb.bff.transfermobile.entities.Card;
import az.kapitalbank.mb.bff.transfermobile.enums.CardStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring",
        imports = {CardStatus.class, LocalDateTime.class}
)
public interface CardMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", expression = "java(CardStatus.ACTIVE)")
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    Card toEntity(CreateCardRequest request);

    CardResponse toResponse(Card card);

    List<CardResponse> toResponseList(List<Card> cards);
}
