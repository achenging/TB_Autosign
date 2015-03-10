package fycsb.gky.tb_autosign.ui;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Field;

import fycsb.gky.tb_autosign.R;

/**
 * Created by codefu on 2014/12/27.
 */
public abstract class BaseActivity extends ActionBarActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

//    private View view;
    /**
     * 更改menu背景色相关：
     * http://stackoverflow.com/questions/2719173/change-background-color-of-android-menu
     * http://stackoverflow.com/questions/12857007/error-a-factory-has-already-been-set-on-this-layoutinflater-change-background
     */
//    protected void setMenuBackground() {
//        // Ask our inflater to create the view
//        LayoutInflater layoutInflater = getLayoutInflater();
//        final LayoutInflater.Factory existingFactory = layoutInflater.getFactory();
//        try {
//            Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
//            field.setAccessible(true);
//            field.setBoolean(layoutInflater, false);
//            getLayoutInflater().setFactory(new LayoutInflater.Factory() {
//                public View onCreateView(String name, Context context, AttributeSet attrs) {
//                    if (name.equalsIgnoreCase("com.android.internal.view.menu.IconMenuItemView")) {
//                        if (existingFactory != null) {
//                            view = existingFactory.onCreateView(name, null, attrs);
//                        }
//
//                        /* The background gets refreshed each time a new item is added the options menu.
//                        * So each time Android applies the default background we need to set our own
//                        * background. This is done using a thread giving the background change as runnable
//                        * object */
//                        new Handler().post(new Runnable() {
//                            public void run() {
//                                // sets the background color
//                                if (view != null) {
//                                    view.setBackgroundResource(R.color.grey);
//                                    // sets the text color
//                                    ((TextView) view).setTextColor(Color.WHITE);
//                                }
//                            }
//                        });
//                        return view;
//                    }
//                    return null;
//                }
//
//            });
//
//        } catch (InflateException e) {
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        }
//
//    }


}
