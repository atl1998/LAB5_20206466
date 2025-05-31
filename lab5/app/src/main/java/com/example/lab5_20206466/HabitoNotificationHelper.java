package com.example.lab5_20206466;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import java.util.Calendar;
import java.util.Random;

public class HabitoNotificationHelper {

    public static void programarNotificacion(Context context, Habito habito) {
        String canalId = habito.getCategoria(); // canal basado en la categoría
        int frecuenciaHoras = habito.getFrecuenciaHoras();

        // Crear intent
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("titulo", habito.getNombre());
        intent.putExtra("mensaje", "¡Hora de " + habito.getNombre() + "!");
        intent.putExtra("canalId", canalId);
        intent.putExtra("icono", obtenerIcono(habito.getCategoria()));

        int idNotificacion = new Random().nextInt(100000); // ID único
        intent.putExtra("id", idNotificacion);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                idNotificacion,
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        // Calcular hora de inicio
        Calendar calendar = Calendar.getInstance();
        String[] fecha = habito.getFechaInicio().split("/");
        String[] hora = habito.getHoraInicio().split(":");

        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(fecha[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(fecha[1]) - 1); // mes comienza en 0
        calendar.set(Calendar.YEAR, Integer.parseInt(fecha[2]));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hora[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(hora[1]));
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long intervalo = frecuenciaHoras * 60L * 60L * 1000L; // ms

        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                intervalo,
                pendingIntent
        );
    }

    private static int obtenerIcono(String categoria) {
        switch (categoria.toLowerCase()) {
            case "ejercicio":
                return R.drawable.ic_ejercicio;
            case "alimentación":
                return R.drawable.ic_alimentacion;
            case "sueño":
                return R.drawable.ic_sueno;
            case "lectura":
                return R.drawable.ic_lectura;
            default:
                return R.drawable.ic_launcher_foreground;
        }
    }
}

