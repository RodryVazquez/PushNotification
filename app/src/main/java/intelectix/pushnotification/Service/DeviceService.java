package intelectix.pushnotification.Service;


import android.app.ProgressDialog;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

import intelectix.pushnotification.Models.DeviceModel;

public class DeviceService {

    private Context context;
    private static final String URL_ADD_DEVICE = "addDevice";
    private TextView textView;

    public DeviceService(Context context, TextView textView) {
        this.context = context;
        this.textView = textView;
    }

    public void sendRegistrationId(String deviceId) {
        final String contentType = "application/json";
        final JSONObject jsonObject = new JSONObject();
        StringEntity stringEntity = null;
        try {
            jsonObject.put("deviceId", 0);
            jsonObject.put("senderId", deviceId);
            jsonObject.put("active", true);
            stringEntity = new StringEntity(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Realizamos el post al servidor
        ServiceBase.PostStringEntity(context, URL_ADD_DEVICE, stringEntity, contentType, new JsonHttpResponseHandler() {

            ProgressDialog dialog;

            @Override
            public void onStart() {
                super.onStart();
                dialog = new ProgressDialog(context);
                dialog.setCancelable(false);
                dialog.setMessage("Registrando dispositivo");
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (statusCode == 200) {
                    textView.setText("Dispositivo registrado con exito");
                    //Actualizamos la tabla del dispositivo
                    List<DeviceModel> deviceModels = DeviceModel.listAll(DeviceModel.class);
                    for (DeviceModel model:deviceModels
                            ) {
                        model.setActive(true);
                        model.save();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog = null;
                }
            }
        });


    }


}
