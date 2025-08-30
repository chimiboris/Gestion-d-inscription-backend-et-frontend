package com.inscription.app.service.mapper;

import com.inscription.app.domain.Candidat;
import com.inscription.app.domain.Dossier;
import com.inscription.app.domain.User;
import com.inscription.app.service.dto.CandidatDTO;
import com.inscription.app.service.dto.DossierDTO;
import com.inscription.app.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Dossier} and its DTO {@link DossierDTO}.
 */
@Mapper(componentModel = "spring")
public interface DossierMapper extends EntityMapper<DossierDTO, Dossier> {
    @Mapping(target = "candidat", source = "candidat", qualifiedByName = "candidatId")
    @Mapping(target = "agent", source = "agent", qualifiedByName = "userLogin")
    DossierDTO toDto(Dossier s);

    @Named("candidatId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CandidatDTO toDtoCandidatId(Candidat candidat);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
