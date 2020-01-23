package com.example.tarea4unidad1_moviles;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.net.URLEncoder;

public class ReceptorLlamadas extends BroadcastReceiver {
    public static final String NOTIFICATION_CHANNEL_ID = "1000";
    public static final String NOTIFICATION_CHANNEL_NAME = "UNJBG";

    @Override
    public void onReceive(Context context, Intent intent) {
        String estado = "", numero = "";
        Bundle extras = intent.getExtras();

        if (extras != null) {
            estado = extras.getString(TelephonyManager.EXTRA_STATE);
            if (estado.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                numero = extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                String info = "Ultima llamada recibida de " + ":"+ " " + numero;
                Log.d("ReceptorAnuncio", info + " intent=" + intent);

                //intent

                Intent IntLlamar = new Intent(Intent.ACTION_DIAL,
                        Uri.parse("Cel:"+numero));
                PendingIntent IntReturnLLamada = PendingIntent.getActivity(context,0, IntLlamar,0);

                PackageManager packageManager = context.getPackageManager();
                Intent i = new Intent(Intent.ACTION_VIEW);
                PendingIntent IntentMensaje = PendingIntent.getActivity(context,0, i,0);
                try {
                    String url = "https://api.whatsapp.com/send?phone=" + "51"+numero + "&text="
                            + URLEncoder.encode("Buen día, tenia una duda del curso ... ", "UTF-8");
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    if (i.resolveActivity(packageManager) != null) {
                        context.startActivity(i);
                    }
                    else {
                        Toast.makeText(context, "No tiene Whatsapp porfavor instale la app"
                                , Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

//notificacion
                NotificationCompat.Builder notificacion = new NotificationCompat.Builder(context).setContentTitle("Informacion de llamadas ")
                        .setContentText(info)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, Servicio.class), 0))
                        .addAction(android.R.drawable.ic_menu_call, "MENSAJE WHATSSAP", IntentMensaje)
                        .addAction(android.R.drawable.ic_menu_call, "LLAMAR", IntReturnLLamada);
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
                    notificationChannel.enableLights(true);
                    notificationChannel.setLightColor(R.color.colorAccent);
                    notificationManager.createNotificationChannel(notificationChannel);
                    notificacion.setChannelId(NOTIFICATION_CHANNEL_ID);
                }
                notificationManager.notify(1, notificacion.build());
            }
        }
    }
}

