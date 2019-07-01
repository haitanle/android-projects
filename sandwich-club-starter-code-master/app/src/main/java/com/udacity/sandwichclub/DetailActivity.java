package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        Log.v(DetailActivity.class.getSimpleName(), "*****HERE*****");

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {

        TextView mAlsoKnownAsTextView = (TextView) findViewById(R.id.also_known_tv);
        for (String alsoKnownAs: sandwich.getAlsoKnownAs()){
            mAlsoKnownAsTextView.append(alsoKnownAs+"\n");
        }

        TextView mPlaceOfOriginTextView = (TextView) findViewById(R.id.origin_tv);
        mPlaceOfOriginTextView.setText(sandwich.getPlaceOfOrigin());

        TextView mIngredientsTextView = (TextView) findViewById(R.id.ingredients_tv);
        for (String ingredient: sandwich.getIngredients()){
            mIngredientsTextView.append(ingredient+"\n");
        }

        TextView mDescriptionTextView = (TextView) findViewById(R.id.description_tv);
        mDescriptionTextView.setText(sandwich.getDescription());
    }
}