package pe.com.codespace.transito;

import java.util.Locale;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.analytics.tracking.android.EasyTracker;


public class GalleryActivity extends Activity implements ActionBar.TabListener {
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

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

        // Agregar el adView
        AdView adView = (AdView)this.findViewById(R.id.adViewGallery);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.gallery, menu);
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
                    return getString(R.string.title_section_senales1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section_senales2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section_senales3).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment_infracciones containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";
        int[] imagesReglamentarias = {R.drawable.reglamen1, R.drawable.reglamen2, R.drawable.reglamen3, R.drawable.reglamen4, R.drawable.reglamen5, R.drawable.reglamen6, R. drawable.reglamen7};
        int[] imagesPreventivas = {R.drawable.preven1, R.drawable.preven2, R.drawable.preven3, R.drawable.preven4, R.drawable.preven5, R.drawable.preven6, R. drawable.preven7, R.drawable.preven8};
        int[] imagesInformativas = {R.drawable.inform1, R.drawable.inform2, R.drawable.inform3, R.drawable.inform4, R.drawable.inform5, R.drawable.inform6};
        int index = 0;

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
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);
            final ImageView imageView = (ImageView) rootView.findViewById(R.id.image_transitsign);
            final TextView textView = (TextView) rootView.findViewById(R.id.tvNroPag);
            final int seccion = getArguments().getInt(ARG_SECTION_NUMBER);
            switch (seccion){
                case 1:
                    imageView.setImageResource(imagesReglamentarias[index]);
                    textView.setText("Pag. " + String.valueOf(index+1));
                    break;
                case 2:
                    imageView.setImageResource(imagesPreventivas[index]);
                    textView.setText("Pag. " + String.valueOf(index+1));
                    break;
                case 3:
                    imageView.setImageResource(imagesInformativas[index]);
                    textView.setText("Pag. " + String.valueOf(index+1));
                    break;
            }

            Button btnNext, btnPrev;
            btnNext = (Button) rootView.findViewById(R.id.btnNextImage);
            btnPrev = (Button) rootView.findViewById(R.id.btnPrevImage);

            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    index++;
                    switch (seccion){
                        case 1:
                            if(index<=6){
                                imageView.setImageResource(imagesReglamentarias[index]);
                                textView.setText("Pag. " + String.valueOf(index+1));
                            }
                            else{
                                imageView.setImageResource(imagesReglamentarias[0]);
                                index = 0;
                                textView.setText("Pag. " + String.valueOf(index+1));
                            }

                            break;
                        case 2:
                            if(index<=7){
                                imageView.setImageResource(imagesPreventivas[index]);
                                textView.setText("Pag. " + String.valueOf(index+1));
                            }
                            else {
                                imageView.setImageResource(imagesPreventivas[0]);
                                index = 0;
                                textView.setText("Pag. " + String.valueOf(index+1));
                            }
                            break;
                        case 3:
                            if(index<=5){
                                imageView.setImageResource(imagesInformativas[index]);
                                textView.setText("Pag. " + String.valueOf(index+1));
                            }
                            else {
                                imageView.setImageResource(imagesInformativas[0]);
                                index = 0;
                                textView.setText("Pag. " + String.valueOf(index+1));
                            }
                            break;
                    }
                }
            });

            btnPrev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    index--;
                    switch (seccion){
                        case 1:
                            if(index<0){
                                imageView.setImageResource(imagesReglamentarias[6]);
                                index=6;
                                textView.setText("Pag. " + String.valueOf(index+1));
                            }
                            else{
                                imageView.setImageResource(imagesReglamentarias[index]);
                                textView.setText("Pag. " + String.valueOf(index+1));
                            }

                            break;
                        case 2:
                            if(index<0){
                                imageView.setImageResource(imagesPreventivas[7]);
                                index=7;
                                textView.setText("Pag. " + String.valueOf(index+1));
                            }
                            else {
                                imageView.setImageResource(imagesPreventivas[index]);
                                textView.setText("Pag. " + String.valueOf(index+1));
                            }
                            break;
                        case 3:
                            if(index<0){
                                imageView.setImageResource(imagesInformativas[5]);
                                index=5;
                                textView.setText("Pag. " + String.valueOf(index+1));
                            }
                            else{
                                imageView.setImageResource(imagesInformativas[index]);
                                textView.setText("Pag. " + String.valueOf(index+1));
                            }
                            break;
                    }
                }
            });
            return rootView;
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
