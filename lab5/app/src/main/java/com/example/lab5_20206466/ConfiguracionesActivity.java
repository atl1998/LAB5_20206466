package com.example.lab5_20206466;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class ConfiguracionesActivity extends AppCompatActivity {

    private EditText editNombre, editMensaje, editFrecuencia;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuraciones);

        editNombre = findViewById(R.id.editNombre);
        editMensaje = findViewById(R.id.editMensaje);
        editFrecuencia = findViewById(R.id.editFrecuencia);
        MaterialButton btnGuardar = findViewById(R.id.btnGuardarConfig);

        prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);

        // Cargar datos actuales
        editNombre.setText(prefs.getString("nombreUsuario", ""));
        editMensaje.setText(prefs.getString("mensajeMotivacional", ""));
        editFrecuencia.setText(String.valueOf(prefs.getInt("frecuenciaMotivacionalHoras", 6)));

        btnGuardar.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("nombreUsuario", editNombre.getText().toString().trim());
            editor.putString("mensajeMotivacional", editMensaje.getText().toString().trim());
            editor.putInt("frecuenciaMotivacionalHoras", Integer.parseInt(editFrecuencia.getText().toString()));
            editor.apply();
            String mensaje = editMensaje.getText().toString().trim();
            int frecuencia = Integer.parseInt(editFrecuencia.getText().toString());
            MotivationalNotificationHelper.programar(this, mensaje, frecuencia);

            Toast.makeText(this, "Configuraciones guardadas y notificación motivacional activada", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
