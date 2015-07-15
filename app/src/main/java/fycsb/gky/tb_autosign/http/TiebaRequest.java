package fycsb.gky.tb_autosign.http;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * Created by codefu.
 */
public class TiebaRequest extends Request<String> {
    private final Response.Listener<String> mListener;

    /**
     * Creates a new request with the given method.
     *
     * @param method        the request {@link Method} to use
     * @param url           HOST_URL to fetch the string at
     * @param listener      Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     */
    public TiebaRequest(int method, String url, Response.Listener<String> listener,
                        Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
    }

    /**
     * Creates a new GET request.
     *
     * @param url           HOST_URL to fetch the string at
     * @param listener      Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     */
    public TiebaRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        this(Method.GET, url, listener, errorListener);
    }

    @Override
    protected Map<String, String> getParams() {
        return super.getParams();
    }

    @Override
    public RetryPolicy getRetryPolicy() {
        RetryPolicy retryPolicy = new DefaultRetryPolicy(TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        return retryPolicy;
    }
    private static final int TIMEOUT = 5;
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Accept-Encoding", "gzip");
        headerMap.put("User-Agent", "BaiduTieba for Android 5.1.1");
        headerMap.put("Cookie", "ka=open");
        headerMap.put("client_user_token", "889014874");

        return headerMap;
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed = getRealString(response.data);
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));

    }

    private int getShort(byte[] data) {
        return (data[0] << 8) | data[1] & 0xFF;
    }

    private String getRealString(byte[] data) {
        byte[] h = new byte[2];
        h[0] = data[0];
        h[1] = data[1];
        int head = getShort(h);
        boolean t = head == 0x1f8b;
        InputStream in;
        StringBuilder sb = new StringBuilder();
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            if (t) {
                in = new GZIPInputStream(bis);
            } else {
                in = bis;
            }
            BufferedReader r = new BufferedReader(new InputStreamReader(in), 1000);
            for (String line = r.readLine(); line != null; line = r.readLine()) {
                sb.append(line);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
