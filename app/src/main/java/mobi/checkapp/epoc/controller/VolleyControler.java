package mobi.checkapp.epoc.controller;

import android.app.Application;
import android.content.Context;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import de.duenndns.ssl.MemorizingTrustManager;
import mobi.checkapp.epoc.chat.data.YaximConfiguration;

/**
 * Created by allancalderon on 30/05/16.
 */
public class VolleyControler extends Application {

    private final String TAG = this.getClass().getName();
    private RequestQueue mRequestQueue;
    private static VolleyControler mInstance;

    public static final String XMPP_IDENTITY_NAME = "chat_respira";
    public static final String XMPP_IDENTITY_TYPE = "phone";

    // MTM is needed globally for both the backend (connect)
    // and the frontend (display dialog)
    public MemorizingTrustManager mMTM;

    private YaximConfiguration mConfig;

    public VolleyControler() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        //Chat configuration
        mMTM = new MemorizingTrustManager(this);
        mConfig = new YaximConfiguration(PreferenceManager.getDefaultSharedPreferences(this));
    }

    public static VolleyControler getApp(Context ctx) {            //Chat configuration
        return (VolleyControler)ctx.getApplicationContext();       //Chat configuration

    }

    public static YaximConfiguration getConfig(Context ctx) {      //Chat configuration
        return getApp(ctx).mConfig;                                //Chat configuration
    }

    public static synchronized VolleyControler getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}