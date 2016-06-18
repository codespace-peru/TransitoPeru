package pe.com.codespace.transito;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;


public class ActivityLinks extends AppCompatActivity {

    private ArrayList<String> links = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_links);
        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.consultas_online_title));
        }

        prepararLinks();
        TextView textView = (TextView) findViewById(R.id.textLinks);
        textView.setText("Enlaces a páginas web:");
        ListView myList = (ListView) findViewById(R.id.listLinks);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.list_item,links);
        myList.setAdapter(adapter);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                switch (pos) {
                    case 0:
                        Tools.CheckInternetAccessToWebview(ActivityLinks.this, "http://slcp.mtc.gob.pe");
                        break;
                    case 1:
                        Tools.CheckInternetAccessToWebview(ActivityLinks.this, "http://www.mtc.gob.pe/servicios_tramite/transporte_terrestre/ntransporte_pasajeros.html");
                        break;
                    case 2:
                        Tools.CheckInternetAccessToWebview(ActivityLinks.this, "http://www.mtc.gob.pe/servicios_tramite/transporte_terrestre/frm_rep_intra_mercancia.html");
                        break;
                    case 3:
                        Tools.CheckInternetAccessToWebview(ActivityLinks.this, "http://spp.mtc.gob.pe");
                        break;
                    case 4:
                        Tools.CheckInternetAccessToWebview(ActivityLinks.this, "http://sistemas.sutran.gob.pe/WebExterno/Pages/frmRecordInfracciones.aspx");
                        break;
                    case 5:
                        Tools.CheckInternetAccessToWebview(ActivityLinks.this, "http://sistemas.sutran.gob.pe/WebExterno/Pages/FrmLicenciasRetSutran5.aspx");
                        break;
                    case 6:
                        Tools.CheckInternetAccessToWebview(ActivityLinks.this, "https://www.sat.gob.pe/websitev8/Modulos/contenidos/Calculadora.aspx");
                        break;
                    case 7:
                        Tools.CheckInternetAccessToWebview(ActivityLinks.this, "http://www.sat.gob.pe/Websitev8/popup.aspx?t=2&v=");
                        break;
                    case 8:
                        Tools.CheckInternetAccessToWebview(ActivityLinks.this, "https://www.sat.gob.pe/Websitev8/popup.aspx?t=6&v=");
                        break;
                    case 9:
                        Tools.CheckInternetAccessToWebview(ActivityLinks.this, "https://www.sat.gob.pe/Websitev8/popup.aspx?t=7&v=");
                        break;
                    case 10:
                        Tools.CheckInternetAccessToWebview(ActivityLinks.this, "https://www.sat.gob.pe/Websitev8/popup.aspx?t=4&v=V");
                        break;
                    case 11:
                        Tools.CheckInternetAccessToWebview(ActivityLinks.this, "https://www.sunarp.gob.pe/consultavehicular/");
                        break;
                    case 12:
                        Tools.CheckInternetAccessToWebview(ActivityLinks.this, "http://placas.pe/Public/CheckPlateStatus.aspx");
                        break;
                    default:
                        break;
                }
            }
        });

        //Agregar el adView
        AdView adView = (AdView)this.findViewById(R.id.adViewLinks);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        //Analytics
        Tracker tracker = ((AnalyticsApplication)  getApplication()).getTracker(AnalyticsApplication.TrackerName.APP_TRACKER);
        String nameActivity = getApplicationContext().getPackageName() + "." + this.getClass().getSimpleName();
        tracker.setScreenName(nameActivity);
        tracker.enableAdvertisingIdCollection(true);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
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
        switch (item.getItemId()){
            case R.id.action_rate:
                Tools.RateApp(this);
                break;
            case R.id.action_share:
                Tools.ShareApp(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void prepararLinks(){
        links = new ArrayList<>();
        links.add("MTC: Sistema de Licencias de Conducir por Puntos");
        links.add("MTC: Empresas de Transporte Terrestre de Pasajeros");
        links.add("MTC: Empresas de Transporte Terrestre de Mercancías y Materiales");
        links.add("MTC: Sistema de Papeleta para Peatones");
        links.add("SUTRAN: Record de Infracciones e Incumplimientos");
        links.add("SUTRAN: Estado de Licencias de Conducir retenidas");
        links.add("SAT: Calculadora del Impuesto Vehicular");
        links.add("SAT: Papeletas pendientes de pago");
        links.add("SAT: Sanciones del Conductor/Peatón");
        links.add("SAT: Captura de Vehículos");
        links.add("SAT: Impuesto Vehicular");
        links.add("SUNARP: Consulta Vehicular");
        links.add("AAP: Consulta de Estado de Placa");
    }

}
