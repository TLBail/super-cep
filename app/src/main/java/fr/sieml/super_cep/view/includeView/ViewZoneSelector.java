package fr.sieml.super_cep.view.includeView;

import android.view.View;
import android.widget.CheckBox;

import androidx.lifecycle.LifecycleOwner;

import fr.sieml.super_cep.controller.ReleveViewModel;
import fr.sieml.super_cep.databinding.ViewZonesSelectionBinding;
import fr.sieml.super_cep.model.Releve.Zone;

import java.util.ArrayList;
import java.util.List;

public class ViewZoneSelector extends View {

    ViewZonesSelectionBinding binding;
    private ReleveViewModel releveViewModel;

    public ViewZoneSelector(ViewZonesSelectionBinding binding, ReleveViewModel releveViewModel) {
        super(binding.getRoot().getContext());
        this.binding = binding;
        this.releveViewModel = releveViewModel;
        releveViewModel.getReleve().observe((LifecycleOwner) getContext(), releve -> {
            binding.linearLayoutZones.removeAllViews();
            setZonesCheckBox(releve.zones.values().toArray(new Zone[0]));
        });
        setZonesCheckBox(releveViewModel.getReleve().getValue().zones.values().toArray(new Zone[0]));

    }

    public List<String> getSelectedZones(){
        List<String> selectedZones = new ArrayList<>();
        for (int i = 0; i < binding.linearLayoutZones.getChildCount(); i++) {
            CheckBox checkBox = (CheckBox) binding.linearLayoutZones.getChildAt(i);
            if (checkBox.isChecked()){
                selectedZones.add(checkBox.getText().toString());
            }
        }
        return selectedZones;
    }

    private void setZonesCheckBox(Zone[] zones){
        binding.linearLayoutZones.removeAllViews();
        for (int i = 0; i < zones.length; i++) {
            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setText(zones[i].nom);
            binding.linearLayoutZones.addView(checkBox);
        }
    }

    public void setSelectedZones(List<String> selectedZones){
        for (int i = 0; i < binding.linearLayoutZones.getChildCount(); i++) {
            CheckBox checkBox = (CheckBox) binding.linearLayoutZones.getChildAt(i);
            if (selectedZones.contains(checkBox.getText())){
                checkBox.setChecked(true);
            }
        }
    }
}
