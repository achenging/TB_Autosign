package fycsb.gky.tb_autosign.ui;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import fycsb.gky.tb_autosign.R;
import fycsb.gky.tb_autosign.api.TieBaApi;

public class AutoSignActivity extends ActionBarActivity {
    private TextView mUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_sign);



    }
    private void init() {
        String username = getIntent().getStringExtra(TieBaApi.NAME);
        mUsername = (TextView) findViewById(R.id.username);
        mUsername.setText(username);
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.config)  + username,MODE_PRIVATE);
        String dbuss = sharedPreferences.getString(TieBaApi.DBUSS,null);
        String id = sharedPreferences.getString(TieBaApi.ID,null);
        if (dbuss != null && id != null) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_auto_sign, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
