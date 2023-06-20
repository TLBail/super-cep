package com.example.super_cep.controller;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.super_cep.model.Calendrier.Calendrier;
import com.example.super_cep.model.Chauffage;
import com.example.super_cep.model.Climatisation;
import com.example.super_cep.model.ECS;
import com.example.super_cep.model.Enveloppe.Zone;
import com.example.super_cep.model.Enveloppe.ZoneElement;
import com.example.super_cep.model.Releve;
import com.example.super_cep.model.Ventilation;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class ReleveViewModel extends ViewModel {

    private final MutableLiveData<Releve> releve = new MutableLiveData<>();

    public void setReleve(Releve releve){
        this.releve.setValue(releve);
    }

    public LiveData<Releve> getReleve(){
        return releve;
    }

    public void addZone(Zone zone){
        releve.getValue().addZone(zone);
        forceUpdateReleve();
    }

    public void forceUpdateReleve(){
        releve.setValue(releve.getValue());
    }

    public void deleteZone(Zone zone) {
        if(!releve.getValue().zones.containsKey(zone.nom)){
            throw new IllegalArgumentException("La zone n'existe pas");
        }
        Releve releve = this.releve.getValue();
        releve.zones.remove(zone.nom);
        for (Ventilation ventilation : releve.ventilations.values()) {
            ventilation.zones.remove(zone.nom);
        }
        for (Climatisation climatisation : releve.climatisations.values()) {
            climatisation.zones.remove(zone.nom);
        }
        for (Chauffage chauffage : releve.chauffages.values()) {
            chauffage.zones.remove(zone.nom);
        }
        setReleve(releve);
    }

    public void setNomBatiment(String nomBatiment){
        releve.getValue().nomBatiment = nomBatiment;
        forceUpdateReleve();
    }

    public void setDateDeConstruction(Calendar dateDeConstruction){
        releve.getValue().dateDeConstruction = dateDeConstruction;
        forceUpdateReleve();
    }

    public void setDateDeDerniereRenovation(Calendar dateDeDerniereRenovation){
        releve.getValue().dateDeDerniereRenovation = dateDeDerniereRenovation;
        forceUpdateReleve();
    }

    public void setSurfaceTotaleChauffe(float surfaceTotaleChauffe){
        releve.getValue().surfaceTotaleChauffe = surfaceTotaleChauffe;
        forceUpdateReleve();
    }

    public void setDescription(String description){
        releve.getValue().description = description;
        forceUpdateReleve();
    }

    public void setAdresse(String adresse){
        releve.getValue().adresse = adresse;
        forceUpdateReleve();
    }

    public void editZoneElement(String oldNameZoneElement,String nomZone, ZoneElement zoneElement){
        getReleve().getValue().getZone(nomZone).removeZoneElement(oldNameZoneElement);
        getReleve().getValue().getZone(nomZone).addZoneElement(zoneElement);
        forceUpdateReleve();
    }
    public void removeZoneElement(String nomZone, String nomZoneElement){
        getReleve().getValue().getZone(nomZone).removeZoneElement(nomZoneElement);
        forceUpdateReleve();
    }

    public void addCalendrier(Calendrier calendrier){
        releve.getValue().addCalendrier(calendrier);
        forceUpdateReleve();
    }

    public void moveZoneElement(String nomZoneElement, String nomPreviousZone, String nomNewZone) {
        Releve releve = this.releve.getValue();
        Zone previousZone = releve.getZone(nomPreviousZone);
        Zone newZone = releve.getZone(nomNewZone);
        ZoneElement zoneElement = previousZone.getZoneElement(nomZoneElement);

        if(newZone.getZoneElement(nomZoneElement) != null){
            throw new IllegalArgumentException("Un élément de la zone porte déjà ce nom");
        }

        previousZone.removeZoneElement(nomZoneElement);
        newZone.addZoneElement(zoneElement);


        forceUpdateReleve();

    }

    public void updateCalendrier(String oldName, Calendrier calendrier) {
        Releve releve = this.releve.getValue();
        if(!calendrier.nom.equals(oldName) && releve.calendriers.containsKey(calendrier.nom)){
            throw new IllegalArgumentException("Un calendrier porte déjà ce nom : " + calendrier.nom);
        }
        releve.calendriers.remove(oldName);
        releve.addCalendrier(calendrier);
        forceUpdateReleve();
    }


    public void supprimerCalendrier(String nomCalendrier) {
        releve.getValue().calendriers.remove(nomCalendrier);
        forceUpdateReleve();
    }

    public void removeChauffage(String nomChauffage) {
        releve.getValue().chauffages.remove(nomChauffage);
        forceUpdateReleve();
    }

    public void addChauffage(Chauffage chauffage) {
        if(releve.getValue().chauffages.containsKey(chauffage.nom)){
            throw new IllegalArgumentException("Un chauffage porte déjà ce nom : " + chauffage.nom);
        }
        releve.getValue().chauffages.put(chauffage.nom, chauffage);
        forceUpdateReleve();
    }

    public void editChauffage(String oldName, Chauffage chauffage) {

        if(!chauffage.nom.equals(oldName) && releve.getValue().chauffages.containsKey(chauffage.nom)){
            throw new IllegalArgumentException("Un calendrier porte déjà ce nom : " + chauffage.nom);
        }
        releve.getValue().chauffages.remove(oldName);
        releve.getValue().chauffages.put(chauffage.nom, chauffage);
        forceUpdateReleve();
    }

    public void removeClimatisation(String nomClimatisation) {
        releve.getValue().climatisations.remove(nomClimatisation);
        forceUpdateReleve();
    }

    public void addClimatisation(Climatisation climatisationFromViews) {
        if(releve.getValue().climatisations.containsKey(climatisationFromViews.nom)){
            throw new IllegalArgumentException("Une climatisation porte déjà ce nom : " + climatisationFromViews.nom);
        }
        releve.getValue().climatisations.put(climatisationFromViews.nom, climatisationFromViews);
        forceUpdateReleve();
    }

    public void editClimatisation(String oldName, Climatisation climatisation) {
        if(!climatisation.nom.equals(oldName) && releve.getValue().climatisations.containsKey(climatisation.nom)){
            throw new IllegalArgumentException("Un calendrier porte déjà ce nom : " + climatisation.nom);
        }
        releve.getValue().climatisations.remove(oldName);
        releve.getValue().climatisations.put(climatisation.nom, climatisation);
        forceUpdateReleve();
    }

    public void removeVentilation(String ventilation) {
        releve.getValue().ventilations.remove(ventilation);
        forceUpdateReleve();
    }

    public void addVentilation(Ventilation ventilationFromViews) {
        if(releve.getValue().ventilations.containsKey(ventilationFromViews.nom)){
            throw new IllegalArgumentException("Une ventilation porte déjà ce nom : " + ventilationFromViews.nom);
        }
        releve.getValue().ventilations.put(ventilationFromViews.nom, ventilationFromViews);
        forceUpdateReleve();
    }

    public void editVentilation(String oldName, Ventilation ventilation) {
        if(!ventilation.nom.equals(oldName) && releve.getValue().ventilations.containsKey(ventilation.nom)){
            throw new IllegalArgumentException("Une ventilation porte déjà ce nom : " + ventilation.nom);
        }
        releve.getValue().ventilations.remove(oldName);
        releve.getValue().ventilations.put(ventilation.nom, ventilation);
        forceUpdateReleve();
    }

    public void removeECS(String nomECS) {
        releve.getValue().ecs.remove(nomECS);
        forceUpdateReleve();
    }

    public void addECS(ECS ecsFromViews) {
        if(releve.getValue().ecs.containsKey(ecsFromViews.nom)){
            throw new IllegalArgumentException("Un ECS porte déjà ce nom : " + ecsFromViews.nom);
        }
        releve.getValue().ecs.put(ecsFromViews.nom, ecsFromViews);
        forceUpdateReleve();
    }

    public void editECS(String nomECS, ECS ecsFromViews) {
        if(!ecsFromViews.nom.equals(nomECS) && releve.getValue().ecs.containsKey(ecsFromViews.nom)){
            throw new IllegalArgumentException("Un ECS porte déjà ce nom : " + ecsFromViews.nom);
        }
        releve.getValue().ecs.remove(nomECS);
        releve.getValue().ecs.put(ecsFromViews.nom, ecsFromViews);
        forceUpdateReleve();
    }
}
