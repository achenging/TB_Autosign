package fycsb.gky.tb_autosign.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import fycsb.gky.tb_autosign.R;
import fycsb.gky.tb_autosign.api.TieBaApi;
import fycsb.gky.tb_autosign.ui.fragment.OneByOneSignFragment;
import fycsb.gky.tb_autosign.ui.fragment.SevenLevelSignFragment;

public class AutoSignActivity extends BaseActivity {
    private static final String SEVEN_LEVEL_TAG = "SEVEN_LEVEL";
    private static final String ONE_BY_ONE_TAG  = "ONE_BY_ONE";
    private String currentTag;
    private String username;
    private String tbs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_sign);
        username = getIntent().getStringExtra(TieBaApi.NAME);
        tbs = getIntent().getStringExtra(TieBaApi.TBS);
        SevenLevelSignFragment sevenLevelSignFragment = SevenLevelSignFragment.newInstance(username, tbs);
        getSupportFragmentManager().beginTransaction().add(R.id.container, sevenLevelSignFragment, SEVEN_LEVEL_TAG).commit();
        currentTag = SEVEN_LEVEL_TAG;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_auto_sign, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.chang_mode:
                if (currentTag.equals(SEVEN_LEVEL_TAG)) {
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag(ONE_BY_ONE_TAG);
                    if (fragment == null)
                        fragment = OneByOneSignFragment.newInstance(username, tbs);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                    currentTag = ONE_BY_ONE_TAG;
                    setTitle(R.string.title_fragment_all_sign);
                } else {
                    setTitle(R.string.title_fragment_some_sign);
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag(SEVEN_LEVEL_TAG);
                    if (fragment == null)
                        fragment = SevenLevelSignFragment.newInstance(username, tbs);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                    currentTag = SEVEN_LEVEL_TAG;
                }
                break;
            case R.id.chang_user:

                break;
            case R.id.about:
                Intent aboutIntent = new Intent(AutoSignActivity.this,AboutActivity.class);
                aboutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(aboutIntent);
                break;
            case R.id.exit:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
