package ch.start.hack.service.mapper;

import ch.start.hack.domain.*;
import ch.start.hack.service.dto.CupDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Cup and its DTO CupDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CupMapper extends EntityMapper<CupDTO, Cup> {

    @Mapping(source = "userCup.id", target = "userCupId")
    @Mapping(source = "userCup.login", target = "userCupLogin")
    CupDTO toDto(Cup cup);

    @Mapping(target = "histories", ignore = false)
    @Mapping(source = "userCupId", target = "userCup")
    Cup toEntity(CupDTO cupDTO);

    default Cup fromId(Long id) {
        if (id == null) {
            return null;
        }
        Cup cup = new Cup();
        cup.setId(id);
        return cup;
    }
}
