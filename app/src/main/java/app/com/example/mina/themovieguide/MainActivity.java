package app.com.example.mina.themovieguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements MainFragment.Callback {

     private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putParcelable("Movie_Object", null);

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_container, fragment, "DFTAG")
                    .commit();


        } else {
            mTwoPane = false;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onItemSelected(Bundle detData) {
        if(!mTwoPane) {

            Intent detailsintent = new Intent(this, DetailsActivity.class);
            // detailsintent.putExtra(Intent.EXTRA_TEXT, m.getId());

            detailsintent.putExtra("Movie_Object" , detData);
            startActivity(detailsintent);
        }
        else
        {

            DetailFragment fragment = (DetailFragment) getSupportFragmentManager().findFragmentByTag("DFTAG");
            fragment.updateFragment( detData);




        }
    }
}
