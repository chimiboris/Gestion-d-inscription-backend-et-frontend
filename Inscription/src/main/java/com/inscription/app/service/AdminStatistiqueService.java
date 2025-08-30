package com.inscription.app.service;

import com.inscription.app.repository.CandidatRepository;
import com.inscription.app.service.dto.StatistiqueDTO;
import org.springframework.stereotype.Service;

@Service
public class AdminStatistiqueService {

    private final CandidatRepository candidatRepository;

    public AdminStatistiqueService(CandidatRepository candidatRepository) {
        this.candidatRepository = candidatRepository;
    }

    public StatistiqueDTO collecterStatistiques() {
        long total = candidatRepository.count();
        long hommes = candidatRepository.countBySexe("HOMME");
        long femmes = candidatRepository.countBySexe("FEMME");

        return new StatistiqueDTO(total, hommes, femmes);
    }
}
