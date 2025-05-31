package com.example.lab5_20206466;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class StorageUtil {

    private static final String PREFS_NAME = "MisPreferencias";
    private static final String KEY_HABITOS = "habitos_guardados";

    // Guardar lista completa de hábitos
    public static void guardarHabitos(Context context, ArrayList<Habito> listaHabitos) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(listaHabitos);
        editor.putString(KEY_HABITOS, json);
        editor.apply();
    }

    // Cargar lista de hábitos
    public static ArrayList<Habito> cargarHabitos(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_HABITOS, "");
        if (!json.isEmpty()) {
            Type tipoLista = new TypeToken<ArrayList<Habito>>() {}.getType();
            return new Gson().fromJson(json, tipoLista);
        }
        return new ArrayList<>();
    }

    // Eliminar hábito por nombre (clave única)
    public static void eliminarHabito(Context context, String nombreHabito) {
        ArrayList<Habito> lista = cargarHabitos(context);
        lista.removeIf(h -> h.getNombre().equals(nombreHabito));
        guardarHabitos(context, lista);
    }
}
