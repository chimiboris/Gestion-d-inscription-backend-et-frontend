package com.inscription.app.service.mapper;

import com.inscription.app.domain.Candidat;
import com.inscription.app.domain.User;
import com.inscription.app.service.dto.CandidatDTO;
import com.inscription.app.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Candidat} and its DTO {@link CandidatDTO}.
 */
@Mapper(componentModel = "spring")
public interface CandidatMapper extends EntityMapper<CandidatDTO, Candidat> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    CandidatDTO toDto(Candidat s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
