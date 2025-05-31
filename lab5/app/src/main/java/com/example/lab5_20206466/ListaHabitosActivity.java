package com.example.lab5_20206466;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ListaHabitosActivity extends AppCompatActivity {

    private ArrayList<Habito> listaHabitos;
    private HabitosAdapter adapter;
    private SharedPreferences prefs;
    private static final String KEY_HABITOS = "habitos_guardados";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_habitos);

        prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        listaHabitos = cargarHabitos();

        RecyclerView recyclerView = findViewById(R.id.recyclerHabitos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HabitosAdapter(listaHabitos);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fabAgregar);
        fab.setOnClickListener(v -> {
            startActivity(new Intent(this, RegistroHabitoActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        listaHabitos.clear();
        listaHabitos.addAll(cargarHabitos());
        adapter.notifyDataSetChanged();
    }

    private ArrayList<Habito> cargarHabitos() {
        String json = prefs.getString(KEY_HABITOS, "");
        if (!json.isEmpty()) {
            Type tipoLista = new TypeToken<ArrayList<Habito>>() {}.getType();
            return new Gson().fromJson(json, tipoLista);
        }
        return new ArrayList<>();
    }
}
