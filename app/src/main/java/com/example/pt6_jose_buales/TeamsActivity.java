package com.example.pt6_jose_buales;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TeamsActivity extends AppCompatActivity {

    private final List<Team> teams = new ArrayList<>();
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams);

        RecyclerView recyclerView = findViewById(R.id.viewLlista);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Obtener la liga desde el Intent (puedes fijar "mlb" para pruebas)
        String lliga = getIntent().getStringExtra("LLIGA");
        if (lliga == null) lliga = "mlb"; // fallback si no viene nada

        // Crear el adapter pasando lista, context y liga
        TeamAdapter adapter = new TeamAdapter(teams, this, lliga);
        recyclerView.setAdapter(adapter);

        // URL JSON
        String url = "https://www.vidalibarraquer.net/android/sports/" + lliga + ".json";

        if (hiHaConnexio()) {
            loadData(recyclerView, url);
        } else {
            Toast.makeText(this, "No hi ha connexió a Internet", Toast.LENGTH_LONG).show();
        }
    }

    // ===== Comprobar conexión a Internet =====
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

    // ===== Cargar datos desde JSON =====
    private void loadData(RecyclerView recyclerView, String url) {
        if (queue == null) queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(url,
                response -> {
                    try {
                        teams.clear();

                        // Aquí usamos "teams" como nombre del array
                        JSONArray array = response.getJSONArray("teams");

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);

                            // Mapear team_abbreviation → code, team_name → nombre
                            teams.add(new Team(
                                    obj.getString("team_abbreviation"),
                                    obj.getString("team_name")
                            ));
                        }

                        recyclerView.getAdapter().notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(TeamsActivity.this,
                                "Error JSON: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    Toast.makeText(TeamsActivity.this,
                            "Volley error: " + error.toString(),
                            Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                });

        queue.add(request);
    }
}
