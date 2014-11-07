package pe.com.codespace.transito;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;


public class ReglamentoLicenciasActivity extends ActionBarActivity implements SearchView.OnQueryTextListener {

    private static final int TIPO_NORMA = 3;
    private static final int CANTIDAD_ARTICULOS_NORMA = 126;
    private SearchView searchView;
    List<NLevelItem> list;
    ListView listView;
    LayoutInflater inflater;
    SQLiteHelperLicencias myDBHelper;
    MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reglamento);
        myDBHelper = SQLiteHelperLicencias.getInstance(this);
        listView = (ListView) findViewById(R.id.listView1);
        list = new ArrayList<NLevelItem>();
        Intent intent = getIntent();
        String titulo = intent.getExtras().getString("title");
        TextView textView = (TextView) findViewById(R.id.titleNorma);
        textView.setText(titulo);
        inflater = LayoutInflater.from(this);
        prepararData();
        NLevelAdapter adapter = new NLevelAdapter(list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int i,long arg3) {
                ((NLevelAdapter) listView.getAdapter()).toggle(i);
                ((NLevelAdapter) listView.getAdapter()).getFilter().filter();
                NLevelItem obj = (NLevelItem )listView.getAdapter().getItem(i);
                Objeto item = (Objeto)obj.getWrappedObject();

                if(item.Num <= 17) {
                    Intent intent1 = new Intent(view.getContext(), TextActivity.class);
                    intent1.putExtra("tipoNorma", TIPO_NORMA);
                    intent1.putExtra("cantidadArticulosNorma", CANTIDAD_ARTICULOS_NORMA);
                    intent1.putExtra("titulo", item.Num);
                    view.getContext().startActivity(intent1);
                }else{
                    Intent intent2 = new Intent(view.getContext(), InfraccionesLicenciasActivity.class);
                    view.getContext().startActivity(intent2);

                }

            }
        });
        // Agregar el adView
        AdView adView = (AdView)this.findViewById(R.id.adViewReglamento);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar_main, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
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
            case R.id.action_goto:
                Tools.GoTo(this,TIPO_NORMA,CANTIDAD_ARTICULOS_NORMA);
                break;
            case R.id.action_favorites:
                Tools.MostrarFavoritos(this,TIPO_NORMA,CANTIDAD_ARTICULOS_NORMA);
                break;
            case R.id.action_notes:
                Tools.MostrarNotas(this,TIPO_NORMA, CANTIDAD_ARTICULOS_NORMA);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void prepararData(){
        String[][] titulos =  myDBHelper.getTitulos();
        Objeto obj1;
        for (int i = 1; i <= titulos.length; i++) {
            obj1 = new Objeto();
            obj1.Nivel = 1;
            obj1.Num = Integer.parseInt(titulos[i-1][0]);
            obj1.Title = titulos[i-1][1];
            obj1.Description = titulos[i-1][2];
            final NLevelItem grandParent = new NLevelItem(obj1, null, new NLevelView() {
                @Override
                public View getView(NLevelItem item) {
                    View view = inflater.inflate(R.layout.explistview_group, null);
                    ViewHolder holder = new ViewHolder(view);
                    holder.myTitle.setText(((Objeto) item.getWrappedObject()).Title);
                    holder.myDescription.setText(((Objeto) item.getWrappedObject()).Description);
                    holder.myTitle.setTextColor(Color.BLACK);
                    holder.myDescription.setTextColor(Color.BLACK);
                    view.setPadding(5,0,0,0);
                    return view;
                }
            });

            list.add(grandParent);



        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        Tools.QuerySubmit(this,menuItem,TIPO_NORMA, CANTIDAD_ARTICULOS_NORMA, s);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    private static class ViewHolder{
        TextView myTitle;
        TextView myDescription;
        ViewHolder(View v){
            myTitle = (TextView) v.findViewById(R.id.tvTitleGroup);
            myDescription = (TextView) v.findViewById(R.id.tvDescriptionGroup);
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
