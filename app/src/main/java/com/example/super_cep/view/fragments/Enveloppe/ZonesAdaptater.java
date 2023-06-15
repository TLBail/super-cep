package com.example.super_cep.view.fragments.Enveloppe;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.super_cep.R;
import com.example.super_cep.databinding.ViewZoneBinding;
import com.example.super_cep.databinding.ViewZoneElementBinding;
import com.example.super_cep.model.Enveloppe.Zone;
import com.example.super_cep.model.Enveloppe.ZoneElement;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ZonesAdaptater extends RecyclerView.Adapter<ZoneViewHolder> {

    private Zone[] zones;
    private ZoneUiHandler zoneUiHandler;
    public ZonesAdaptater(Zone[] zones, ZoneUiHandler zoneUiHandler) {
        this.zones = zones;
        this.zoneUiHandler = zoneUiHandler;
    }

    @NonNull
    @Override
    public ZoneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View zoneViewHolder = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_zone, parent, false);
        return new ZoneViewHolder(zoneViewHolder);
    }

    @Override
    public void onBindViewHolder(@NonNull ZoneViewHolder holder, int position) {
        Zone zone = zones[position];
        setupDragAndDrop(holder, zone);
        holder.getZoneName().setText(zone.nom);
        updateZoneElements(holder, zone);
    }

    private void updateZoneElements(ZoneViewHolder holder, Zone zone) {
        int nbElementPerRow = 0;
        Context context = holder.getTableLayout().getContext();
        TableLayout tableLayout = holder.getTableLayout();
        TableRow tableRow = new TableRow(context);
        tableLayout.addView(tableRow);
        ZoneElement[] zoneElements = zone.getZoneElements();
        for (ZoneElement zoneElement : zone.getZoneElements()) {
            ZoneElementView zoneElementView = new ZoneElementView(tableRow,zone,  zoneElement, zoneUiHandler);

            // Define LayoutParams for ZoneElementView
            TableRow.LayoutParams lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
            zoneElementView.setLayoutParams(lp);
            nbElementPerRow++;
            tableRow.addView(zoneElementView);

            if (nbElementPerRow == 4) {
                nbElementPerRow = 0;
                tableRow = new TableRow(context);
                tableLayout.addView(tableRow);
            }

        }
        ajouterBouttonAjoutElement(tableRow, zone);
        if(zoneElements.length == 0)
            ajouterBouttonSuprimerZone(tableRow, zone);
    }

    private void setupDragAndDrop(ZoneViewHolder holder, Zone zone) {
        holder.itemView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()){
                    case DragEvent.ACTION_DRAG_STARTED:
                        if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                            ((ConstraintLayout) v).setBackground(v.getContext().getDrawable(R.drawable.border_teal));
                            v.invalidate();
                            return true;
                        }

                        return false;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        ((ConstraintLayout) v).setBackground(v.getContext().getDrawable(R.drawable.border_green));
                        v.invalidate();
                        return true;
                    case DragEvent.ACTION_DRAG_LOCATION:
                        return true;
                    case DragEvent.ACTION_DRAG_EXITED:
                        ((ConstraintLayout) v).setBackground(v.getContext().getDrawable(R.drawable.border_teal));
                        v.invalidate();

                        return true;
                    case DragEvent.ACTION_DROP:
                        ClipData.Item itemZoneElement = event.getClipData().getItemAt(0);
                        CharSequence nomZoneElement = itemZoneElement.getText();
                        ClipData.Item itemZone = event.getClipData().getItemAt(1);
                        CharSequence nomZone = itemZone.getText();
                        Log.i("drag",  "Dragged data is " + nomZoneElement + " moved to " + zone.nom + " from " + nomZone);

                        zoneUiHandler.moveZoneElement(nomZoneElement.toString(), nomZone.toString(), zone.nom);

                        ((ConstraintLayout) v).setBackground(v.getContext().getDrawable(R.drawable.border_black));
                        v.invalidate();
                        return true;
                    case DragEvent.ACTION_DRAG_ENDED:
                        ((ConstraintLayout) v).setBackground(v.getContext().getDrawable(R.drawable.border_black));
                        v.invalidate();
                        return true;
                }
                return false;
            }
        });
    }

    private void ajouterBouttonAjoutElement(TableRow tableRow, Zone zone) {
        View buttonAjout = LayoutInflater.from(tableRow.getContext()).inflate(R.layout.view_zone_ajouter_un_element, tableRow, false);

        TableRow.LayoutParams lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        buttonAjout.setLayoutParams(lp);
        tableRow.addView(buttonAjout);
        buttonAjout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoneUiHandler.nouvelleElementZone(zone);
            }
        });
    }

    private void ajouterBouttonSuprimerZone(TableRow tableRow, Zone zone){
        View buttonSuprimer = LayoutInflater.from(tableRow.getContext()).inflate(R.layout.view_zone_suprimer_zone, tableRow, false);
// Define LayoutParams for ZoneElementView

        TableRow.LayoutParams lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
        buttonSuprimer.setLayoutParams(lp);
        tableRow.addView(buttonSuprimer);
        buttonSuprimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Supprimer la zone ?", Snackbar.LENGTH_LONG)
                        .setAction("Oui", null).show();
                zoneUiHandler.deleteZone(zone);}
        });
    }

    @Override
    public int getItemCount() {
        return zones.length ;
    }
}
