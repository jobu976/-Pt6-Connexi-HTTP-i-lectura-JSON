package com.example.pt6_jose_buales;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
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
        TeamAdapter adapter = new TeamAdapter(teams, this);
        recyclerView.setAdapter(adapter);

        String lliga = getIntent().getStringExtra("LLIGA");

        String url = "https://www.vidalibarraquer.net/android/sports/" + lliga + ".json";

        if (hiHaConnexio()) {
            loadData(recyclerView, url);
        } else {
            Toast.makeText(this, "No hi ha connexió a Internet", Toast.LENGTH_LONG).show();
        }
    }

    // ===== Comprovar connexió =====
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

    // ===== Carregar JSON =====
    private void loadData(RecyclerView recyclerView, String url) {
        if (queue == null) {
            queue = Volley.newRequestQueue(this);
        }

        JsonObjectRequest request = new JsonObjectRequest(url,
                response -> {
                    try {
                        teams.clear();
                        JSONArray array = response.getJSONArray("data");

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            teams.add(new Team(
                                    obj.getString("code"),
                                    obj.getString("name")
                            ));
                        }
                        recyclerView.getAdapter().notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error carregant dades", Toast.LENGTH_SHORT).show()
        );

        queue.add(request);
    }
}
