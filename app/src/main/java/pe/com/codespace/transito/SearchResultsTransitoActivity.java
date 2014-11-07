package pe.com.codespace.transito;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

//import com.google.analytics.tracking.android.EasyTracker;

/**
 * Created by Carlos on 20/02/14.
 */
public class SearchResultsTransitoActivity extends ActionBarActivity implements SearchView.OnQueryTextListener {
    private static final int NORMA_TRANSITO = 1;
    private static final int NORMA_VEHICULAR = 2;
    private static final int NORMA_LICENCIAS = 3;
    ListView myList;
    SQLiteHelperTransito myDBHelper1;
    SQLiteHelperVehiculos myDBHelper2;
    SQLiteHelperLicencias myDBHelper3;
    AdapterListArticulos myListAdapter;
    int numeroArticuloSeleccionado;
    String nombreArticuloSeleccionado;
    SearchView searchView;
    String searchText;
    MenuItem menuItem;
    int tipoNorma, cantidadArticulosNorma;
    String[][] resultados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchresults);
        Intent intent = getIntent();
        searchText = intent.getExtras().getString("searchText");
        tipoNorma = intent.getExtras().getInt("tipoNorma");
        cantidadArticulosNorma = intent.getExtras().getInt("cantidadArticulosNorma");
        try{
            switch (tipoNorma){
                case NORMA_TRANSITO:
                    myDBHelper1 = SQLiteHelperTransito.getInstance(this);
                    resultados = myDBHelper1.searchArticulo(searchText);
                    break;
                case NORMA_VEHICULAR:
                    myDBHelper2 = SQLiteHelperVehiculos.getInstance(this);
                    resultados = myDBHelper2.searchArticulo(searchText);
                    break;
                case NORMA_LICENCIAS:
                    myDBHelper3 = SQLiteHelperLicencias.getInstance(this);
                    resultados = myDBHelper3.searchArticulo(searchText);
                    break;
            }

            myList = (ListView) findViewById(R.id.lvSearchResults);

            myListAdapter = new AdapterListArticulos(this,resultados,true,searchText);
            TextView tv = (TextView)findViewById(R.id.tvSearchText);
            switch (resultados.length){
                case 0:
                    tv.setText(Html.fromHtml("No se encontraron coincidencias para <b><i>'" + searchText + "'</i></b>"));
                    break;
                case 1:
                    tv.setText(Html.fromHtml("Se encontró 1 artículo que contiene <b><i>'" + searchText + "'</i></b>"));
                    break;
                default:
                    tv.setText(Html.fromHtml("Se encontraron " + resultados.length + " artículos que contienen <b><i>'" + searchText + "'</i></b>"));
                    break;
            }

            myList.setAdapter(myListAdapter);
            registerForContextMenu(myList);
            myList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(getBaseContext(),"Mantener presionado para ver opciones",Toast.LENGTH_LONG).show();
                }
            });
            // Agregar el adView
            AdView adView = (AdView)this.findViewById(R.id.adViewSearch);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);

        }catch (Exception ex){
            Log.e("Debug", "MessageError: " + ex);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu,view,menuInfo);
        MenuInflater inflater = getMenuInflater();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        numeroArticuloSeleccionado = Integer.parseInt(((TextView) info.targetView.findViewById(R.id.tvNumberItem)).getText().toString());
        nombreArticuloSeleccionado = ((TextView) info.targetView.findViewById(R.id.tvTitleItem)).getText().toString();
        menu.setHeaderTitle(nombreArticuloSeleccionado);
        inflater.inflate(R.menu.menu_contextual_lista,menu);

        switch (tipoNorma){
            case NORMA_TRANSITO:
                if(myDBHelper1.hay_nota(numeroArticuloSeleccionado)){
                    MenuItem itemHide1 = menu.findItem(R.id.CtxAddNote);
                    itemHide1.setVisible(false);
                }
                else{
                    MenuItem itemHide1 = menu.findItem(R.id.CtxEditNote);
                    itemHide1.setVisible(false);
                    MenuItem itemHide2 = menu.findItem(R.id.CtxShowNote);
                    itemHide2.setVisible(false);
                }

                if(myDBHelper1.es_favorito(numeroArticuloSeleccionado)){
                    MenuItem itemHide3 = menu.findItem(R.id.CtxAddFavorito);
                    itemHide3.setVisible(false);
                }
                else{
                    MenuItem itemHide3 = menu.findItem(R.id.CtxDelFavorito);
                    itemHide3.setVisible(false);
                }
                break;
            case NORMA_VEHICULAR:
                if(myDBHelper2.hay_nota(numeroArticuloSeleccionado)){
                    MenuItem itemHide1 = menu.findItem(R.id.CtxAddNote);
                    itemHide1.setVisible(false);
                }
                else{
                    MenuItem itemHide1 = menu.findItem(R.id.CtxEditNote);
                    itemHide1.setVisible(false);
                    MenuItem itemHide2 = menu.findItem(R.id.CtxShowNote);
                    itemHide2.setVisible(false);
                }

                if(myDBHelper2.es_favorito(numeroArticuloSeleccionado)){
                    MenuItem itemHide3 = menu.findItem(R.id.CtxAddFavorito);
                    itemHide3.setVisible(false);
                }
                else{
                    MenuItem itemHide3 = menu.findItem(R.id.CtxDelFavorito);
                    itemHide3.setVisible(false);
                }
                break;
            case NORMA_LICENCIAS:
                if(myDBHelper3.hay_nota(numeroArticuloSeleccionado)){
                    MenuItem itemHide1 = menu.findItem(R.id.CtxAddNote);
                    itemHide1.setVisible(false);
                }
                else{
                    MenuItem itemHide1 = menu.findItem(R.id.CtxEditNote);
                    itemHide1.setVisible(false);
                    MenuItem itemHide2 = menu.findItem(R.id.CtxShowNote);
                    itemHide2.setVisible(false);
                }

                if(myDBHelper3.es_favorito(numeroArticuloSeleccionado)){
                    MenuItem itemHide3 = menu.findItem(R.id.CtxAddFavorito);
                    itemHide3.setVisible(false);
                }
                else{
                    MenuItem itemHide3 = menu.findItem(R.id.CtxDelFavorito);
                    itemHide3.setVisible(false);
                }
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.CtxAddFavorito:
                Tools.AgregarFavorito(this, tipoNorma, numeroArticuloSeleccionado, nombreArticuloSeleccionado);
                return true;
            case R.id.CtxDelFavorito:
                Tools.EliminarFavorito(this,tipoNorma,numeroArticuloSeleccionado,nombreArticuloSeleccionado);
                return true;
            case R.id.CtxShowNote:
                Tools.ShowNota(this,tipoNorma,numeroArticuloSeleccionado,nombreArticuloSeleccionado);
                return true;
            case R.id.CtxAddNote: case R.id.CtxEditNote:
                Tools.AgregarNota(this, tipoNorma, numeroArticuloSeleccionado, nombreArticuloSeleccionado, cantidadArticulosNorma);
                return true;
            case R.id.CtxCopyArticulo:
                Tools.CopyArticuloToClipboard(this,tipoNorma,numeroArticuloSeleccionado);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar_main, menu);
        MenuItem itemHide = menu.findItem(R.id.action_goto);
        itemHide.setVisible(false);
        final MenuItem searchItem;
        searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Búsqueda...");
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View view, boolean b){
                MenuItemCompat.collapseActionView(searchItem);
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
                Tools.MostrarFavoritos(this,tipoNorma, cantidadArticulosNorma);
                break;
            case R.id.action_notes:
                Tools.MostrarNotas(this, tipoNorma, cantidadArticulosNorma);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        finish();
        Tools.QuerySubmit(this,menuItem,tipoNorma,cantidadArticulosNorma, s);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
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
