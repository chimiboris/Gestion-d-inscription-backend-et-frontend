package com.inscription.app.service.mapper;

import com.inscription.app.domain.Candidat;
import com.inscription.app.domain.RendezVous;
import com.inscription.app.service.dto.CandidatDTO;
import com.inscription.app.service.dto.RendezVousDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RendezVous} and its DTO {@link RendezVousDTO}.
 */
@Mapper(componentModel = "spring")
public interface RendezVousMapper extends EntityMapper<RendezVousDTO, RendezVous> {
    @Mapping(target = "candidat", source = "candidat", qualifiedByName = "candidatId")
    RendezVousDTO toDto(RendezVous s);

    @Named("candidatId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CandidatDTO toDtoCandidatId(Candidat candidat);
}
