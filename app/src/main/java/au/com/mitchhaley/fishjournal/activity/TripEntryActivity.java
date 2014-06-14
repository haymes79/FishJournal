package au.com.mitchhaley.fishjournal.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import au.com.mitchhaley.fishjournal.R;
import au.com.mitchhaley.fishjournal.adapter.SectionFragmentPagerAdapter;
import au.com.mitchhaley.fishjournal.contentprovider.FishEntryContentProvider;
import au.com.mitchhaley.fishjournal.contentprovider.TripEntryContentProvider;
import au.com.mitchhaley.fishjournal.db.ContactContentHelper;
import au.com.mitchhaley.fishjournal.db.FishEntryContentHelper;
import au.com.mitchhaley.fishjournal.db.MediaEntryContentHelper;
import au.com.mitchhaley.fishjournal.db.TripEntryContentHelper;
import au.com.mitchhaley.fishjournal.fragment.ContactsFragment;
import au.com.mitchhaley.fishjournal.fragment.FishConditionsFragment;
import au.com.mitchhaley.fishjournal.fragment.FishDetailsFragment;
import au.com.mitchhaley.fishjournal.fragment.FishTypeListFragment;
import au.com.mitchhaley.fishjournal.fragment.TripDetailsFragment;
import au.com.mitchhaley.fishjournal.fragment.TripLocationFragment;
import au.com.mitchhaley.fishjournal.nav.FishJournalNavDrawerActivity;
import au.com.mitchhaley.fishjournal.nav.NavDrawerActivityConfiguration;
import au.com.mitchhaley.fishjournal.nav.NavDrawerAdapter;
import au.com.mitchhaley.fishjournal.nav.NavDrawerItem;
import au.com.mitchhaley.fishjournal.nav.NavMenuItem;
import au.com.mitchhaley.fishjournal.nav.NavMenuSection;

public class TripEntryActivity extends FishJournalNavDrawerActivity {

    private SectionFragmentPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    private TripDetailsFragment tripDetailsFragment;
    private TripLocationFragment tripLocationFragment;

    private ContactsFragment contactsFragment;

    private Uri tripEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            extras = new Bundle();
        } else if (extras.containsKey(TripEntryContentProvider.TRIP_CONTENT_ITEM_TYPE)){
            tripEntry =(Uri) extras.get(TripEntryContentProvider.TRIP_CONTENT_ITEM_TYPE);
        }

        // Create the adapter that will return a fragment for each of the sections of the app.
        mSectionsPagerAdapter = new SectionFragmentPagerAdapter(getSupportFragmentManager(), getApplicationContext());

        mSectionsPagerAdapter.addSection("Details", TripDetailsFragment.class,extras);
        mSectionsPagerAdapter.addSection("Location", TripLocationFragment.class,extras);
        mSectionsPagerAdapter.addSection("Anglers", ContactsFragment.class,extras);


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        PagerTitleStrip titleStrip = (PagerTitleStrip) findViewById(R.id.pager_title_strip);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.main, menu);
      return true;
    } 
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
      case R.id.save:
          if (getTripEntry() == null) {
              Uri uri = TripEntryContentHelper.create(this);

              ContactContentHelper.create(this, getContactsFragment().getSelectedContacts(), uri.getLastPathSegment());
              Toast.makeText(this, "Trip Entry Created", Toast.LENGTH_LONG).show();
          } else {
            int numRowsImpacted = TripEntryContentHelper.update(this);

            Toast.makeText(this, "Trip Entry Updated", Toast.LENGTH_LONG).show();
          }

          Intent i = new Intent(this, TripListActivity.class);
          startActivity(i);

    	  return true;
      case R.id.delete:
    	  return true;
      default:
    	  return super.onOptionsItemSelected(item);
      }
    } 

    @Override
    protected NavDrawerActivityConfiguration getNavDrawerConfiguration() {

        NavDrawerItem[] menu = new NavDrawerItem[] {
                NavMenuSection.create(100, "Fish"),
                NavMenuItem.create(101, "Fish Entry", R.color.fishEntryBackground, false, this),
                NavMenuItem.create(102, "Fish List", R.color.fishListBackground, false, this),
                NavMenuSection.create(103, "Trip"),
                NavMenuItem.create(104, "Trip Entry", R.color.tripEntryBackground, false, this),
                NavMenuItem.create(105, "Trip List", R.color.tripListBackground, false, this)};

        NavDrawerActivityConfiguration navDrawerActivityConfiguration = new NavDrawerActivityConfiguration();
        navDrawerActivityConfiguration.setMainLayout(R.layout.tripentry_main);
        navDrawerActivityConfiguration.setDrawerLayoutId(R.id.drawer_layout);
        navDrawerActivityConfiguration.setLeftDrawerId(R.id.left_drawer);
        navDrawerActivityConfiguration.setNavItems(menu);

        navDrawerActivityConfiguration.setDrawerOpenDesc(R.string.drawer_open);
        navDrawerActivityConfiguration.setDrawerCloseDesc(R.string.drawer_close);
        navDrawerActivityConfiguration.setBaseAdapter(
                new NavDrawerAdapter(this, R.layout.navdrawer_item, menu ));
        return navDrawerActivityConfiguration;
    }

	@Override
	public int getMainLayout() {
		return R.layout.tripentry_main;
	}

    public TripDetailsFragment getTripDetailsFragment() {
        return tripDetailsFragment;
    }

    public void setTripDetailsFragment(TripDetailsFragment tripDetailsFragment) {
        this.tripDetailsFragment = tripDetailsFragment;
    }

    public void setTripLocationFragment(TripLocationFragment tripLocationFragment) {
        this.tripLocationFragment = tripLocationFragment;
    }

    public Uri getTripEntry() {
        return tripEntry;
    }

    public TripLocationFragment getTripLocationFragment() {
        return tripLocationFragment;
    }

    public ContactsFragment getContactsFragment() {
        return contactsFragment;
    }

    public void setContactsFragment(ContactsFragment contactsFragment) {
        this.contactsFragment = contactsFragment;
    }
}
