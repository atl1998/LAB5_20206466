package com.example.lab5_20206466;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import android.os.Build;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String titulo = intent.getStringExtra("titulo");
        String mensaje = intent.getStringExtra("mensaje");
        String canalId = intent.getStringExtra("canalId");
        int idNotificacion = intent.getIntExtra("id", 0);
        int icono = intent.getIntExtra("icono", R.drawable.ic_launcher_foreground);

        // Mostrar la notificación
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    canalId,
                    canalId,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Canal para notificaciones de hábitos");
            NotificationManager nm = context.getSystemService(NotificationManager.class);
            if (nm.getNotificationChannel(canalId) == null) {
                nm.createNotificationChannel(channel);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, canalId)
                .setSmallIcon(icono)
                .setContentTitle(titulo)
                .setContentText(mensaje)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(idNotificacion, builder.build());

        // Si es motivacional, volver a programarla
        if (intent.getBooleanExtra("esMotivacional", false)) {
            int frecuenciaHoras = intent.getIntExtra("frecuenciaHoras", 1);
            String nuevoMensaje = intent.getStringExtra("mensajeMotivacional");

            Calendar next = Calendar.getInstance();
            next.add(Calendar.HOUR_OF_DAY, frecuenciaHoras);

            Intent nuevoIntent = new Intent(context, AlarmReceiver.class);
            nuevoIntent.putExtra("titulo", "Motivación");
            nuevoIntent.putExtra("mensaje", nuevoMensaje);
            nuevoIntent.putExtra("canalId", canalId);
            nuevoIntent.putExtra("icono", icono);
            nuevoIntent.putExtra("id", idNotificacion);
            nuevoIntent.putExtra("esMotivacional", true);
            nuevoIntent.putExtra("frecuenciaHoras", frecuenciaHoras);
            nuevoIntent.putExtra("mensajeMotivacional", nuevoMensaje);

            PendingIntent nuevoPending = PendingIntent.getBroadcast(
                    context,
                    idNotificacion,
                    nuevoIntent,
                    PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
            );

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    next.getTimeInMillis(),
                    nuevoPending
            );
        }

        // Si es un hábito, volver a programar
        if (intent.getBooleanExtra("esHabito", false)) {
            int frecuenciaHoras = intent.getIntExtra("frecuenciaHoras", 1);
            String nombreHabito = intent.getStringExtra("nombreHabito");
            String categoria = intent.getStringExtra("categoria");

            Calendar next = Calendar.getInstance();
            next.add(Calendar.HOUR_OF_DAY, frecuenciaHoras);

            Intent nuevoIntent = new Intent(context, AlarmReceiver.class);
            nuevoIntent.putExtra("titulo", nombreHabito);
            nuevoIntent.putExtra("mensaje", "¡Hora de " + nombreHabito + "!");
            nuevoIntent.putExtra("canalId", categoria.toLowerCase());
            nuevoIntent.putExtra("icono", obtenerIcono(categoria));
            nuevoIntent.putExtra("id", nombreHabito.hashCode());
            nuevoIntent.putExtra("esHabito", true);
            nuevoIntent.putExtra("frecuenciaHoras", frecuenciaHoras);
            nuevoIntent.putExtra("nombreHabito", nombreHabito);
            nuevoIntent.putExtra("categoria", categoria);

            PendingIntent nuevoPending = PendingIntent.getBroadcast(
                    context,
                    nombreHabito.hashCode(),
                    nuevoIntent,
                    PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
            );

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    next.getTimeInMillis(),
                    nuevoPending
            );
        }
    }

    private int obtenerIcono(String categoria) {
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
