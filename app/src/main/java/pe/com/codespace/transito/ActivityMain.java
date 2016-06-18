package pe.com.codespace.transito;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;


public class ActivityMain extends AppCompatActivity {

    private final static int PORTRAIT = 1;
    private final static int LANDSCAPE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        }

        AppRater.app_launched(this);
        int orientation = getResources().getConfiguration().orientation;
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;
        float scaleFactor = metrics.density;
        float widthDp = widthPixels/scaleFactor;
        float heightDp = heightPixels/scaleFactor;
        float smallWidth = Math.min(widthDp,heightDp);

        int height_portrait = (int) convertDpToPixel((heightDp/8),this);
        int height_landscape = (int) convertDpToPixel((heightDp/5),this);
        int width_landscape = (int) convertDpToPixel((widthDp/2),this);

        Button boton1 = (Button) findViewById(R.id.btnTransito1);
        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) boton1.getLayoutParams();

        Button boton2 = (Button) findViewById(R.id.btnTransito2);
        RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) boton2.getLayoutParams();


        Button boton3 = (Button) findViewById(R.id.btnTransito3);
        RelativeLayout.LayoutParams params3 = (RelativeLayout.LayoutParams) boton3.getLayoutParams();


        Button boton4 = (Button) findViewById(R.id.btnTransito4);
        RelativeLayout.LayoutParams params4 = (RelativeLayout.LayoutParams) boton4.getLayoutParams();


        Button boton5 = (Button) findViewById(R.id.btnTransito5);
        RelativeLayout.LayoutParams params5 = (RelativeLayout.LayoutParams) boton5.getLayoutParams();


        Button boton6 = (Button) findViewById(R.id.btnTransito6);
        RelativeLayout.LayoutParams params6 = (RelativeLayout.LayoutParams) boton6.getLayoutParams();

        boton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityReglamentoTransito.class);
                startActivity(intent);
            }
        });

        boton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityReglamentoLicencias.class);
                startActivity(intent);
            }
        });

        boton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityReglamentoVehicular.class);
                startActivity(intent);
            }
        });

        boton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivitySenales.class);
                startActivity(intent);
            }
        });

        boton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityLinks.class);
                startActivity(intent);
            }
        });

        boton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityMultasTransito.class);
                intent.putExtra(MyValues.ANEXO, 9);
                startActivity(intent);
            }
        });

        switch (orientation){
            case PORTRAIT:
                params1.height = height_portrait;
                params2.height = height_portrait;
                params3.height = height_portrait;
                params4.height = height_portrait;
                params5.height = height_portrait;
                params6.height = height_portrait;
                break;
            case LANDSCAPE:
                params1.height = height_landscape;
                params1.width = width_landscape;
                params2.height = height_landscape;
                params2.width = width_landscape;
                params2.addRule(RelativeLayout.RIGHT_OF, R.id.btnTransito1);
                params2.addRule(RelativeLayout.BELOW, R.id.myToolbar);
                params3.height = height_landscape;
                params3.width = width_landscape;
                params4.height = height_landscape;
                params4.width = width_landscape;
                params4.addRule(RelativeLayout.RIGHT_OF, R.id.btnTransito3);
                params4.addRule(RelativeLayout.BELOW, R.id.btnTransito2);
                params5.height = height_landscape;
                params5.width = width_landscape;
                params6.height = height_landscape;
                params6.width = width_landscape;
                params6.addRule(RelativeLayout.RIGHT_OF, R.id.btnTransito5);
                params6.addRule(RelativeLayout.BELOW, R.id.btnTransito4);
                break;
        }

        if(smallWidth<=600){
            switch (orientation){
                case PORTRAIT:
                    break;
                case LANDSCAPE:
                    break;
            }
        }

        boton1.setLayoutParams(params1);
        boton2.setLayoutParams(params2);
        boton3.setLayoutParams(params3);
        boton4.setLayoutParams(params4);
        boton5.setLayoutParams(params5);
        boton6.setLayoutParams(params6);

        //Agregar el adView
        AdView adView = (AdView)this.findViewById(R.id.adViewMain);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        //Analytics
        Tracker tracker = ((AnalyticsApplication)  getApplication()).getTracker(AnalyticsApplication.TrackerName.APP_TRACKER);
        String nameActivity = getApplicationContext().getPackageName() + "." + this.getClass().getSimpleName();
        tracker.setScreenName(nameActivity);
        tracker.enableAdvertisingIdCollection(true);
        tracker.send(new HitBuilders.AppViewBuilder().build());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar_main, menu);
        MenuItem itemHide1 = menu.findItem(R.id.action_favorites);
        MenuItem itemHide2 = menu.findItem(R.id.action_notes);
        MenuItem itemHide3 = menu.findItem(R.id.action_search);
        MenuItem itemHide4 = menu.findItem(R.id.action_goto);
        itemHide1.setVisible(false);
        itemHide2.setVisible(false);
        itemHide3.setVisible(false);
        itemHide4.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_rate:
                Tools.RateApp(this);
                break;
            case R.id.action_share:
                Tools.ShareApp(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * (metrics.densityDpi / 160f);
    }
}