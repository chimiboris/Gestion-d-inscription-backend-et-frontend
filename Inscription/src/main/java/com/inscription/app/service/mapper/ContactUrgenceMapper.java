package com.inscription.app.service.mapper;

import com.inscription.app.domain.Candidat;
import com.inscription.app.domain.ContactUrgence;
import com.inscription.app.service.dto.CandidatDTO;
import com.inscription.app.service.dto.ContactUrgenceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContactUrgence} and its DTO {@link ContactUrgenceDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContactUrgenceMapper extends EntityMapper<ContactUrgenceDTO, ContactUrgence> {
    @Mapping(target = "candidat", source = "candidat", qualifiedByName = "candidatId")
    ContactUrgenceDTO toDto(ContactUrgence s);

    @Named("candidatId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CandidatDTO toDtoCandidatId(Candidat candidat);
}
