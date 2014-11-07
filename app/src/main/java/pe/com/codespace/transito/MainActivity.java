package pe.com.codespace.transito;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class MainActivity extends Activity {

    GridView gridView;
    TextView textView1;
    private final static int PORTRAIT = 1;
    private final static int LANDSCAPE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        int orientation = getResources().getConfiguration().orientation;
        gridView = (GridView) findViewById(R.id.gridViewMain);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;
        float scaleFactor = metrics.density;
        float widthDp = widthPixels/scaleFactor;
        float heightDp = heightPixels/scaleFactor;
        float smallestWidth = Math.min(widthDp,heightDp);
        float maxSize = Math.max(widthDp,heightDp);

        textView1 = (TextView) findViewById(R.id.tvMenuMainTitle);
        //ViewGroup.MarginLayoutParams mlp_textview = (ViewGroup.MarginLayoutParams) textView1.getLayoutParams();
        ViewGroup.LayoutParams mlp_gridview = gridView.getLayoutParams();

        if(smallestWidth>=720){
            //tablet 10"
            switch (orientation){
                case PORTRAIT:
                    gridView.setNumColumns(2);
                    mlp_gridview.height = (int) convertDpToPixel(3*(maxSize/4),this);
                    break;
                case LANDSCAPE:
                    gridView.setNumColumns(3);
                    gridView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    break;
                default: break;
            }
        }
        else if(smallestWidth>=600){
            //tablet 7"
            switch (orientation){
                case PORTRAIT:
                    gridView.setNumColumns(2);
                    mlp_gridview.height = (int) convertDpToPixel(3*(maxSize/4),this);
                    break;
                case LANDSCAPE:
                    gridView.setNumColumns(3);
                    gridView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    break;
                default: break;
            }
        }
        else {
            //
            switch (orientation){
                case PORTRAIT:
                    gridView.setNumColumns(2);
                    mlp_gridview.height = (int) convertDpToPixel(2*(maxSize/3),this);
                    break;
                case LANDSCAPE:
                    gridView.setNumColumns(3);
                    gridView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    break;
                default: break;
            }
        }

        gridView.setAdapter(new AdapterImageMenu(this));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                switch (pos){
                    case 0:
                        Intent intent = new Intent(getApplicationContext(), ReglamentoTransitoActivity.class);
                        intent.putExtra("title","Reglamento Nacional de Tránsito");
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(getApplicationContext(), ReglamentoLicenciasActivity.class);
                        intent1.putExtra("title","Reglamento de Licencias de Conducir");
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(getApplicationContext(), ReglamentoVehicularActivity.class);
                        intent2.putExtra("title","Reglamento Nacional de Vehículos");
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(getApplicationContext(), GalleryActivity.class);
                        startActivity(intent3);
                        break;
                    case 4:
                        Intent intent4 = new Intent(getApplicationContext(), LinksActivity.class);
                        startActivity(intent4);
                        break;
                    default:
                        break;
                }

            }
        });

        //Agregar el adView
        AdView adView = (AdView)this.findViewById(R.id.adViewMain);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_settings:
                break;
            case 2:
                break;
        }

        return super.onOptionsItemSelected(item);
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

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

}
