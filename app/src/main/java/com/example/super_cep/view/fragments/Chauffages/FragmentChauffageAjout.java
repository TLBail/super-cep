package com.example.super_cep.view.fragments.Chauffages;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.super_cep.R;
import com.example.super_cep.controller.PhotoManager;
import com.example.super_cep.controller.ReleveViewModel;
import com.example.super_cep.controller.SpinnerDataViewModel;
import com.example.super_cep.databinding.FragmentChauffageAjoutBinding;
import com.example.super_cep.databinding.ViewFooterZoneElementBinding;
import com.example.super_cep.databinding.ViewFooterZoneElementConsultationBinding;
import com.example.super_cep.databinding.ViewImageZoneElementBinding;
import com.example.super_cep.model.Chauffage;
import com.example.super_cep.model.Enveloppe.Zone;
import com.example.super_cep.view.Mode;
import com.example.super_cep.view.includeView.ViewPhoto;
import com.example.super_cep.view.includeView.ViewZoneSelector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FragmentChauffageAjout extends Fragment {


    private static final String ARG_NOM_CHAUFFAGE = "param2";

    private String nomChauffage;


    private Mode mode = Mode.Ajout;
    public FragmentChauffageAjout() {
        // Required empty public constructor
    }

    public static FragmentChauffageAjout newInstance() {
        return newInstance(null);
    }


    public static FragmentChauffageAjout newInstance(String nomChauffage) {
        FragmentChauffageAjout fragment = new FragmentChauffageAjout();
        Bundle args = new Bundle();
        args.putString(ARG_NOM_CHAUFFAGE, nomChauffage);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments().getString(ARG_NOM_CHAUFFAGE) != null){
            mode = Mode.Edition;
            nomChauffage = getArguments().getString(ARG_NOM_CHAUFFAGE);
        }

    }

    private FragmentChauffageAjoutBinding binding;

    private ReleveViewModel releveViewModel;
    private SpinnerDataViewModel spinnerDataViewModel;



    List<String> typeChauffageProducteur = new ArrayList<>();
    List<String> typeChauffageEmetteur = new ArrayList<>();

    private ViewPhoto viewPhoto;
    private ViewZoneSelector viewZoneSelector;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChauffageAjoutBinding.inflate(inflater, container, false);
        releveViewModel = new ViewModelProvider(requireActivity()).get(ReleveViewModel.class);
        spinnerDataViewModel = new ViewModelProvider(requireActivity()).get(SpinnerDataViewModel.class);
        Map<String, List<String>> spinnerLiveData = spinnerDataViewModel.getSpinnerData().getValue();
        if(!spinnerLiveData.containsKey("typeChauffageEmetteur") || !spinnerLiveData.containsKey("typeChauffageProducteur")){
            Toast.makeText(getContext(), "Erreur lors de la récupération des données", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().popBackStack();
            return binding.getRoot();
        }
        typeChauffageEmetteur = spinnerLiveData.get("typeChauffageEmetteur");
        typeChauffageProducteur = spinnerLiveData.get("typeChauffageProducteur");
        viewPhoto = new ViewPhoto(binding.includeViewPhoto, this);
        viewPhoto.setupPhotoLaunchers();
        viewZoneSelector = new ViewZoneSelector(binding.includeZoneSelection, releveViewModel);

        updateSpinner();

        if(mode == Mode.Ajout){
            addFooterAjout();
        }

        try {
            if(mode == Mode.Edition){
                Chauffage chauffage = releveViewModel.getReleve().getValue().chauffages.get(nomChauffage);
                setModeEdition(chauffage);
                addDataToView(chauffage);
            }
        }catch (Exception e){
            Log.e("Ajout chauffage", "onCreateView: ", e);
            Toast.makeText(getContext(), "Erreur lors de la récupération des données", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().popBackStack();
        }

        return binding.getRoot();

    }

    private void addFooterAjout() {
        ViewFooterZoneElementBinding viewFooter = ViewFooterZoneElementBinding.inflate(getLayoutInflater());
        viewFooter.buttonAnnuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        viewFooter.buttonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addChauffageToReleve();
            }
        });

        binding.linearLayoutAjoutChauffage.addView(viewFooter.getRoot());
    }

    private void setModeEdition(Chauffage chauffage) {
        binding.textViewTitleChauffage.setText(chauffage.nom);
        ViewFooterZoneElementConsultationBinding viewFooter = ViewFooterZoneElementConsultationBinding.inflate(getLayoutInflater());
        viewFooter.buttonAnnuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().popBackStack();
            }
        });

        viewFooter.buttonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editChauffage();
            }
        });

        viewFooter.buttonSupprimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                releveViewModel.removeChauffage(nomChauffage);
                getParentFragmentManager().popBackStack();
            }
        });

        binding.linearLayoutAjoutChauffage.addView(viewFooter.getRoot());
    }


    private void addChauffageToReleve() {
        try {
            releveViewModel.addChauffage(getChauffageFromViews());
            getParentFragmentManager().popBackStack();

        }catch (Exception e){
            Log.e("Chauffage", "addChauffageToReleve: ", e);
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void editChauffage(){
        try {
            releveViewModel.editChauffage(nomChauffage, getChauffageFromViews());
            getParentFragmentManager().popBackStack();
        } catch (Exception e) {
            Log.e("Chauffage", "addChauffageToReleve: ", e);
            Toast.makeText(getContext(), e.getMessage() , Toast.LENGTH_SHORT).show();
        }
    }


    private void updateSpinner() {
        List<String> customList = new ArrayList<>();
        customList.addAll(typeChauffageProducteur);
        customList.addAll(typeChauffageEmetteur);

        SpinnerDataViewModel.updateSpinnerData(binding.spinnerTypeChauffage, customList);
        spinnerDataViewModel.updateSpinnerData(binding.spinnerRegulations, "regulationChauffage");

        binding.spinnerTypeChauffage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(estProducteur()){
                    spinnerDataViewModel.updateSpinnerData(binding.spinnerMarque, "marqueProducteur");
                }else{
                    spinnerDataViewModel.updateSpinnerData(binding.spinnerMarque, "marqueEmetteur");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    private boolean estProducteur(){
        return typeChauffageProducteur.contains(binding.spinnerTypeChauffage.getSelectedItem().toString());
    }

    private Chauffage getChauffageFromViews(){
            Chauffage chauffage = new Chauffage(
                    binding.editTextNomChauffage.getText().toString(),
                    binding.spinnerTypeChauffage.getSelectedItem().toString(),
                    binding.editTextNumberPuissance.getText().toString().isEmpty() ? 0 : Float.parseFloat(binding.editTextNumberPuissance.getText().toString()),
                    binding.editTextNumberQuantite.getText().toString().isEmpty() ? 0 : Integer.parseInt(binding.editTextNumberQuantite.getText().toString()),
                    binding.spinnerMarque.getSelectedItem().toString(),
                    binding.editTextModele.getText().toString(),
                    viewZoneSelector.getSelectedZones(),
                    estProducteur(),
                    binding.spinnerRegulations.getSelectedItem().toString(),
                    viewPhoto.getUriImages(),
                    binding.checkBoxAVerifierChauffage.isChecked(),
                    binding.editTextMultilineNoteChauffage.getText().toString()
            );

        return chauffage;
    }

    private void addDataToView(Chauffage chauffage){
        binding.editTextNomChauffage.setText(chauffage.nom);
        spinnerDataViewModel.setSpinnerSelection(binding.spinnerTypeChauffage, chauffage.type);
        binding.editTextNumberPuissance.setText(String.valueOf(chauffage.puissance));
        binding.editTextNumberQuantite.setText(String.valueOf(chauffage.quantite));
        spinnerDataViewModel.setSpinnerSelection(binding.spinnerMarque, chauffage.marque);
        binding.editTextModele.setText(chauffage.modele);
        spinnerDataViewModel.setSpinnerSelection(binding.spinnerRegulations, chauffage.regulation);
        binding.checkBoxAVerifierChauffage.setChecked(chauffage.aVerifier);
        binding.editTextMultilineNoteChauffage.setText(chauffage.note);

        viewZoneSelector.setSelectedZones(chauffage.zones);
        for (Uri uri : chauffage.uriImages) {
            viewPhoto.addPhotoToView(uri);
        }
    }

}