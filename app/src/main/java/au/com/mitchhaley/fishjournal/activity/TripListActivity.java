package au.com.mitchhaley.fishjournal.activity;


import android.os.Bundle;

import au.com.mitchhaley.fishjournal.R;
import au.com.mitchhaley.fishjournal.nav.FishJournalNavDrawerActivity;
import au.com.mitchhaley.fishjournal.nav.NavDrawerActivityConfiguration;
import au.com.mitchhaley.fishjournal.nav.NavDrawerAdapter;
import au.com.mitchhaley.fishjournal.nav.NavDrawerItem;
import au.com.mitchhaley.fishjournal.nav.NavMenuItem;
import au.com.mitchhaley.fishjournal.nav.NavMenuSection;

public class TripListActivity extends FishJournalNavDrawerActivity {

		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
        navDrawerActivityConfiguration.setMainLayout(R.layout.triplist_main);
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
		return R.layout.triplist_main;
	}


} 