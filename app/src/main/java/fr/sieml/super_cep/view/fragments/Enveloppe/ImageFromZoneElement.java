package fr.sieml.super_cep.view.fragments.Enveloppe;

import fr.sieml.super_cep.R;

import java.util.HashMap;
import java.util.Map;

public class ImageFromZoneElement {



    final Map<String, Integer> images = new HashMap<String, Integer>();
    private static final int DEFAULT_IMAGE = R.drawable.ic_enveloppe_wall;

    public ImageFromZoneElement(){
        images.put("mur", R.drawable.ic_enveloppe_wall);
        images.put("toiture", R.drawable.ic_enveloppe_roof);
        images.put("menuiserie", R.drawable.ic_enveloppe_window);
        images.put("sol", R.drawable.ic_enveloppe_floor);
        images.put("éclairage", R.drawable.ic_enveloppe_light);
        images.put("centraliser", R.drawable.ic_centralise);
        images.put("decentraliser", R.drawable.ic_decentralise);
    }

    public int getImage(String logo){
        if(images.containsKey(logo)){
            return images.get(logo);
        }else{
            return DEFAULT_IMAGE;
        }
    }
}
