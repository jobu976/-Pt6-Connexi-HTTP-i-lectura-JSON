package com.example.pt6_jose_buales;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;

public class TeamDetailActivity extends AppCompatActivity {

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);

        ImageView imgEscut = findViewById(R.id.imgEscut);
        TextView txtEstadi = findViewById(R.id.txtEstadi);
        TextView txtTitols = findViewById(R.id.txtTitols);

        String lliga = getIntent().getStringExtra("LLIGA");
        String codi = getIntent().getStringExtra("CODI");

        String url = "https://www.vidalibarraquer.net/android/sports/"
                + lliga + "/" + codi.toLowerCase() + ".json";

        if (hiHaConnexio()) {
            loadData(url, imgEscut, txtEstadi, txtTitols);
        } else {
            Toast.makeText(this, "No hi ha connexió a Internet", Toast.LENGTH_LONG).show();
        }
    }

    // ===== Comprovar Internet =====
    private boolean hiHaConnexio() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NetworkCapabilities caps = cm.getNetworkCapabilities(cm.getActiveNetwork());
            return caps != null &&
                    (caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                            || caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
        } else {
            NetworkInfo net = cm.getActiveNetworkInfo();
            return net != null && net.isConnectedOrConnecting();
        }
    }

    // ===== Carregar JSON detall =====
    private void loadData(String url, ImageView imgEscut,
                          TextView txtEstadi, TextView txtTitols) {

        if (queue == null) {
            queue = Volley.newRequestQueue(this);
        }

        JsonObjectRequest request = new JsonObjectRequest(url,
                response -> {
                    try {
                        String stadium = response.getString("stadium");
                        int titles = response.getInt("titles");
                        String badge = response.getString("badge");

                        txtEstadi.setText("Estadi: " + stadium);
                        txtTitols.setText("Títols: " + titles);

                        Glide.with(this)
                                .load(badge)
                                .into(imgEscut);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error carregant detall", Toast.LENGTH_SHORT).show()
        );

        queue.add(request);
    }
}
