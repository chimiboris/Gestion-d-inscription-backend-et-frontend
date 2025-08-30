package com.inscription.app.service.mapper;

import com.inscription.app.domain.Candidat;
import com.inscription.app.domain.Parcours;
import com.inscription.app.service.dto.CandidatDTO;
import com.inscription.app.service.dto.ParcoursDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Parcours} and its DTO {@link ParcoursDTO}.
 */
@Mapper(componentModel = "spring")
public interface ParcoursMapper extends EntityMapper<ParcoursDTO, Parcours> {
    @Mapping(target = "candidat", source = "candidat", qualifiedByName = "candidatId")
    ParcoursDTO toDto(Parcours s);

    @Named("candidatId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CandidatDTO toDtoCandidatId(Candidat candidat);
}
