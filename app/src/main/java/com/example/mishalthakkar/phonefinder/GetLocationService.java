package com.example.mishalthakkar.phonefinder;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.concurrent.Executor;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.example.mishalthakkar.phonefinder.MainActivity.passcode;

public class GetLocationService extends Service {

    private Location mLocation;
    private FusedLocationProviderClient client;
    private static final int SEND_SMS_PERMISSION_CODE = 111;
    String message;

    private Bundle bundle;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public GetLocationService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        bundle = intent.getBundleExtra("BUNDLE");
        client = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        final MediaPlayer mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.ringtone);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("PASSCODE", Context.MODE_PRIVATE);
        final String passcode = sharedPreferences.getString("passcode", "");
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    mLocation = location;
                    getLocation(bundle, getApplicationContext(), passcode, mPlayer);
                }
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    void getLocation(Bundle bundle, Context context, String passcode, MediaPlayer mPlayer){
        if(mLocation!=null){
            if (bundle != null) {
                Object[] pdus = (Object[])bundle.get("pdus");
                for(int i=0;i<pdus.length;i++){
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

                    String mobilenumber = smsMessage.getDisplayOriginatingAddress();
                    //Check the sender to filter messages which we require to read
                    String messageBody = smsMessage.getMessageBody();
                    if (passcode.equals(messageBody))
                    {
                        sendSMS(mobilenumber);
                        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
                        assert am != null;
                        if(am.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE || am.getRingerMode() == AudioManager.RINGER_MODE_SILENT || am.getRingerMode() == AudioManager.RINGER_MODE_NORMAL)
                        {
                            am.setStreamVolume(
                                    AudioManager.STREAM_MUSIC,
                                    am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                                    0);
                            Toast.makeText(context,"passcode matched." + mLocation, Toast.LENGTH_SHORT).show();
                            am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                            mPlayer.start();
                        } else if (1 == Settings.System.getInt(context.getContentResolver(), "vibrate_when_ringing", 0)){
                            //vibrate on
                        }
                        //Pass the message text to interface

                    }else{
                        Toast.makeText(context, "passcode match failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }
    }

    void sendSMS(String mobilenumber){

        Double lat = mLocation.getLatitude();
        Double lng = mLocation.getLongitude();
        String message = "http://maps.google.com?q="+lat+","+lng;
        //MainActivity.sendSMS();
        //textView.setText(location.toString());
        message = "http://maps.google.com/?q="+lat+","+lng;
//        SmsManager smsManager = SmsManager.getDefault();
        //Bundle bundle = new Bundle();
        //bundle.putString("Message",message);
        Intent intent = new Intent(GetLocationService.this,MainActivity.class);
        intent.putExtra("SERVICE",true);
intent.putExtra("msg", message);
intent.putExtra("mobile",mobilenumber);
        //intent.putExtra("Bundle",bundle);
        startActivity(intent);
//        smsManager.sendTextMessage("9909677576", null, message, null, null);
        Toast.makeText(getApplicationContext(), "SMS sent.",
                    Toast.LENGTH_LONG).show();


//        smsManager.sendTextMessage("9909677576",null,message,null,null);
    }

    }