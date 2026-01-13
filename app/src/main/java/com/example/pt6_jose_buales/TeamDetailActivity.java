package com.example.pt6_jose_buales;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Executors;

public class TeamDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);

        TextView txtTitles = findViewById(R.id.txtTitles);
        TextView txtStadium = findViewById(R.id.txtStadium);
        ImageView imgShield = findViewById(R.id.imgShield);

        String lliga = getIntent().getStringExtra("LLIGA");
        String codi = getIntent().getStringExtra("CODI").toLowerCase();

        String jsonUrl = "https://www.vidalibarraquer.net/android/sports/"
                + lliga + "/" + codi + ".json";

        String imageUrl = "https://www.vidalibarraquer.net/android/sports/"
                + lliga + "/" + codi + ".png";

        JsonObjectRequest request = new JsonObjectRequest(jsonUrl,
                response -> {
                    try {
                        JSONArray data = response.getJSONArray("data");
                        JSONObject team = data.getJSONObject(0);

                        String titles = team.getString("titles");
                        String stadium = team.getString("team_stadium");

                        txtTitles.setText("Títols: " + titles);
                        txtStadium.setText("Estadi: " + stadium);

                        // Cargar imagen SIN Glide
                        Executors.newSingleThreadExecutor().execute(() -> {
                            try {
                                InputStream is = new URL(imageUrl).openStream();
                                imgShield.post(() ->
                                        imgShield.setImageBitmap(
                                                BitmapFactory.decodeStream(is)
                                        ));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this,
                                "Error llegint el JSON",
                                Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    Toast.makeText(this,
                            "Error carregant dades",
                            Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                }
        );

        Volley.newRequestQueue(this).add(request);
    }
}
