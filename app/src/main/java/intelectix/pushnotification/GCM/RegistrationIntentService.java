package intelectix.pushnotification.GCM;

import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import intelectix.pushnotification.Models.DeviceModel;
import intelectix.pushnotification.R;
import intelectix.pushnotification.Service.DeviceService;
import intelectix.pushnotification.Service.ServiceBase;


public class RegistrationIntentService extends IntentService {

    private static final String TAG = "ResgistrationIntentService";
    private static final String[] TOPICS = {"global"};


    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        try {

            InstanceID instanceID = InstanceID.getInstance(this);
            String id = instanceID.getId();
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.i(TAG, "GCM registration token : " + token);

            saveRegistrationToken(token);
            subscribeTopics(token);
            //Indicamos que el token fue enviado al servidor
            sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true).apply();
        } catch (Exception ex) {
            Log.d(TAG, "Failed to complete token refresh", ex);
            //Si ocurre una excepcion al generar el token o la informacion de registro
            sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false).apply();
        }
        //Notificamos que el registro fue completado
        Intent registrationComplete = new Intent(QuickstartPreferences.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    //Enviamos el token al servidor
    private void saveRegistrationToken(String token) {
        DeviceModel deviceModel = new DeviceModel(token,false);
        deviceModel.save();
    }

    //Nos suscribimos a cualquier topic de GCM(Envio a multiples dispositivos)
    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
}
