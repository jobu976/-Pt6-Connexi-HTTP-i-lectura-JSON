package com.example.pt6_jose_buales;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final List<Team> elements = new ArrayList<>();
    private RequestQueue queue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.viewLlista);
        TeamAdapter adapter = new TeamAdapter(elements, this);
        recyclerView.setAdapter(adapter);

        // Recuperar la liga
        String lliga = getIntent().getStringExtra("LLIGA");

        String url = "https://www.vidalibarraquer.net/android/sports/" + lliga + ".json";

        if (hiHaConnexio())
            loadData(recyclerView, url);
        else
            Toast.makeText(this, "No hi ha connexió a Internet", Toast.LENGTH_SHORT).show();
    }

    // ======= COMPROVACIÓ INTERNET =======
    private boolean hiHaConnexio() {
        boolean resultat = false;
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NetworkCapabilities cap = cm.getNetworkCapabilities(cm.getActiveNetwork());
            if (cap != null) {
                resultat = cap.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        cap.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
            }
        } else {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            resultat = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        return resultat;
    }

    // ======= CARREGAR JSON =======
    private void loadData(RecyclerView recyclerView, String url) {
        if (queue == null)
            queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(url,
                response -> {
                    try {
                        elements.clear();

                        JSONArray jsonArray = response.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);

                            Team team = new Team(
                                    obj.getString("code"),
                                    obj.getString("name")
                            );
                            elements.add(team);
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