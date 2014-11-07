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

import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by Carlos on 01/03/14.
 */
public class NotesActivity extends ActionBarActivity implements SearchView.OnQueryTextListener {

    SQLiteHelperTransito myDBHelper1;
    SQLiteHelperVehiculos myDBHelper2;
    SQLiteHelperLicencias myDBHelper3;
    AdapterListArticulos myListAdapter;
    int tipoNorma, cantidadArticulosNorma;
    ListView myList;
    SearchView searchView;
    MenuItem menuItem;
    private static final int NORMA_TRANSITO = 1;
    private static final int NORMA_VEHICULAR = 2;
    private static final int NORMA_LICENCIAS = 3;
    float numeroArticuloSeleccionado;
    String nombreArticuloSeleccionado;
    String[][] myListNotes;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        Intent intent = getIntent();
        tipoNorma = intent.getExtras().getInt("tipoNorma");
        cantidadArticulosNorma = intent.getExtras().getInt("cantidadArticulosNorma");
        try{
            TextView tt = (TextView) findViewById(R.id.txtNoneNotas);
            switch (tipoNorma){
                case NORMA_TRANSITO:
                    myDBHelper1 = SQLiteHelperTransito.getInstance(this);
                    myListNotes = myDBHelper1.getNotes();                    
                    break;
                case NORMA_VEHICULAR:
                    myDBHelper2 = SQLiteHelperVehiculos.getInstance(this);
                    myListNotes = myDBHelper2.getNotes();
                    break;
                case NORMA_LICENCIAS:
                    myDBHelper3 = SQLiteHelperLicencias.getInstance(this);
                    myListNotes = myDBHelper3.getNotes();
                    break;
            }

            if(myListNotes.length>0){
                tt.setVisibility(View.GONE);
            }
            myList = (ListView) findViewById(R.id.lvNotes);
            myListAdapter = new AdapterListArticulos(this,myListNotes);
            myList.setAdapter(myListAdapter);
            registerForContextMenu(myList);
            myList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(getBaseContext(),"Mantener presionado para ver opciones",Toast.LENGTH_LONG).show();
                }
            });
            // Agregar el adView
            AdView adView = (AdView)this.findViewById(R.id.adViewNotes);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);

        }catch (Exception ex){
            Log.e("Debug", "MessageError: " + ex);
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
                Intent intent = new Intent(this,AddNoteActivity.class);
                intent.putExtra("numeroArticulo",numeroArticuloSeleccionado);
                intent.putExtra("nombreArticulo",nombreArticuloSeleccionado);
                intent.putExtra("tipoNorma", tipoNorma);
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
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Búsqueda...");
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener(){
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
