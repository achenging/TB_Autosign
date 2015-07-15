package fycsb.gky.tb_autosign.http;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by codefu.
 */
public class VolleySingleton {
    private        Context         mContext;
    private static VolleySingleton mSingleton;
    private        RequestQueue    mRequestQueue;
    private        ImageLoader     mImageLoader;

    private VolleySingleton(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
        mImageLoader = new ImageLoader(mRequestQueue,new ImageLoader.ImageCache() {
            LruCache<String, Bitmap> lruCache = new LruCache<>(10);

            @Override
            public Bitmap getBitmap(String url) {
                return lruCache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                lruCache.put(url, bitmap);
            }
        });
    }

    public static VolleySingleton getInstance(Context context) {
        if (mSingleton == null) {
            mSingleton = new VolleySingleton(context);
        }
        return mSingleton;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addRequest(Request<T> request) {
        mRequestQueue.add(request);

    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}
