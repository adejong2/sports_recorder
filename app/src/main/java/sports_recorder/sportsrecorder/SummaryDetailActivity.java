package sports_recorder.sportsrecorder;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

// Code from class contact example:
public class SummaryDetailActivity extends Activity implements ListView.OnItemClickListener {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;

    private String items[] = new String[] { "Data Entry","Summary"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Disable original title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_summary_detail);

        //Create new Action Bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);

        getActionBar().setElevation(10);

        toolbar.setNavigationIcon(R.drawable.ic_menu_gray757575_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mDrawerLayout.openDrawer(mDrawerListView);
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerListView = (ListView) findViewById(R.id.drawer_list_view);
        mDrawerListView.setOnItemClickListener(this);


        ListAdapter listAdapter = new ArrayAdapter<>(this, R.layout.nav_drawer_list_item, items);
        mDrawerListView.setAdapter(listAdapter);

        FragmentManager fragmentManager = getFragmentManager();
        SummaryDetailFragment fragment = (SummaryDetailFragment) fragmentManager.findFragmentById(R.id.fragment_summary_detail);
        if (fragment != null) {
            int position = 0;
            position = getIntent().getIntExtra("POSITION",0);
            fragment.loadPosition(position);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (items[position].equals("Data Entry")) {
            finish();
        }

        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.summary_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_email:
                // User chose the "Email" item, show the app settings UI...
//                System.out.println("Email button pressed");
                Toast.makeText(this, R.string.action_email, Toast.LENGTH_SHORT).show();
                FragmentManager fragmentManager = getFragmentManager();
                SummaryDetailFragment fragment = (SummaryDetailFragment) fragmentManager.findFragmentById(R.id.fragment_summary_detail);
                if (fragment != null) {
                    int position = 0;
                    position = getIntent().getIntExtra("POSITION",0);
//                    fragment.loadPosition(position);
                    fragment.sendEmail();
                }

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

}
