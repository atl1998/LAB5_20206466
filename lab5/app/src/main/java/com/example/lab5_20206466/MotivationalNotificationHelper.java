package com.example.lab5_20206466;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class MotivationalNotificationHelper {

    public static void programar(Context context, String mensaje, int frecuenciaHoras) {
        String canalId = "motivacional";
        int idNotificacion = 99999;

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("titulo", "Motivación");
        intent.putExtra("mensaje", mensaje);
        intent.putExtra("canalId", canalId);
        intent.putExtra("icono", R.drawable.ic_mensaje);
        intent.putExtra("id", idNotificacion);
        intent.putExtra("esMotivacional", true); // <- Marca que es motivacional
        intent.putExtra("frecuenciaHoras", frecuenciaHoras);
        intent.putExtra("mensajeMotivacional", mensaje);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                idNotificacion,
                intent,
                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance(); // ahora mismo

        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                pendingIntent
        );
    }
}
