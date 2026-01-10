package com.example.pt6_jose_buales;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

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

    // ===== Carregar JSON =====
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
                        String badgeUrl = response.getString("badge");

                        txtEstadi.setText("Estadi: " + stadium);
                        txtTitols.setText("Títols: " + titles);

                        // Cargar imagen con Volley ImageRequest
                        ImageRequest imageRequest = new ImageRequest(
                                badgeUrl,
                                bitmap -> imgEscut.setImageBitmap(bitmap),
                                0, 0, ImageView.ScaleType.CENTER_INSIDE,
                                Bitmap.Config.ARGB_8888,
                                error -> Toast.makeText(this, "Error carregant escut", Toast.LENGTH_SHORT).show()
                        );
                        queue.add(imageRequest);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error carregant detall", Toast.LENGTH_SHORT).show()
        );

        queue.add(request);
    }

    // ===== Comprovar connexió =====
    private boolean hiHaConnexio() {
        android.net.ConnectivityManager cm =
                (android.net.ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            android.net.NetworkCapabilities caps = cm.getNetworkCapabilities(cm.getActiveNetwork());
            return caps != null &&
                    (caps.hasTransport(android.net.NetworkCapabilities.TRANSPORT_WIFI)
                            || caps.hasTransport(android.net.NetworkCapabilities.TRANSPORT_CELLULAR));
        } else {
            android.net.NetworkInfo net = cm.getActiveNetworkInfo();
            return net != null && net.isConnectedOrConnecting();
        }
    }
}
