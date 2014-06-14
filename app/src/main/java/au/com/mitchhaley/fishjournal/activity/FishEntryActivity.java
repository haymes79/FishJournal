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
import au.com.mitchhaley.fishjournal.db.FishEntryContentHelper;
import au.com.mitchhaley.fishjournal.db.MediaEntryContentHelper;
import au.com.mitchhaley.fishjournal.fragment.FishConditionsFragment;
import au.com.mitchhaley.fishjournal.fragment.FishDetailsFragment;
import au.com.mitchhaley.fishjournal.fragment.FishLocationFragment;
import au.com.mitchhaley.fishjournal.fragment.FishSpeciesFragment;
import au.com.mitchhaley.fishjournal.fragment.FishTypeListFragment;
import au.com.mitchhaley.fishjournal.fragment.MediaFragment;
import au.com.mitchhaley.fishjournal.nav.AbstractNavDrawerActivity;
import au.com.mitchhaley.fishjournal.nav.FishJournalNavDrawerActivity;
import au.com.mitchhaley.fishjournal.nav.NavDrawerActivityConfiguration;
import au.com.mitchhaley.fishjournal.nav.NavDrawerAdapter;
import au.com.mitchhaley.fishjournal.nav.NavDrawerItem;
import au.com.mitchhaley.fishjournal.nav.NavMenuItem;
import au.com.mitchhaley.fishjournal.nav.NavMenuSection;

public class FishEntryActivity extends FishJournalNavDrawerActivity {

    private SectionFragmentPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    private FishDetailsFragment fishDetailsFragment;
    
    private FishSpeciesFragment fishSpeciesFragment;
    
    private FishConditionsFragment fishConditionsFragment;

    private FishLocationFragment fishLocationFragment;

    private MediaFragment mediaFragment;

    private Uri fishEntry;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            extras = new Bundle();
        } else if (extras.containsKey(FishEntryContentProvider.CONTENT_ITEM_TYPE)){
            fishEntry =(Uri) extras.get(FishEntryContentProvider.CONTENT_ITEM_TYPE);
        }

        // Create the adapter that will return a fragment for each of the sections of the app.
        mSectionsPagerAdapter = new SectionFragmentPagerAdapter(getSupportFragmentManager(), getApplicationContext());

        mSectionsPagerAdapter.addSection("Details", FishDetailsFragment.class,extras);
        mSectionsPagerAdapter.addSection("Species", FishSpeciesFragment.class, extras);
        mSectionsPagerAdapter.addSection("Conditions", FishConditionsFragment.class, extras);
        mSectionsPagerAdapter.addSection("Media", MediaFragment.class, extras);
        mSectionsPagerAdapter.addSection("Location", FishLocationFragment.class, extras);

        
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

          if (getFishEntry() == null) {
              fishEntry = FishEntryContentHelper.create(this);
              Toast.makeText(this, "Fish Entry Created", Toast.LENGTH_LONG).show();
          } else {
              int numRowsImpacted = FishEntryContentHelper.update(this);
              Toast.makeText(this, "Fish Entry Updated", Toast.LENGTH_LONG).show();
          }

          //Add new images.
          if (fishEntry != null && getMediaFragment() != null) {
              MediaEntryContentHelper.create(this, getMediaFragment().getNewImages(), FishEntryContentProvider.CONTENT_ITEM_TYPE, Integer.parseInt(fishEntry.getLastPathSegment()));
          }

          Intent i = new Intent(this, FishListActivity.class);
          startActivity(i);

    	  return true;
      case R.id.delete:
    	  return true;
      default:
    	  return super.onOptionsItemSelected(item);
      }
    } 

	public FishConditionsFragment getFishConditionsFragment() {
		return fishConditionsFragment;
	}

	public void setFishConditionsFragment(
			FishConditionsFragment fishConditionsFragment) {
		this.fishConditionsFragment = fishConditionsFragment;
	}

	public void setFishDetailsFragment(FishDetailsFragment fishDetailsFragment) {
		this.fishDetailsFragment = fishDetailsFragment;
	}

	public FishDetailsFragment getFishDetailsFragment() {
		return fishDetailsFragment;
	}

	public FishSpeciesFragment getFishSpeciesFragment() {
		return fishSpeciesFragment;
	}

	public void setFishSpeciesFragment(FishSpeciesFragment fishSpeciesFragment) {
		this.fishSpeciesFragment = fishSpeciesFragment;
	}

    public FishLocationFragment getFishLocationFragment() {
        return fishLocationFragment;
    }

    public void setFishLocationFragment(FishLocationFragment fishLocationFragment) {
        this.fishLocationFragment = fishLocationFragment;
    }

    public MediaFragment getMediaFragment() {
        return mediaFragment;
    }

    public void setMediaFragment(MediaFragment mediaFragment) {
        this.mediaFragment = mediaFragment;
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
        navDrawerActivityConfiguration.setMainLayout(R.layout.fishentry_main);
        navDrawerActivityConfiguration.setDrawerLayoutId(R.id.drawer_layout);
        navDrawerActivityConfiguration.setLeftDrawerId(R.id.left_drawer);
        navDrawerActivityConfiguration.setNavItems(menu);
//            navDrawerActivityConfiguration.setDrawerShadow(R.drawable.drawer_shadow);
        navDrawerActivityConfiguration.setDrawerOpenDesc(R.string.drawer_open);
        navDrawerActivityConfiguration.setDrawerCloseDesc(R.string.drawer_close);
        navDrawerActivityConfiguration.setBaseAdapter(
                new NavDrawerAdapter(this, R.layout.navdrawer_item, menu ));
        return navDrawerActivityConfiguration;
    }

	@Override
	public int getMainLayout() {
		return R.layout.fishentry_main;
	}

    public Uri getFishEntry() {
        return fishEntry;
    }
}
