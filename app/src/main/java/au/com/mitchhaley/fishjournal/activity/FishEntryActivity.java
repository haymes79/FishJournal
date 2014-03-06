package au.com.mitchhaley.fishjournal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import au.com.mitchhaley.fishjournal.R;
import au.com.mitchhaley.fishjournal.adapter.SectionFragmentPagerAdapter;
import au.com.mitchhaley.fishjournal.db.FishEntryContentHelper;
import au.com.mitchhaley.fishjournal.fragment.FishConditionsFragment;
import au.com.mitchhaley.fishjournal.fragment.FishDetailsFragment;
import au.com.mitchhaley.fishjournal.fragment.FishTypeListFragment;
import au.com.mitchhaley.fishjournal.nav.AbstractNavDrawerActivity;
import au.com.mitchhaley.fishjournal.nav.FishJournalNavDrawerActivity;
import au.com.mitchhaley.fishjournal.nav.NavDrawerActivityConfiguration;
import au.com.mitchhaley.fishjournal.nav.NavDrawerAdapter;
import au.com.mitchhaley.fishjournal.nav.NavDrawerItem;
import au.com.mitchhaley.fishjournal.nav.NavMenuItem;
import au.com.mitchhaley.fishjournal.nav.NavMenuSection;

public class FishEntryActivity extends FishJournalNavDrawerActivity {

    private SectionFragmentPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private FishDetailsFragment fishDetailsFragment;
    
    private FishTypeListFragment fishTypeListFragment;
    
    private FishConditionsFragment fishConditionsFragment;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the adapter that will return a fragment for each of the sections of the app.
        mSectionsPagerAdapter = new SectionFragmentPagerAdapter(getSupportFragmentManager(), getApplicationContext());

        mSectionsPagerAdapter.addSection("Details", FishDetailsFragment.class, new Bundle());
        mSectionsPagerAdapter.addSection("Species", FishTypeListFragment.class, new Bundle());
        mSectionsPagerAdapter.addSection("Conditions", FishConditionsFragment.class, new Bundle());
        
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        PagerTitleStrip titleStrip = (PagerTitleStrip) findViewById(R.id.pager_title_strip);
        titleStrip.setBackgroundColor(getResources().getColor(R.color.fishEntryBackground));
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
      case R.id.fish_entry_save:
    	  FishEntryContentHelper.create(this);
    	  return true;
      case R.id.fish_entry_delete:
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

	public FishTypeListFragment getFishTypeListFragment() {
		return fishTypeListFragment;
	}

	public void setFishTypeListFragment(FishTypeListFragment fishTypeListFragment) {
		this.fishTypeListFragment = fishTypeListFragment;
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
    protected void onNavItemSelected(int id) {

        Intent i;
        switch ((int)id) {
            case 100:
                break;
            case 101:
                i = new Intent(this, FishEntryActivity.class);
                i.putExtra(NAV_DRAWER_POSITION, 1);
                startActivity(i);
                break;
            case 102:
                i = new Intent(this, FishListActivity.class);
                i.putExtra(NAV_DRAWER_POSITION, 1);

                startActivity(i);
                break;
            case 103:
                break;
            case 104:
                break;
            case 105:
                break;
        }
    }

	@Override
	public int getMainLayout() {
		return R.layout.fishentry_main;
	}

	   
    

}
