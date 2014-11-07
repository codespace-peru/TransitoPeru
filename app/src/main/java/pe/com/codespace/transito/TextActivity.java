package pe.com.codespace.transito;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
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

public class TextActivity extends ActionBarActivity implements SearchView.OnQueryTextListener {
    private static final int NORMA_TRANSITO = 1; //Codigo de Transito
    private static final int NORMA_VEHICULAR = 2; //Reglamento Nacional Vehicular
    private static final int NORMA_LICENCIAS = 3; //Reglamento de Licencias de Conducir
    private SearchView searchView;
    AdapterListArticulos myListAdapter;
    ListView myList;
    SQLiteHelperTransito myDBHelper1;
    SQLiteHelperVehiculos myDBHelper2;
    SQLiteHelperLicencias myDBHelper3;
    String[][] LstArticulos;
    String nombreArticuloSeleccionado="";
    float numeroArticuloSeleccionado = -1;
    MenuItem menuItem;
    int tipoNorma, cantidadArticulosNorma = 0;
    int seccion, titulo, capitulo;
    boolean ir = false;
    int primerArticulo;
    int gotoArticulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        Intent intent = getIntent();
        tipoNorma = intent.getExtras().getInt("tipoNorma");
        titulo = intent.getExtras().getInt("titulo");
        capitulo = intent.getExtras().getInt("capitulo");
        seccion = intent.getExtras().getInt("seccion");
        ir = intent.getExtras().getBoolean("ir");
        cantidadArticulosNorma = intent.getExtras().getInt("cantidadArticulosNorma");
        TextView myText1;
        TextView myText2;
        TextView myText3;

        myText1 = (TextView) findViewById(R.id.tvRootTitle);
        myText2 = (TextView) findViewById(R.id.tvTitle);
        myText3 = (TextView) findViewById(R.id.tvSubTitle);
        // Agregar el adView
        AdView adView = (AdView)this.findViewById(R.id.adViewText);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        try {
            switch(tipoNorma){
                case NORMA_TRANSITO:
                    myDBHelper1 = SQLiteHelperTransito.getInstance(this);
                    String[] tit1 = myDBHelper1.getTitulo(titulo);
                    String[] cap1 = myDBHelper1.getCapitulo(titulo,capitulo);
                    String[] sec1 = myDBHelper1.getSeccion(titulo,capitulo,seccion);
                    LstArticulos = myDBHelper1.getListaArticulos(titulo,capitulo,seccion);
                    if(titulo!=8 && titulo!=9)
                        myText1.setText(tit1[0] + ": " + tit1[1]);
                    else
                        myText1.setText(tit1[0]);

                    if(cap1[0]!=null)
                        myText2.setText(cap1[0] + ": " + cap1[1]);
                    else
                        myText2.setVisibility(View.GONE);

                    if(sec1[0]!=null)
                        myText3.setText(sec1[0] + ": " + sec1[1]);
                    else
                        myText3.setVisibility(View.GONE);
                    break;
                case NORMA_VEHICULAR:
                    myDBHelper2 = SQLiteHelperVehiculos.getInstance(this);
                    String[] sec2 = myDBHelper2.getSeccion(seccion);
                    String[] tit2 = myDBHelper2.getTitulo(seccion,titulo);
                    String[] cap2 = myDBHelper2.getCapitulo(seccion,titulo,capitulo);
                    LstArticulos = myDBHelper2.getListaArticulos(seccion,titulo,capitulo);
                    myText1.setText(sec2[0] + ": " + sec2[1]);
                    if(tit2[0]!=null)
                        myText2.setText(tit2[0] + ": " + tit2[1]);
                    else
                        myText2.setVisibility(View.GONE);

                    if(cap2[0]!=null)
                        myText3.setText(cap2[0] + ": " + cap2[1]);
                    else
                        myText3.setVisibility(View.GONE);
                    break;
                case NORMA_LICENCIAS:
                    myDBHelper3 = SQLiteHelperLicencias.getInstance(this);
                    String[] tit3 = myDBHelper3.getTitulo(titulo);
                    LstArticulos = myDBHelper3.getListaArticulos(titulo);
                    myText1.setText(tit3[0] + ": " + tit3[1]);
                    myText2.setVisibility(View.GONE);
                    myText3.setVisibility(View.GONE);
                    break;
            }

            myList = (ListView) findViewById(R.id.lvText);
            myListAdapter = new AdapterListArticulos(this,LstArticulos);
            myList.setAdapter(myListAdapter);
            if(ir==true){
                String tt = LstArticulos[0][0];
                primerArticulo = Integer.parseInt(tt);
                float temp = intent.getExtras().getFloat("gotoArticulo");
                gotoArticulo = Math.round((int) temp);
                myList.setSelection(gotoArticulo-primerArticulo + 1);
            }
            registerForContextMenu(myList);
            myList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(getBaseContext(), "Mantener presionado para ver opciones", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        };
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu,view,menuInfo);
        MenuInflater inflater = getMenuInflater();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        String temp = ((TextView) info.targetView.findViewById(R.id.tvNumberItem)).getText().toString();
        nombreArticuloSeleccionado = ((TextView) info.targetView.findViewById(R.id.tvTitleItem)).getText().toString();
        numeroArticuloSeleccionado = Float.parseFloat(temp);
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
            case  NORMA_VEHICULAR:
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
            case  NORMA_LICENCIAS:
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
        try{
            switch (item.getItemId()) {
                case R.id.CtxAddFavorito:
                    Tools.AgregarFavorito(this, tipoNorma, numeroArticuloSeleccionado, nombreArticuloSeleccionado);
                    return true;
                case R.id.CtxDelFavorito:
                    Tools.EliminarFavorito(this,tipoNorma, numeroArticuloSeleccionado, nombreArticuloSeleccionado);
                    return true;
                case R.id.CtxShowNote:
                    Tools.ShowNota(this, tipoNorma, numeroArticuloSeleccionado, nombreArticuloSeleccionado);
                    return true;
                case R.id.CtxAddNote: case R.id.CtxEditNote:
                    Tools.AgregarNota(this, tipoNorma, numeroArticuloSeleccionado, nombreArticuloSeleccionado, cantidadArticulosNorma);
                    return true;
                case R.id.CtxCopyArticulo:
                    Tools.CopyArticuloToClipboard(this, tipoNorma, numeroArticuloSeleccionado);
                    return true;
                default:
                    return super.onContextItemSelected(item);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar_main, menu);
        final MenuItem searchItem;
        searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Búsqueda...");
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View view, boolean b) {
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
            case R.id.action_goto:
                Tools.GoTo(this,tipoNorma,cantidadArticulosNorma);
                break;
            case R.id.action_favorites:
                Tools.MostrarFavoritos(this,tipoNorma, cantidadArticulosNorma);
                break;
            case R.id.action_notes:
                Tools.MostrarNotas(this,tipoNorma, cantidadArticulosNorma);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        Tools.QuerySubmit(this, menuItem,tipoNorma,cantidadArticulosNorma, s);
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
