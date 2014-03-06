package au.com.mitchhaley.fishjournal.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;


    public class SectionFragmentPagerAdapter extends FragmentPagerAdapter {

        private List<TabInfo> tabs;

        private Context context;

        public SectionFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            tabs = new ArrayList<TabInfo>();
            this.context = context;
        }

        public void addSection(String tabTitle, Class<?> clss, Bundle args) {
            tabs.add(new TabInfo(clss.getName(), tabTitle, args));
        }

        @Override
        public Fragment getItem(int position) {
            TabInfo tabInfo = tabs.get(position);

            return Fragment.instantiate(context, tabInfo.getTabClass(), tabInfo.getTabArgs());
        }

        @Override
        public int getCount() {
            return tabs.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //Locale l = Locale.getDefault();

            TabInfo tabInfo = tabs.get(position);
            return tabInfo.getTabTitle();
        }

        public class TabInfo {

            private String tabClass;

            private String tabTitle;

            private Bundle tabArgs;

            public TabInfo(String tabClass, String tabTitle, Bundle tabArgs) {
                this.tabClass = tabClass;
                this.tabTitle = tabTitle;
                this.tabArgs = tabArgs;
            }

            public String getTabClass() {
                return tabClass;
            }

            public void setTabClass(String tabClass) {
                this.tabClass = tabClass;
            }

            public String getTabTitle() {
                return tabTitle;
            }

            public void setTabTitle(String tabTitle) {
                this.tabTitle = tabTitle;
            }

            public Bundle getTabArgs() {
                return tabArgs;
            }

            public void setTabArgs(Bundle tabArgs) {
                this.tabArgs = tabArgs;
            }
        }
    }