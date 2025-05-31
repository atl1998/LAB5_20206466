package com.example.lab5_20206466;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import android.os.Build;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String titulo = intent.getStringExtra("titulo");
        String mensaje = intent.getStringExtra("mensaje");
        String canalId = intent.getStringExtra("canalId");
        int idNotificacion = intent.getIntExtra("id", 0);
        int icono = intent.getIntExtra("icono", R.drawable.ic_launcher_foreground);

        // Crear canal si es necesario (API >= 26)
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

        // Crear notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, canalId)
                .setSmallIcon(icono)
                .setContentTitle(titulo)
                .setContentText(mensaje)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(idNotificacion, builder.build());
    }
}

