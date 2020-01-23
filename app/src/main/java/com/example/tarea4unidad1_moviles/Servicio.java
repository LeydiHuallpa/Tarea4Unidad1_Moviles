package com.example.tarea4unidad1_moviles;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class Servicio extends Service {
    MediaPlayer reproductor;
    private static final int ID_NOTIFICACION_CREAR = 1;
    public static final String NOTIFICATION_CHANNEL_ID = "1000";
    public static final String NOTIFICATION_CHANNEL_NAME = "UNJBG";

    @Override public void onCreate() {
        Toast.makeText(this,"Servicio creado",
                Toast.LENGTH_SHORT).show();
        reproductor = MediaPlayer.create(this, R.raw.musica);

    }

    @Override
    public int onStartCommand(Intent intenc, int flags, int idArranque) {
        NotificationCompat.Builder notific = new NotificationCompat.Builder(this)
                .setContentTitle("Creando Servicio")
                .setSmallIcon(R.mipmap.llamada)
                .setContentText("información adicional")
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        android.R.drawable.ic_menu_call))
                .setWhen(System.currentTimeMillis() + 1000 * 60 * 60)
                .setContentInfo("más info").setTicker("Texto en barra de estado")
                .setDefaults(Notification.DEFAULT_SOUND)

                .setDefaults(Notification.DEFAULT_VIBRATE);

        PendingIntent intencionPendiente = PendingIntent.getActivity(
                this, 0, new Intent(this, Servicio.class), 0);
        notific.setContentIntent(intencionPendiente);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(R.color.colorAccent);
            notificationManager.createNotificationChannel(notificationChannel);
            notific.setChannelId(NOTIFICATION_CHANNEL_ID);
        }
        notificationManager.notify(ID_NOTIFICACION_CREAR, notific.build());
        Toast.makeText(this, "Servicio arrancado " + idArranque, Toast.LENGTH_SHORT).show();
        reproductor.start();
        startForeground(101,notific.build() );
        return START_STICKY;
    }

    @Override public void onDestroy() {
        Toast.makeText(this,"Servicio detenido",
                Toast.LENGTH_SHORT).show();
        reproductor.stop();
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(ID_NOTIFICACION_CREAR);

    }
    @Override public IBinder onBind(Intent intencion) {
        return null;
    }
}
