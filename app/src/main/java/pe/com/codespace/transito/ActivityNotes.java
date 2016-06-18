package pe.com.codespace.transito;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Creado por Carlos el 01/03/14.
 */
public class ActivityNotes extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private int tipoNorma, cantidadArticulosNorma;
    private MenuItem menuItem;
    private float numeroArticuloSeleccionado;
    private String nombreArticuloSeleccionado;
    private String[][] myListNotes;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.mis_notas));
        }

        Intent intent = getIntent();
        tipoNorma = intent.getExtras().getInt(MyValues.TIPO_NORMA);
        cantidadArticulosNorma = intent.getExtras().getInt(MyValues.CANTIDAD_ARTICULOS);
        try{
            TextView tt = (TextView) findViewById(R.id.txtNoneNotas);
            switch (tipoNorma){
                case MyValues.NORMA_TRANSITO:
                    SQLiteHelperTransito myDBHelper1 = SQLiteHelperTransito.getInstance(this);
                    myListNotes = myDBHelper1.getNotes();                    
                    break;
                case MyValues.NORMA_VEHICULAR:
                    SQLiteHelperVehiculos myDBHelper2 = SQLiteHelperVehiculos.getInstance(this);
                    myListNotes = myDBHelper2.getNotes();
                    break;
                case MyValues.NORMA_LICENCIAS:
                    SQLiteHelperLicencias myDBHelper3 = SQLiteHelperLicencias.getInstance(this);
                    myListNotes = myDBHelper3.getNotes();
                    break;
            }

            if(myListNotes.length>0){
                tt.setVisibility(View.GONE);
            }
            ListView myList = (ListView) findViewById(R.id.lvNotes);
            AdapterListArticulos myListAdapter = new AdapterListArticulos(this, myListNotes);
            myList.setAdapter(myListAdapter);
            registerForContextMenu(myList);
            myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(getBaseContext(), "Mantener presionado para ver opciones", Toast.LENGTH_LONG).show();
                }
            });

            // Agregar el adView
            AdView adView = (AdView)this.findViewById(R.id.adViewNotes);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);

            //Analytics
            Tracker tracker = ((AnalyticsApplication)  getApplication()).getTracker(AnalyticsApplication.TrackerName.APP_TRACKER);
            String nameActivity = getApplicationContext().getPackageName() + "." + this.getClass().getSimpleName();
            tracker.setScreenName(nameActivity);
            tracker.enableAdvertisingIdCollection(true);
            tracker.send(new HitBuilders.AppViewBuilder().build());

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, view, menuInfo);
        MenuInflater inflater = getMenuInflater();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        numeroArticuloSeleccionado = Integer.parseInt(((TextView) info.targetView.findViewById(R.id.tvNumberItem)).getText().toString());
        nombreArticuloSeleccionado = ((TextView) info.targetView.findViewById(R.id.tvTitleItem)).getText().toString();
        menu.setHeaderTitle(nombreArticuloSeleccionado);
        inflater.inflate(R.menu.menu_contextual_notas,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.CtxEditNote:
                Intent intent = new Intent(this,ActivityAddNote.class);
                intent.putExtra(MyValues.NUMERO_ARTICULO,numeroArticuloSeleccionado);
                intent.putExtra(MyValues.NOMBRE_ARTICULO,nombreArticuloSeleccionado);
                intent.putExtra(MyValues.TIPO_NORMA, tipoNorma);
                this.startActivity(intent);
                finish();
                return  true;
            case R.id.CtxDelNote:
                Tools.EliminarNota(this, tipoNorma, numeroArticuloSeleccionado, nombreArticuloSeleccionado, cantidadArticulosNorma);
                return  true;
            case R.id.CtxCopyNote:
                Tools.CopyNotaToClipboard(this, tipoNorma,numeroArticuloSeleccionado);
                return  true;
            default:
                return super.onContextItemSelected(item);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar_main, menu);
        final MenuItem searchItem;
        MenuItem itemHide1 = menu.findItem(R.id.action_notes);
        MenuItem itemHide2 = menu.findItem(R.id.action_goto);
        itemHide1.setVisible(false);
        itemHide2.setVisible(false);
        searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Búsqueda...");
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                searchItem.collapseActionView();
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        menuItem = item;
        switch (item.getItemId()){
            case R.id.action_search:
                break;
            case R.id.action_favorites:
                Tools.MostrarFavoritos(this, tipoNorma, cantidadArticulosNorma);
                break;
            case R.id.action_share:
                Tools.ShareApp(this);
                break;
            case R.id.action_rate:
                Tools.RateApp(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        Tools.QuerySubmit(this,menuItem,tipoNorma,cantidadArticulosNorma,s);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

}
