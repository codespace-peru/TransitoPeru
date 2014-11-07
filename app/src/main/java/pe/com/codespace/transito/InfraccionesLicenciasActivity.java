package pe.com.codespace.transito;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;



public class InfraccionesLicenciasActivity extends Activity implements ActionBar.TabListener {

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infracciones);

        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.infracciones, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section_infraccioneslicencias1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section_infraccioneslicencias2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section_infraccioneslicencias3).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment_infracciones containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";
        LayoutInflater inflater;
        SQLiteHelperLicencias myDBHelper;
        List<String> listDataHeader;
        HashMap<String, List<String>> listDataChild;
        AdapterExpandableList myAdapter;
        private final static int PRIMERA = 1;
        private final static int SEGUNDA = 2;
        private final static int TERCERA = 3;

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_infracciones, container, false);
            //int anexo = getActivity().getIntent().getExtras().getInt("anexo");
            int tipo = getArguments().getInt(ARG_SECTION_NUMBER);

            prepararData(tipo);
            final ExpandableListView myExpand;
            myExpand = (ExpandableListView) rootView.findViewById(R.id.myExpand);

            myAdapter = new AdapterExpandableList(getActivity(), listDataHeader, listDataChild);
            myExpand.setAdapter(myAdapter);
            myExpand.setGroupIndicator(null);
            myExpand.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                int lastExpandedPosition = -1;
                @Override
                public void onGroupExpand(int pos) {
                    if(lastExpandedPosition != -1 && pos != lastExpandedPosition)
                        myExpand.collapseGroup(lastExpandedPosition);
                    lastExpandedPosition = pos;
                }
            });

            //Agregar el adView
            AdView adView = (AdView)rootView.findViewById(R.id.adViewInfracciones);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);

            return rootView;
        }

        private void prepararData(int tipo){
            listDataHeader = new ArrayList<String>();
            listDataChild = new HashMap<String, List<String>>();
            myDBHelper = SQLiteHelperLicencias.getInstance(getActivity());
            String[] temp;
            switch (tipo){
                case PRIMERA:
                    listDataHeader = myDBHelper.getInfracciones("A");
                    List<String> Primera;
                    for(int i=0; i<listDataHeader.size(); i++){
                        temp = myDBHelper.getInfraccion(listDataHeader.get(i));
                        Primera = new ArrayList<String>();
                        Primera.add("<b>Infracción:</b><br>" + temp[0] + "<br><br>" + "<b>Calificación:</b><br>" + temp[1] + "<br><br>" + "<b>Sanción:</b><br>" + temp[2] + "<br>");
                        listDataChild.put(listDataHeader.get(i), Primera);
                    }
                    break;
                case SEGUNDA:
                    listDataHeader = myDBHelper.getInfracciones("B");
                    List<String> Segunda;
                    for(int i=0; i<listDataHeader.size(); i++){
                        temp = myDBHelper.getInfraccion(listDataHeader.get(i));
                        Segunda = new ArrayList<String>();
                        Segunda.add("<b>Infracción:</b><br>" + temp[0] + "<br><br>" + "<b>Calificación:</b><br>" + temp[1] + "<br><br>" + "<b>Sanción:</b><br>" + temp[2] + "<br>");
                        listDataChild.put(listDataHeader.get(i), Segunda);
                    }
                    break;
                case TERCERA:
                    listDataHeader = myDBHelper.getInfracciones("C");
                    List<String> Tercera;
                    for(int i=0; i<listDataHeader.size(); i++){
                        temp = myDBHelper.getInfraccion(listDataHeader.get(i));
                        Tercera = new ArrayList<String>();
                        Tercera.add("<b>Infracción:</b><br>" + temp[0] + "<br><br>" + "<b>Calificación:</b><br>" + temp[1] + "<br><br>" + "<b>Sanción:</b><br>" + temp[2] + "<br>");
                        listDataChild.put(listDataHeader.get(i), Tercera);
                    }
                    break;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);
    }
}
