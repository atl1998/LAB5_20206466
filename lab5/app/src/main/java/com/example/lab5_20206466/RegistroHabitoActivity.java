package com.example.lab5_20206466;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.*;

public class RegistroHabitoActivity extends AppCompatActivity {

    private EditText editNombre, editFrecuencia;
    private Spinner spinnerCategoria;
    private TextView txtFecha, txtHora;
    private SharedPreferences prefs;
    private final String KEY_HABITOS = "habitos_guardados";
    private Calendar calendario = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_habito);

        editNombre = findViewById(R.id.editNombreHabito);
        editFrecuencia = findViewById(R.id.editFrecuencia);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        txtFecha = findViewById(R.id.txtFecha);
        txtHora = findViewById(R.id.txtHora);
        MaterialButton btnGuardar = findViewById(R.id.btnGuardarHabito);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                Arrays.asList("Ejercicio", "Alimentación", "Sueño", "Lectura")
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);

        txtFecha.setOnClickListener(v -> seleccionarFecha());
        txtHora.setOnClickListener(v -> seleccionarHora());

        prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);

        btnGuardar.setOnClickListener(v -> guardarHabito());
    }

    private void seleccionarFecha() {
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendario.set(Calendar.YEAR, year);
            calendario.set(Calendar.MONTH, month);
            calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            txtFecha.setText(String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year));
        }, calendario.get(Calendar.YEAR), calendario.get(Calendar.MONTH), calendario.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void seleccionarHora() {
        new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            calendario.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendario.set(Calendar.MINUTE, minute);
            txtHora.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
        }, calendario.get(Calendar.HOUR_OF_DAY), calendario.get(Calendar.MINUTE), true).show();
    }

    private void guardarHabito() {
        String nombre = editNombre.getText().toString().trim();
        String categoria = spinnerCategoria.getSelectedItem().toString();
        String frecuenciaStr = editFrecuencia.getText().toString();
        String fecha = txtFecha.getText().toString();
        String hora = txtHora.getText().toString();

        if (nombre.isEmpty() || frecuenciaStr.isEmpty() || fecha.isEmpty() || hora.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int frecuenciaHoras = Integer.parseInt(frecuenciaStr);
        Habito nuevoHabito = new Habito(nombre, categoria, frecuenciaHoras, fecha, hora);

        // Guardar en SharedPreferences
        Gson gson = new Gson();
        String jsonActual = prefs.getString(KEY_HABITOS, "");
        ArrayList<Habito> listaHabitos;

        if (!jsonActual.isEmpty()) {
            Type tipo = new TypeToken<ArrayList<Habito>>() {}.getType();
            listaHabitos = gson.fromJson(jsonActual, tipo);
        } else {
            listaHabitos = new ArrayList<>();
        }

        listaHabitos.add(nuevoHabito);
        prefs.edit().putString(KEY_HABITOS, gson.toJson(listaHabitos)).apply();

        HabitoNotificationHelper.programarNotificacion(this, nuevoHabito);
        Toast.makeText(this, "Hábito guardado y notificación programada", Toast.LENGTH_SHORT).show();
        finish();
    }
}
