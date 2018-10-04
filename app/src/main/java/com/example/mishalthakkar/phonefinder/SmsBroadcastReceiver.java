package com.example.mishalthakkar.phonefinder;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class SmsBroadcastReceiver extends BroadcastReceiver {
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SMSBroadcastReceiver";
    private Location mLocation;
    private FusedLocationProviderClient client;
    private GetLocationService mGetLocationService;

    //String passcodes = MainActivity.passcode;
    @Override
    public void onReceive(final Context context, final Intent intent) {
//        MainActivity.sendSMS();

        client = LocationServices.getFusedLocationProviderClient(context);
        //Intent intent = getIntent();
        final MediaPlayer mPlayer = MediaPlayer.create(context, R.raw.ringtone);
        SharedPreferences sharedPreferences = context.getSharedPreferences("PASSCODE", Context.MODE_PRIVATE);
        final String passcode = sharedPreferences.getString("passcode", "");
        Log.d(TAG, "onReceive: " + passcode);

        final Bundle bundle = intent.getExtras();
        Intent intent1 = new Intent(context, GetLocationService.class);
        intent1.putExtra("BUNDLE", bundle);
        context.startService(intent1);

//        client.getLastLocation().addOnSuccessListener(context, new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                if (location != null) {
//                    mLocation = location;
//                    getLocation(bundle, context, passcode, mPlayer);
//                }
//            }
//        });

    }

//    void getLocation(Bundle bundle, Context context, String passcode, MediaPlayer mPlayer){
//        if(mLocation!=null){
//            if (bundle != null) {
//                Object[] pdus = (Object[])bundle.get("pdus");
//                for(int i=0;i<pdus.length;i++){
//                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
//
//                    String sender = smsMessage.getDisplayOriginatingAddress();
//                    //Check the sender to filter messages which we require to read
//                    String messageBody = smsMessage.getMessageBody();
//                    if (passcode.equals(messageBody))
//                    {
//                        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
//                        assert am != null;
//                        if(am.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE || am.getRingerMode() == AudioManager.RINGER_MODE_SILENT || am.getRingerMode() == AudioManager.RINGER_MODE_NORMAL){
//                            Toast.makeText(context,"passcode matched." + mLocation, Toast.LENGTH_SHORT).show();
//                            am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
//                            mPlayer.start();
//                        } else if (1 == Settings.System.getInt(context.getContentResolver(), "vibrate_when_ringing", 0)){
//                            //vibrate on
//                        }
//                        //Pass the message text to interface
//
//                    }else{
//                        Toast.makeText(context, "passcode match failed.", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//
//        }
//    }

}
