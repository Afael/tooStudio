package com.desktopip.exploriztic.tootanium.utilities;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.interfaces.IRequestResult;
import com.desktopip.exploriztic.tootanium.volley.TokenGenerate;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Jayus on 05/07/2018.
 */

public class MySingleton {

    private static MySingleton mInstance;
    private RequestQueue requestQueue;
    private static Context context;

    private static SessionManager sessionManager;
    private static HashMap<String, String> user;
    private static String urlAPI, uid, userId;
    private static String baseUrl;

    private MySingleton(Context context) {
        this.context = context;
        requestQueue = getRequestQueue();

        //sessionManager = new SessionManager(context);
        //user = sessionManager.getUserDetails();
        // Base url API
        //urlAPI = user.get(SessionManager.BASE_URL_API);
        //baseUrl = user.get(SessionManager.BASE_URL);

    }

    // Let's assume your server app is hosting inside a server machine
    // which has a server certificate in which "Issued to" is "localhost",for example.
    // Then, inside verify method you can verify "localhost".
    // If not, you can temporarily return true
//    private HostnameVerifier getHostnameVerifier() {
//        return new HostnameVerifier() {
//            @Override
//            public boolean verify(String hostname, SSLSession session) {
//                //return true; // verify always returns true, which could cause insecure network traffic due to trusting TLS/SSL server certificates for wrong hostnames
//                HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
//                return hv.verify(baseUrl, session);
//            }
//        };
//    }
//
//    private TrustManager[] getWrappedTrustManagers(TrustManager[] trustManagers) {
//        final X509TrustManager originalTrustManager = (X509TrustManager) trustManagers[0];
//        return new TrustManager[]{
//                new X509TrustManager() {
//                    public X509Certificate[] getAcceptedIssuers() {
//                        return originalTrustManager.getAcceptedIssuers();
//                    }
//
//                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
//                        try {
//                            if (certs != null && certs.length > 0){
//                                certs[0].checkValidity();
//                            } else {
//                                originalTrustManager.checkClientTrusted(certs, authType);
//                            }
//                        } catch (CertificateException e) {
//                            Log.w("checkClientTrusted", e.toString());
//                        }
//                    }
//
//                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
//                        try {
//                            if (certs != null && certs.length > 0){
//                                certs[0].checkValidity();
//                            } else {
//                                originalTrustManager.checkServerTrusted(certs, authType);
//                            }
//                        } catch (CertificateException e) {
//                            Log.w("checkServerTrusted", e.toString());
//                        }
//                    }
//                }
//        };
//    }
//
//    private SSLSocketFactory getSSLSocketFactory(){
//        try {
//            CertificateFactory cf = CertificateFactory.getInstance("X.509");
//            InputStream caInput = context.getResources().openRawResource(R.raw.my_cert); // this cert file stored in \app\src\main\res\raw folder path
//
//            Certificate ca = cf.generateCertificate(caInput);
//            caInput.close();
//
//            String keyStoreType = KeyStore.getDefaultType();
//            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
//            keyStore.load(null, null);
//            keyStore.setCertificateEntry("ca", ca);
//
//            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
//            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
//            tmf.init(keyStore);
//
//            TrustManager[] wrappedTrustManagers = getWrappedTrustManagers(tmf.getTrustManagers());
//
//            SSLContext sslContext = SSLContext.getInstance("TLS");
//            sslContext.init(null, wrappedTrustManagers, null);
//            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
//
//            return sslContext.getSocketFactory();
//
//        } catch (CertificateException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//        }
//
//        return  null;
//    }
//
//    private SSLSocketFactory getSocketFactory() {
//
//        CertificateFactory cf = null;
//        try {
//            cf = CertificateFactory.getInstance("X.509");
//            InputStream caInput = context.getResources().openRawResource(R.raw.my_cert);
//            Certificate ca;
//            try {
//                ca = cf.generateCertificate(caInput);
//                Log.e("CERT", "ca=" + ((X509Certificate) ca).getSubjectDN());
//            } finally {
//                caInput.close();
//            }
//
//
//            String keyStoreType = KeyStore.getDefaultType();
//            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
//            keyStore.load(null, null);
//            keyStore.setCertificateEntry("ca", ca);
//
//
//            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
//            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
//            tmf.init(keyStore);
//
//
//            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
//                @Override
//                public boolean verify(String hostname, SSLSession session) {
//
//                    Log.e("CipherUsed", session.getCipherSuite());
//                    return hostname.compareTo(baseUrl)==0; //The Hostname of your server
//
//                }
//            };
//
//
//            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
//            SSLContext context = null;
//            context = SSLContext.getInstance("TLS");
//
//            context.init(null, tmf.getTrustManagers(), null);
//            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
//
//            SSLSocketFactory sf = context.getSocketFactory();
//
//
//            return sf;
//
//        } catch (CertificateException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//        }
//
//        return  null;
//    }

    public RequestQueue getRequestQueue() {
        if(requestQueue == null) {

//            HurlStack hurlStack = new HurlStack() {
//                @Override
//                protected HttpURLConnection createConnection(URL url) throws IOException {
//                    HttpsURLConnection httpsURLConnection = (HttpsURLConnection) super.createConnection(url);
//                    try {
//                        httpsURLConnection.setSSLSocketFactory(getSSLSocketFactory());
//                        httpsURLConnection.setHostnameVerifier(getHostnameVerifier());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    return httpsURLConnection;
//                }
//            };

            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized MySingleton getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new MySingleton(context);
        }
        return mInstance;
    }

    public<T> void addToRequestQueue(Request<T> request) {
        requestQueue.add(request);
    }

}
