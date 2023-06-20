package com.example.super_cep.view.fragments.ECS;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.super_cep.R;
import com.example.super_cep.controller.ReleveViewModel;
import com.example.super_cep.controller.SpinnerDataViewModel;
import com.example.super_cep.databinding.FragmentECSAjoutBinding;
import com.example.super_cep.databinding.ViewFooterZoneElementBinding;
import com.example.super_cep.databinding.ViewFooterZoneElementConsultationBinding;
import com.example.super_cep.model.ECS;
import com.example.super_cep.view.Mode;
import com.example.super_cep.view.includeView.ViewPhoto;
import com.example.super_cep.view.includeView.ViewZoneSelector;

public class FragmentECSAjout extends Fragment {

    private static final String ARG_NOM_ECS = "param2";

    private String nomECS;


    private Mode mode = Mode.Ajout;
    public FragmentECSAjout() {
        // Required empty public constructor
    }

    public static FragmentECSAjout newInstance() {
        return newInstance(null);
    }


    public static FragmentECSAjout newInstance(String nomECS) {
        FragmentECSAjout fragment = new FragmentECSAjout();
        Bundle args = new Bundle();
        args.putString(ARG_NOM_ECS, nomECS);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments().getString(ARG_NOM_ECS) != null){
            mode = Mode.Edition;
            nomECS = getArguments().getString(ARG_NOM_ECS);
        }

    }

    private FragmentECSAjoutBinding binding;

    private ReleveViewModel releveViewModel;
    private SpinnerDataViewModel spinnerDataViewModel;


    private ViewZoneSelector viewZoneSelector;
    private ViewPhoto viewPhoto;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentECSAjoutBinding.inflate(inflater, container, false);
        releveViewModel = new ViewModelProvider(requireActivity()).get(ReleveViewModel.class);
        spinnerDataViewModel = new ViewModelProvider(requireActivity()).get(SpinnerDataViewModel.class);
        viewPhoto = new ViewPhoto(binding.includeViewPhoto, this);
        viewPhoto.setupPhotoLaunchers();
        updateSpinner();
        viewZoneSelector = new ViewZoneSelector(binding.includeZoneSelection, releveViewModel);

        if(mode == Mode.Ajout){
            addFooterAjout();
        }

        try {
            if(mode == Mode.Edition){
                ECS ecs = releveViewModel.getReleve().getValue().ecs.get(nomECS);
                setModeEdition(ecs);
                addDataToView(ecs);
            }
        }catch (Exception e){
            Log.e("Ajout ecs", "onCreateView: ", e);
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
                addECSToReleve();
            }
        });

        binding.linearLayoutAjoutECS.addView(viewFooter.getRoot());
    }

    private void setModeEdition(ECS ecs) {
        binding.textViewTitleECS.setText(ecs.nom);
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
                editECS();
            }
        });

        viewFooter.buttonSupprimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                releveViewModel.removeECS(nomECS);
                getParentFragmentManager().popBackStack();
            }
        });

        binding.linearLayoutAjoutECS.addView(viewFooter.getRoot());
    }


    private void addECSToReleve() {
        try {
            releveViewModel.addECS(getECSFromViews());
            getParentFragmentManager().popBackStack();

        }catch (Exception e){
            Log.e("ECS", "addECSToReleve: ", e);
            Toast.makeText(getContext(), e.getMessage() , Toast.LENGTH_SHORT).show();
        }
    }

    private void editECS(){
        try {
            releveViewModel.editECS(nomECS, getECSFromViews());
            getParentFragmentManager().popBackStack();
        } catch (Exception e) {
            Log.e("ECS", "addECSToReleve: ", e);
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateSpinner() {
        spinnerDataViewModel.updateSpinnerData(binding.spinnerTypeECS, "typeECS");
        spinnerDataViewModel.updateSpinnerData(binding.spinnerMarque, "marqueECS");
    }

    private ECS getECSFromViews(){
        ECS ecs = new ECS(
                binding.editTextNomECS.getText().toString(),
                binding.spinnerTypeECS.getSelectedItem().toString(),
                binding.editTextModele.getText().toString(),
                binding.spinnerMarque.getSelectedItem().toString(),
                binding.editTextVolume.getText().toString().isEmpty() ? 0 : Float.parseFloat(binding.editTextVolume.getText().toString()),
                binding.editTextNumberQuantite.getText().toString().isEmpty() ? 0 : Integer.parseInt(binding.editTextNumberQuantite.getText().toString()),
                viewZoneSelector.getSelectedZones(),
                viewPhoto.getUriImages(),
                binding.checkBoxAVerifierECS.isChecked(),
                binding.editTextMultilineNoteECS.getText().toString()
        );

        return ecs;
    }

    private void addDataToView(ECS ecs){
        binding.editTextNomECS.setText(ecs.nom);
        spinnerDataViewModel.setSpinnerSelection(binding.spinnerTypeECS, ecs.type);
        binding.editTextModele.setText(ecs.modele);
        spinnerDataViewModel.setSpinnerSelection(binding.spinnerMarque, ecs.marque);
        binding.editTextVolume.setText(String.valueOf(ecs.volume));
        binding.editTextNumberQuantite.setText(String.valueOf(ecs.quantite));

        viewZoneSelector.setSelectedZones(ecs.zones);
        binding.checkBoxAVerifierECS.setChecked(ecs.aVerifier);
        binding.editTextMultilineNoteECS.setText(ecs.note);

        for (Uri uri : ecs.uriImages) {
            viewPhoto.addPhotoToView(uri);
        }
    }
}