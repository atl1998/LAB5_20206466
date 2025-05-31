package com.example.lab5_20206466;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 1001;
    private ImageView imagenUsuario;
    private TextView saludoText, mensajeText;
    private SharedPreferences prefs;
    private String imagenFileName = "foto_usuario.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saludoText = findViewById(R.id.txtSaludo);
        mensajeText = findViewById(R.id.txtMensaje);
        imagenUsuario = findViewById(R.id.imgUsuario);
        MaterialButton btnHabitos = findViewById(R.id.btnVerHabitos);
        MaterialButton btnConfiguraciones = findViewById(R.id.btnConfiguraciones);

        prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        cargarDatos();
        cargarImagen();

        imagenUsuario.setOnClickListener(v -> seleccionarImagen());

        btnHabitos.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ListaHabitosActivity.class));
        });

        btnConfiguraciones.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ConfiguracionesActivity.class));
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }
    }

    private void seleccionarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imagenUsuario.setImageBitmap(bitmap);

                // Guardar imagen en Internal Storage
                File file = new File(getFilesDir(), imagenFileName);
                FileOutputStream fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
                Toast.makeText(this, "Imagen guardada", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al cargar imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void cargarImagen() {
        File file = new File(getFilesDir(), imagenFileName);
        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            imagenUsuario.setImageBitmap(bitmap);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        cargarDatos();  // recarga saludo, mensaje y (opcionalmente) imagen
    }

    private void cargarDatos() {
        String nombre = prefs.getString("nombreUsuario", "Usuario");
        String mensaje = prefs.getString("mensajeMotivacional", "¡Tú puedes con todo hoy!");
        saludoText.setText("¡Hola, " + nombre + "!");
        mensajeText.setText(mensaje);
    }
}
