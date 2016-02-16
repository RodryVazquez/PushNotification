package intelectix.pushnotification.Service;


import android.content.Context;
import android.preference.PreferenceActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.apache.http.Header;
import org.apache.http.HttpEntity;

public class ServiceBase {

    //Cliente asincrono estatico.
    private static AsyncHttpClient client = new AsyncHttpClient();
    //Server url
    private static final String baseUrl = "http://10.144.3.31:8083/api/device/";
    public static final int SOCKET_TIMEOUT = 10 * 2000;


    /**
     * POST
     * @param context
     * @param url
     * @param params
     * @param contentType
     * @param responseHandler
     */
    public static void PostStringEntity(Context context, String url, HttpEntity params, String contentType, AsyncHttpResponseHandler responseHandler) {
        client.setTimeout(SOCKET_TIMEOUT);
        client.post(context, getAbsoluteUrl(url), params, contentType, responseHandler);
    }

    /**
     * Construye la cadena de cada peticion.
     *
     * @param relativeUrl cadena proviniente del metodo de cada servicio.
     * @return url.
     */
    private static String getAbsoluteUrl(String relativeUrl) {
        return baseUrl + relativeUrl;
    }
}
