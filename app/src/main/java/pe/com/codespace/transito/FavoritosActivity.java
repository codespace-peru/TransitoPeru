package pe.com.codespace.transito;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.analytics.tracking.android.EasyTracker;

/**
 * Created by Carlos on 16/02/14.
 */
public class FavoritosActivity extends ActionBarActivity implements SearchView.OnQueryTextListener {
    AdapterListArticulos myListAdapter;
    SQLiteHelperTransito myDBHelper1;
    SQLiteHelperVehiculos myDBHelper2;
    SQLiteHelperLicencias myDBHelper3;
    private static final int NORMA_TRANSITO = 1;
    private static final int NORMA_VEHICULAR = 2;
    private static final int NORMA_LICENCIAS = 3;
    ListView myList;
    String nombreArticuloSeleccionado = "";
    float numeroArticuloSeleccionado = -1;
    private SearchView searchView;
    MenuItem menuItem;
    int tipoNorma, cantidadArticulosNorma;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);
        Intent intent = getIntent();
        tipoNorma = intent.getExtras().getInt("tipoNorma");
        cantidadArticulosNorma = intent.getExtras().getInt("cantidadArticulosNorma");
        TextView tt = (TextView) findViewById(R.id.txtNone);
        String[][] myListaFav = null;

        try{
            switch (tipoNorma){
                case NORMA_TRANSITO:
                    myDBHelper1 = SQLiteHelperTransito.getInstance(this);
                    myListaFav = myDBHelper1.getFavoritos();
                    break;
                case NORMA_VEHICULAR:
                    myDBHelper2 = SQLiteHelperVehiculos.getInstance(this);
                    myListaFav = myDBHelper2.getFavoritos();
                    break;
                case NORMA_LICENCIAS:
                    myDBHelper3 = SQLiteHelperLicencias.getInstance(this);
                    myListaFav = myDBHelper3.getFavoritos();
                    break;
                default: break;
            }

            if(myListaFav.length>0){
                tt.setVisibility(View.GONE);
            }

            myList = (ListView) findViewById(R.id.lvFavoritos);
            myListAdapter = new AdapterListArticulos(this,myListaFav);
            myList.setAdapter(myListAdapter);
            registerForContextMenu(myList);
            myList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(getBaseContext(),"Mantener presionado para ver opciones",Toast.LENGTH_LONG).show();
                }
            });
            // Agregar el adView
            AdView adView = (AdView)this.findViewById(R.id.adViewFavoritos);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }catch (Exception ex){
           Log.e("Debug","MessageError: " + ex);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu,view,menuInfo);
        MenuInflater inflater = getMenuInflater();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        nombreArticuloSeleccionado = ((TextView) info.targetView.findViewById(R.id.tvTitleItem)).getText().toString();
        numeroArticuloSeleccionado = Integer.parseInt(((TextView) info.targetView.findViewById(R.id.tvNumberItem)).getText().toString());
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
                break;
            default: break;
        }
        MenuItem itemHide3 = menu.findItem(R.id.CtxAddFavorito);
        itemHide3.setVisible(false);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.CtxAddFavorito:
                return false; // Esta opción no se mostrará al usuario en esta activity
            case R.id.CtxDelFavorito:
                Tools.EliminarFavorito(this,tipoNorma, numeroArticuloSeleccionado, nombreArticuloSeleccionado);
                return true;
            case R.id.CtxShowNote:
                Tools.ShowNota(this, tipoNorma, numeroArticuloSeleccionado, nombreArticuloSeleccionado);
                return true;
            case R.id.CtxCopyArticulo:
                Tools.CopyArticuloToClipboard(this,tipoNorma,numeroArticuloSeleccionado);
                return  true;
            case R.id.CtxAddNote: case R.id.CtxEditNote:
                Tools.AgregarNota(this, tipoNorma, numeroArticuloSeleccionado, nombreArticuloSeleccionado, cantidadArticulosNorma);
                return  true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar_main, menu);
        final MenuItem searchItem;
        MenuItem itemHide1 = menu.findItem(R.id.action_favorites);
        MenuItem itemHide2 = menu.findItem(R.id.action_goto);
        itemHide1.setVisible(false);
        itemHide2.setVisible(false);
        searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Búsqueda...");
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View view, boolean b){
                menuItem.collapseActionView();
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
            case R.id.action_notes:
                Tools.MostrarNotas(this,tipoNorma, cantidadArticulosNorma);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
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
