package fycsb.gky.tb_autosign.ui;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

/**
 * Created by codefu on 2014/12/27.
 */
public abstract class BaseActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public void log(Class<?> c,String log) {
        Log.i(c.getName(),log);
    }
}
