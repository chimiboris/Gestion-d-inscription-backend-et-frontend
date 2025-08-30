package com.inscription.app.service.dto;

public class StatistiqueDTO {

    private long totalCandidats;
    private long totalHommes;
    private long totalFemmes;

    public StatistiqueDTO(long totalCandidats, long totalHommes, long totalFemmes) {
        this.totalCandidats = totalCandidats;
        this.totalHommes = totalHommes;
        this.totalFemmes = totalFemmes;
    }

    public long getTotalCandidats() {
        return totalCandidats;
    }

    public void setTotalCandidats(long totalCandidats) {
        this.totalCandidats = totalCandidats;
    }

    public long getTotalHommes() {
        return totalHommes;
    }

    public void setTotalHommes(long totalHommes) {
        this.totalHommes = totalHommes;
    }

    public long getTotalFemmes() {
        return totalFemmes;
    }

    public void setTotalFemmes(long totalFemmes) {
        this.totalFemmes = totalFemmes;
    }
}
