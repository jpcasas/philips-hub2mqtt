package dev.jpcasas.clients;


import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class ClientTools {
    
    public static Client getClient() throws KeyManagementException, NoSuchAlgorithmException{
        return  ClientBuilder.newBuilder().sslContext(ClientTools.getSSLContext()).hostnameVerifier((s1, s2) -> true).build();
    }

    public static  SSLContext getSSLContext() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslcontext  = SSLContext.getInstance("TLS");

        sslcontext.init(null, new TrustManager[] { new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        } }, new java.security.SecureRandom());
        return sslcontext;
    }
}
