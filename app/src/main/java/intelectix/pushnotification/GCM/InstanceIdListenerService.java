package intelectix.pushnotification.GCM;


import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

public class InstanceIdListenerService extends InstanceIDListenerService {

    private static final String TAG = "InstanceIdListenerService";

    //Se ejecuta al actualizar el token (Seguridad comprometida del token anterior)
    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}
