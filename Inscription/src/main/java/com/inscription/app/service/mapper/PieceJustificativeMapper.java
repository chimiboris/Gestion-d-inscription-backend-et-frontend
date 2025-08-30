package com.inscription.app.service.mapper;

import com.inscription.app.domain.Candidat;
import com.inscription.app.domain.PieceJustificative;
import com.inscription.app.service.dto.CandidatDTO;
import com.inscription.app.service.dto.PieceJustificativeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PieceJustificative} and its DTO {@link PieceJustificativeDTO}.
 */
@Mapper(componentModel = "spring")
public interface PieceJustificativeMapper extends EntityMapper<PieceJustificativeDTO, PieceJustificative> {
    @Mapping(target = "candidat", source = "candidat", qualifiedByName = "candidatId")
    PieceJustificativeDTO toDto(PieceJustificative s);

    @Named("candidatId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CandidatDTO toDtoCandidatId(Candidat candidat);
}
