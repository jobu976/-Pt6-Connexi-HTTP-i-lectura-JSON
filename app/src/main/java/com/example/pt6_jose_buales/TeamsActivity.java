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

        String lliga = getIntent().getStringExtra("LLIGA");
        if (lliga == null) lliga = "mlb";

        TeamAdapter adapter = new TeamAdapter(teams, this, lliga);
        recyclerView.setAdapter(adapter);

        String url = "https://www.vidalibarraquer.net/android/sports/" + lliga + ".json";

        if (queue == null) queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(url,
                response -> {
                    try {
                        teams.clear();
                        JSONArray array = response.getJSONArray("teams");

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            teams.add(new Team(
                                    obj.getString("team_abbreviation"),
                                    obj.getString("team_name")
                            ));
                        }

                        recyclerView.getAdapter().notifyDataSetChanged();

                    } catch (Exception e) {
                        Toast.makeText(this, "Error JSON", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error carregant dades", Toast.LENGTH_SHORT).show()
        );

        queue.add(request);
    }
}
