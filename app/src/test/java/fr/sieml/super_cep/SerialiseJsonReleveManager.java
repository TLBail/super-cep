package fr.sieml.super_cep;

import fr.sieml.super_cep.model.Releve.ApprovionnementEnergetique.ApprovisionnementEnergetique;
import fr.sieml.super_cep.model.Releve.Calendrier.Calendrier;
import fr.sieml.super_cep.model.Releve.Calendrier.CalendrierDate;
import fr.sieml.super_cep.model.Releve.Calendrier.ChaufferOccuper;
import fr.sieml.super_cep.model.Releve.Chauffage.CategorieChauffage;
import fr.sieml.super_cep.model.Releve.Chauffage.ChauffageCentraliser;
import fr.sieml.super_cep.model.Releve.Chauffage.ChauffageDecentraliser;
import fr.sieml.super_cep.model.Releve.Enveloppe.Mur;
import fr.sieml.super_cep.model.Releve.Remarque;
import fr.sieml.super_cep.model.Releve.Zone;
import fr.sieml.super_cep.model.Export.JsonReleveManager;
import fr.sieml.super_cep.model.Releve.Releve;

import org.junit.Test;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class SerialiseJsonReleveManager {


    @Test
    public void serialiseJsonReleve() {
        Releve releve = createTestReleve();
        String jsonReleve = JsonReleveManager.serialize(releve);
        System.out.println("jsonReleve = " + jsonReleve);
        Releve deserializedReleve = JsonReleveManager.deserialize(jsonReleve);
        System.out.println("deserializedReleve = " + deserializedReleve);

    }


    public Releve createTestReleve() {
        Releve releve = new Releve();

        releve.nomBatiment = "Batiment Test";
        releve.dateDeConstruction = Calendar.getInstance();
        releve.dateDeConstruction.set(2000, Calendar.JANUARY, 1);
        releve.dateDeDerniereRenovation = Calendar.getInstance();
        releve.dateDeDerniereRenovation.set(2015, Calendar.JANUARY, 1);
        releve.surfaceTotaleChauffe = 5000.0f;
        releve.adresse = "123 Rue de Test, Ville Test, 10000";
        releve.localisation = new double[]{12.0, 12.0};

        Zone superZone = new Zone("Super Zone");
        Mur mur = new Mur("Mur 1",
                "type de mur",
                "type de mise en oeuvre ",
                "type d'isolation",
                "niveau isolation",
                2,
                true,
                "note",
                null
                );
        superZone.addZoneElement(mur);
        releve.zones.put(superZone.nom, superZone);

        Zone zone1 = new Zone("Zone 1");
        releve.zones.put(zone1.nom, zone1);

        Zone zone2 = new Zone("Zone 2");
        releve.zones.put(zone2.nom, zone2);

        releve.chauffages.put("chauffage 1", new ChauffageCentraliser(
                "chauffage 1",
                "type de chauffage",
                12.0f,
                12,
                "Mitachi",
                "X14",
                List.of("zone 1", "zone 2"),
                CategorieChauffage.ProducteurEmetteur,
                "regulation",
                new ArrayList<>(),
                false,
                ""
        ));
        releve.chauffages.put("chauffage 2", new ChauffageDecentraliser(
                "chauffage 2",
                "type de chauffage",
                12.0f,
                12,
                "Mitachi",
                "X14",
                "zone 2",
                CategorieChauffage.ProducteurEmetteur,
                "regulation",
                new ArrayList<>(),
                false,
                ""
        ));

        releve.calendriers.put("calendrier", new Calendrier(
                "calendrier",
                List.of("zone 1", "zone 2"),
                Map.of(new CalendrierDate(DayOfWeek.MONDAY,4,30) , ChaufferOccuper.CHAUFFER_OCCUPER,
                        new CalendrierDate(DayOfWeek.TUESDAY,4,30) , ChaufferOccuper.CHAUFFER,
                        new CalendrierDate(DayOfWeek.WEDNESDAY,4,30) , ChaufferOccuper.OCCUPER,
                        new CalendrierDate(DayOfWeek.THURSDAY,4,30) , ChaufferOccuper.CHAUFFER_OCCUPER,
                        new CalendrierDate(DayOfWeek.FRIDAY,4,30) , ChaufferOccuper.CHAUFFER_OCCUPER,
                        new CalendrierDate(DayOfWeek.SATURDAY,4,30) , ChaufferOccuper.CHAUFFER_OCCUPER,
                        new CalendrierDate(DayOfWeek.SUNDAY,4,30) , ChaufferOccuper.CHAUFFER_OCCUPER


                )

        ));

        releve.preconisations.add("preconisation 1");
        releve.preconisations.add("preconisation 2");
        releve.remarques.put("remarque 1", new Remarque("remarque 1", "note 1", false));


        releve.imageFacadeBatiment = "C:\\Users\\TLB\\Pictures\\cropped-cropped-Logo-Sieml-110-1.png";

        releve.approvisionnementEnergetiques.put("fioul", new ApprovisionnementEnergetique("fioul", "fioul", new ArrayList<>(), new ArrayList<>(), false, ""));

        // Ajoutez autant de zones et d'éléments de zone que nécessaire

        return releve;
    }
}
