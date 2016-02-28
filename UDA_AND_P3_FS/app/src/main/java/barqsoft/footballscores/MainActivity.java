package barqsoft.footballscores;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {

    public static int mSelectedMatchId;
    public static int mCurrentFragment = 2;
    private PagerFragment mPagerFragment;

    private static final String PAGER_TAG = "Pager_Current";
    private static final String MATCH_TAG = "Match_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            mPagerFragment = new PagerFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, mPagerFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about)
        {
            Intent start_about = new Intent(this,AboutActivity.class);
            startActivity(start_about);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putInt(PAGER_TAG, mPagerFragment.mPagerHandler.getCurrentItem());
        outState.putInt(MATCH_TAG, mSelectedMatchId);
        getSupportFragmentManager().putFragment(outState, "mPagerFragment", mPagerFragment);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        mCurrentFragment = savedInstanceState.getInt(PAGER_TAG);
        mSelectedMatchId = savedInstanceState.getInt(MATCH_TAG);
        mPagerFragment = (PagerFragment) getSupportFragmentManager().getFragment(savedInstanceState, "mPagerFragment");
        super.onRestoreInstanceState(savedInstanceState);
    }

}
