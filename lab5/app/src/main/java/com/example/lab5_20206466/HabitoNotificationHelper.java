package com.example.lab5_20206466;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import java.util.Calendar;
import java.util.Random;

public class HabitoNotificationHelper {

    public static void programarNotificacion(Context context, Habito habito) {
        String canalId = habito.getCategoria().toLowerCase(); // canal basado en categoría
        int frecuenciaHoras = habito.getFrecuenciaHoras();

        // Intent para AlarmReceiver
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("titulo", habito.getNombre());
        intent.putExtra("mensaje", "¡Hora de " + habito.getNombre() + "!");
        intent.putExtra("canalId", canalId);
        intent.putExtra("icono", obtenerIcono(habito.getCategoria()));
        intent.putExtra("frecuenciaHoras", frecuenciaHoras);
        intent.putExtra("esHabito", true);
        intent.putExtra("nombreHabito", habito.getNombre());
        intent.putExtra("categoria", habito.getCategoria());

        int idNotificacion = habito.getNombre().hashCode(); // ID único basado en nombre
        intent.putExtra("id", idNotificacion);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                idNotificacion,
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        // Convertir fecha y hora de inicio
        Calendar calendar = Calendar.getInstance();
        String[] fecha = habito.getFechaInicio().split("/");
        String[] hora = habito.getHoraInicio().split(":");

        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(fecha[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(fecha[1]) - 1);
        calendar.set(Calendar.YEAR, Integer.parseInt(fecha[2]));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hora[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(hora[1]));
        calendar.set(Calendar.SECOND, 0);

        long startTime = calendar.getTimeInMillis();
        long now = System.currentTimeMillis();
        if (startTime < now) {
            startTime = now + 5000; // para pruebas rápidas
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                startTime,
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
