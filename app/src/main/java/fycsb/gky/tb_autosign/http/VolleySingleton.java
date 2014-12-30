package fycsb.gky.tb_autosign.http;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by codefu on 2014/12/27.
 */
public class VolleySingleton {
    private static Context         mContext;
    private static VolleySingleton mSingleton;
    private        RequestQueue    mRequestQueue;
    private        ImageLoader     mImageLoader;

    private VolleySingleton(Context context) {
        mContext = context;
        mRequestQueue = getmRequestQueue();
        mImageLoader = new ImageLoader(mRequestQueue,new ImageLoader.ImageCache() {
            LruCache<String, Bitmap> lruCache = new LruCache<>(50);

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

    public RequestQueue getmRequestQueue() {
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
