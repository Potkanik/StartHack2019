package ch.start.hack.service.mapper;

import ch.start.hack.domain.*;
import ch.start.hack.service.dto.HistoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity History and its DTO HistoryDTO.
 */
@Mapper(componentModel = "spring", uses = {CupMapper.class})
public interface HistoryMapper extends EntityMapper<HistoryDTO, History> {

    @Mapping(source = "kup.id", target = "kupId")
    HistoryDTO toDto(History history);

    @Mapping(source = "kupId", target = "kup")
    History toEntity(HistoryDTO historyDTO);

    default History fromId(Long id) {
        if (id == null) {
            return null;
        }
        History history = new History();
        history.setId(id);
        return history;
    }
}
