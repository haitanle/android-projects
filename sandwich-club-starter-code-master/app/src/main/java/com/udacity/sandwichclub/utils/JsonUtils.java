package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String NAME = "name";
    private static final String MAIN_NAME = "mainName";
    private static final String ALSO_KNOWN_AS = "alsoKnownAs";
    private static final String PLACE_OF_ORIGIN = "placeOfOrigin";
    private static final String IMAGE = "image";
    private static final String DESCRIPTION = "description";
    private static final String INGREDIENTS = "ingredients";
    private static final String NA = "N/A";

    /* Parses json data and return Sandwich object
    * json example -  {"name":{"mainName":"Chivito","alsoKnownAs":[]},"placeOfOrigin":"Uruguay","description":"Chivito is a national dish of Uruguay, It is a thin slice of tender cooked beef steak (churrasco), with mozzarella, tomatoes, mayonnaise, black or green olives, and commonly also bacon, fried or hard-boiled eggs and ham, served as a sandwich in a bun, often accompanied by French fried potatoes. Other ingredients, such as red beets, peas, grilled or pan-fried red peppers, and slices of cucumber, may be added.","image":"https://upload.wikimedia.org/wikipedia/commons/4/48/Chivito1.jpg","ingredients":["Bun","Churrasco beef","Bacon","Fried or hard-boiled eggs","Ham","Black or green olives","Mozzarella","Tomatoes","Mayonnaise"]}
    * Input: String JSON
    * Return: Sandwich object
    */
    public static Sandwich parseSandwichJson(String json) {
       Log.v(JsonUtils.class.getSimpleName(),json);

       try {
           JSONObject dish = new JSONObject(json);

           JSONObject name = dish.getJSONObject(NAME);
           String mainName = name.optString(MAIN_NAME, NA);

           List<String> alsoKnownAs = new ArrayList<String>();
           JSONArray arrayAlsoKnownAs = name.getJSONArray(ALSO_KNOWN_AS);
           for (int i = 0; i < arrayAlsoKnownAs.length(); i++) {
               alsoKnownAs.add(arrayAlsoKnownAs.getString(i));
           }

           String placeOfOrigin = dish.optString(PLACE_OF_ORIGIN, NA);

           String imageUrl = dish.optString(IMAGE, NA);
           String description = dish.optString(DESCRIPTION, NA);

           List<String> ingredients = new ArrayList<String>();
           JSONArray arrayIngredients = dish.getJSONArray(INGREDIENTS);

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

