package pe.com.codespace.transito;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;


public class LinksActivity extends Activity {

    ArrayList<String> links = null;
    ListView myList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_links);

        prepararLinks();
        TextView textView = (TextView) findViewById(R.id.textLinks);
        textView.setText("Enlaces a páginas web del Ministerio de Transportes, SUTRAN y SAT:");
        myList = (ListView) findViewById(R.id.listLinks);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.list_item,links);
        myList.setAdapter(adapter);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                switch (pos){
                    case 0:
                        Tools.CheckInternetAccessToWebview(LinksActivity.this, "http://slcp.mtc.gob.pe/");
                        break;
                    case 1:
                        Tools.CheckInternetAccessToWebview(LinksActivity.this, "https://www.mtc.gob.pe/portal/transportes/terrestre/padron_carga/ntransporte_pasajeros.asp");
                        break;
                    case 2:
                        Tools.CheckInternetAccessToWebview(LinksActivity.this, "http://spp.mtc.gob.pe/FrmConsultaPapeletasPeaton.aspx");
                        break;
                    case 3:
                        Tools.CheckInternetAccessToWebview(LinksActivity.this, "http://www.sutran.gob.pe/portal/index.php/devolucion-de-licencias-de-conducir-retenidas");
                        break;
                    case 4:
                        Tools.CheckInternetAccessToWebview(LinksActivity.this, "http://www.sutran.gob.pe/portal/index.php/devolucion-licencias-de-conducir-pnp");
                        break;
                    case 5:
                        Tools.CheckInternetAccessToWebview(LinksActivity.this, "http://www.sutran.gob.pe/portal/index.php/libro-de-reclamaciones");
                        break;
                    case 6:
                        Tools.CheckInternetAccessToWebview(LinksActivity.this, "http://www.sat.gob.pe/Websitev8/popup.aspx?t=2&v=");
                        break;
                    case 7:
                        Tools.CheckInternetAccessToWebview(LinksActivity.this, "https://www.sat.gob.pe/Websitev8/popup.aspx?t=6&v=");
                        break;
                    case 8:
                        Tools.CheckInternetAccessToWebview(LinksActivity.this, "https://www.sat.gob.pe/Websitev8/popup.aspx?t=7&v=");
                        break;
                    case 9:
                        Tools.CheckInternetAccessToWebview(LinksActivity.this, "https://www.sat.gob.pe/website/consultas/Buscador_Expedientes.aspx");
                        break;
                    case 10:
                        Tools.CheckInternetAccessToWebview(LinksActivity.this, "https://www.sat.gob.pe/Websitev8/popup.aspx?t=4&v=V");
                        break;
                    default: break;
                }
            }
        });

        //Agregar el adView
        AdView adView = (AdView)this.findViewById(R.id.adViewLinks);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.links, menu);
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

    private void prepararLinks(){
        links = new ArrayList<String>();
        links.add("MTC: Sistema de Licencias de Conducir por Puntos");
        links.add("MTC: Empresas de Transporte de Pasajeros");
        links.add("MTC: Sistema de Papeleta para Peatones");
        links.add("SUTRAN: Devolución de Licencias de conducir retenidas por los inspectores");
        links.add("SUTRAN: Devolución de Licencias de conducir retenidas por la PNP");
        links.add("SUTRAN: Libro de reclamaciones");
        links.add("SAT: Papeletas pendientes de pago");
        links.add("SAT: Sanciones del Conductor/Peatón");
        links.add("SAT: Captura de Vehículos");
        links.add("SAT: Consulta del Estado de los Expedientes");
        links.add("SAT: Impuesto Vehicular");
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
