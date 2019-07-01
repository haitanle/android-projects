package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) {
       Log.v(JsonUtils.class.getSimpleName(),json);


       try {
           JSONObject dish = new JSONObject(json);

           JSONObject name = dish.getJSONObject("name");
           String mainName = name.getString("mainName");

           List<String> alsoKnownAs = new ArrayList<String>();
           JSONArray arrayAlsoKnownAs = name.getJSONArray("alsoKnownAs");
           for (int i = 0; i < arrayAlsoKnownAs.length(); i++) {
               alsoKnownAs.add(arrayAlsoKnownAs.getString(i));
           }

           String placeOfOrigin = dish.getString("placeOfOrigin");

           String imageUrl = dish.getString("image");
           String description = dish.getString("description");

           List<String> ingredients = new ArrayList<String>();
           JSONArray arrayIngredients = dish.getJSONArray("ingredients");

           for (int i = 0; i < arrayIngredients.length(); i++) {
               ingredients.add(arrayIngredients.getString(i));
           }

           Sandwich sandwich = new Sandwich();
           sandwich.setMainName(mainName);
           sandwich.setImage(imageUrl);
           sandwich.setAlsoKnownAs(alsoKnownAs);
           sandwich.setPlaceOfOrigin(placeOfOrigin);
           sandwich.setIngredients(ingredients);
           sandwich.setDescription(description);

           return sandwich;
       }catch (JSONException e){
           e.printStackTrace();
       }


       return null;
    }
}


// {"name":{"mainName":"Chivito","alsoKnownAs":[]},"placeOfOrigin":"Uruguay","description":"Chivito is a national dish of Uruguay, It is a thin slice of tender cooked beef steak (churrasco), with mozzarella, tomatoes, mayonnaise, black or green olives, and commonly also bacon, fried or hard-boiled eggs and ham, served as a sandwich in a bun, often accompanied by French fried potatoes. Other ingredients, such as red beets, peas, grilled or pan-fried red peppers, and slices of cucumber, may be added.","image":"https://upload.wikimedia.org/wikipedia/commons/4/48/Chivito1.jpg","ingredients":["Bun","Churrasco beef","Bacon","Fried or hard-boiled eggs","Ham","Black or green olives","Mozzarella","Tomatoes","Mayonnaise"]}