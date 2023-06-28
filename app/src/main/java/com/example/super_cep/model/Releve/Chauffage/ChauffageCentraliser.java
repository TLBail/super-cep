package com.example.super_cep.model.Releve.Chauffage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ChauffageCentraliser extends Chauffage{


    public List<String> zones;

    @JsonCreator
    public ChauffageCentraliser(@JsonProperty("nom") String nom,
                                  @JsonProperty("type") String type,
                                  @JsonProperty("puissance") float puissance,
                                  @JsonProperty("quantite") int quantite,
                                  @JsonProperty("marque") String marque,
                                  @JsonProperty("modele") String modele,
                                @JsonProperty("zones") List<String> zones,
                                  @JsonProperty("categorie") CategorieChauffage categorie,
                                  @JsonProperty("regulation") String regulation,
                                  @JsonProperty("images") List<String> images,
                                  @JsonProperty("aVerifier") boolean aVerifier,
                                  @JsonProperty("note") String note

    ) {
        super(nom, type, puissance, quantite, marque, modele, categorie, regulation, images, aVerifier, note);
        this.zones = zones;
    }

    @Override
    public String getZoneText() {
        StringBuilder builder = new StringBuilder("  ");
        for (String zone : zones) {
            builder.append(zone).append(", ");
        }
        builder.delete(Math.max(0,builder.length() - 2) , builder.length());
        return builder.toString();
    }
}