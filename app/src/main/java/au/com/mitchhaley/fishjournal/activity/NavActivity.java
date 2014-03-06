package au.com.mitchhaley.fishjournal.activity;

import android.os.Bundle;

import au.com.mitchhaley.fishjournal.R;
import au.com.mitchhaley.fishjournal.nav.AbstractNavDrawerActivity;
import au.com.mitchhaley.fishjournal.nav.NavDrawerActivityConfiguration;
import au.com.mitchhaley.fishjournal.nav.NavDrawerAdapter;
import au.com.mitchhaley.fishjournal.nav.NavDrawerItem;
import au.com.mitchhaley.fishjournal.nav.NavMenuSection;

public class NavActivity extends AbstractNavDrawerActivity {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        protected NavDrawerActivityConfiguration getNavDrawerConfiguration() {

            NavDrawerItem[] menu = new NavDrawerItem[] {
                    NavMenuSection.create(100, "Demos")};
//                    NavMenuItem.create(101, "FishEntry", "ic_drawer", false, this)};

            NavDrawerActivityConfiguration navDrawerActivityConfiguration = new NavDrawerActivityConfiguration();
            navDrawerActivityConfiguration.setMainLayout(R.layout.nav_drawer);
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
            switch ((int)id) {
                case 100:
                    break;
                case 101:
                    break;
            }
        }
    }