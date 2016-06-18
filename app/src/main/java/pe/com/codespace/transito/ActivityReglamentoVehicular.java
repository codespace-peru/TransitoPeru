package pe.com.codespace.transito;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;


public class ActivityReglamentoVehicular extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private static final int CANTIDAD_ARTICULOS_NORMA = 151;
    private List<NLevelItem> list;
    private ListView listView;
    private LayoutInflater inflater;
    private SQLiteHelperVehiculos myDBHelper;
    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reglamento);

        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!= null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.reglamento_vehiculos_title));
        }

        myDBHelper = SQLiteHelperVehiculos.getInstance(this);
        listView = (ListView) findViewById(R.id.listView1);
        list = new ArrayList<>();
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

                NLevelItem padre = (NLevelItem)obj.getParent();
                if(padre==null){
                    switch(item.Num){
                        case 1:case 3:case 4:case 5://Seccion 1, Disposic. complementarias, Anexos 1 y 2
                            Intent intent1 = new Intent(view.getContext(), ActivityText.class);
                            intent1.putExtra(MyValues.TIPO_NORMA, MyValues.NORMA_VEHICULAR);
                            intent1.putExtra(MyValues.NUMERO_SECCION, item.Num);
                            intent1.putExtra(MyValues.NUMERO_TITULO, 0);
                            intent1.putExtra(MyValues.NUMERO_CAPITULO, 0);
                            intent1.putExtra(MyValues.CANTIDAD_ARTICULOS,CANTIDAD_ARTICULOS_NORMA);
                            view.getContext().startActivity(intent1);
                            break;
                        case 6: // Anexo 3
                            Intent intent2 = new Intent(view.getContext(), ActivitymyHtmlReader.class);
                            startActivity(intent2);
                            break;
                        case 7:case 8:case 9:
                            break;
                    }
                }else{//Se abre la Seccion 2
                    Objeto objPadre = (Objeto) padre.getWrappedObject();
                    NLevelItem abuelo = (NLevelItem)obj.getParent().getParent();
                    if(abuelo==null){
                        if(item.Num==1 || item.Num==2 || item.Num==3 || item.Num==4 | item.Num==8) {
                            Intent intent2 = new Intent(view.getContext(), ActivityText.class);
                            intent2.putExtra(MyValues.TIPO_NORMA, MyValues.NORMA_VEHICULAR);
                            intent2.putExtra(MyValues.CANTIDAD_ARTICULOS,CANTIDAD_ARTICULOS_NORMA);
                            intent2.putExtra(MyValues.NUMERO_SECCION, objPadre.Num);
                            intent2.putExtra(MyValues.NUMERO_TITULO, item.Num);
                            intent2.putExtra(MyValues.NUMERO_CAPITULO, 0);
                            view.getContext().startActivity(intent2);
                        }
                    }else{
                        Objeto objAbuelo = (Objeto) abuelo.getWrappedObject();
                        Intent intent = new Intent(view.getContext(), ActivityText.class);
                        intent.putExtra(MyValues.TIPO_NORMA, MyValues.NORMA_VEHICULAR);
                        intent.putExtra(MyValues.CANTIDAD_ARTICULOS,CANTIDAD_ARTICULOS_NORMA);
                        intent.putExtra(MyValues.NUMERO_SECCION, objAbuelo.Num);
                        intent.putExtra(MyValues.NUMERO_TITULO, objPadre.Num);
                        intent.putExtra(MyValues.NUMERO_CAPITULO, item.Num);
                        view.getContext().startActivity(intent);
                    }
                }
            }
        });

        // Agregar el adView
        AdView adView = (AdView)this.findViewById(R.id.adViewReglamento);
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
        final MenuItem searchItem = menu.findItem(R.id.action_search);
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
            case R.id.action_goto:
                Tools.GoTo(this,MyValues.NORMA_VEHICULAR,CANTIDAD_ARTICULOS_NORMA);
                break;
            case R.id.action_favorites:
                Tools.MostrarFavoritos(this, MyValues.NORMA_VEHICULAR, CANTIDAD_ARTICULOS_NORMA);
                break;
            case R.id.action_notes:
                Tools.MostrarNotas(this, MyValues.NORMA_VEHICULAR, CANTIDAD_ARTICULOS_NORMA);
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


    private void prepararData(){
        String[][] secciones =  myDBHelper.getSecciones();
        Objeto obj1;
        Objeto obj2;
        Objeto obj3;
        for (int i = 1; i <= secciones.length; i++) {
            obj1 = new Objeto();
            obj1.Nivel = 1;
            obj1.Num = Integer.parseInt(secciones[i-1][0]);
            obj1.Title = secciones[i-1][1];
            obj1.Description = secciones[i-1][2];
            final NLevelItem grandParent = new NLevelItem(obj1, null, new NLevelView() {
                @Override
                public View getView(NLevelItem item) {
                    View view = inflater.inflate(R.layout.explistview_group1, null);
                    ViewHolder holder = new ViewHolder(view);
                    holder.myTitle.setText(((Objeto) item.getWrappedObject()).Title);
                    holder.myDescription.setText(((Objeto) item.getWrappedObject()).Description);
                    holder.myTitle.setTextColor(Color.BLACK);
                    holder.myDescription.setTextColor(Color.BLACK);
                    view.setPadding(10,0,0,0);
                    return view;
                }
            });

            list.add(grandParent);
            String[][] titulos = myDBHelper.getTitulos(i);
            int numChildren = titulos.length;
            for (int j = 1; j <= numChildren; j++) {
                obj2 = new Objeto();
                obj2.Nivel=2;
                obj2.Num = Integer.parseInt(titulos[j-1][0]);
                obj2.Title = titulos[j-1][1];
                obj2.Description = titulos[j-1][2];
                final NLevelItem parent = new NLevelItem(obj2, grandParent, new NLevelView() {
                    @Override
                    public View getView(NLevelItem item) {
                        View view = inflater.inflate(R.layout.explistview_group1, null);
                        ViewHolder holder = new ViewHolder(view);
                        holder.myTitle.setText(((Objeto) item.getWrappedObject()).Title);
                        holder.myDescription.setText(((Objeto) item.getWrappedObject()).Description);
                        holder.myTitle.setTextColor(Color.BLACK);
                        holder.myDescription.setTextColor(Color.BLACK);
                        view.setPadding(20, 0, 0, 0);
                        return view;
                    }
                });
                list.add(parent);
                String[][] capitulos = myDBHelper.getCapitulos(i,j);
                int grandChildren = capitulos.length;
                for (int k = 1; k <= grandChildren; k++) {
                    obj3 = new Objeto();
                    obj3.Nivel = 3;
                    obj3.Num = Integer.parseInt(capitulos[k-1][0]);
                    obj3.Title = capitulos[k-1][1];
                    obj3.Description = capitulos[k-1][2];
                    NLevelItem child = new NLevelItem(obj3, parent, new NLevelView() {
                        @Override
                        public View getView(NLevelItem item) {
                            View view = inflater.inflate(R.layout.explistview_group1, null);
                            ViewHolder holder = new ViewHolder(view);
                            holder.myTitle.setText(((Objeto) item.getWrappedObject()).Title);
                            holder.myDescription.setText(((Objeto) item.getWrappedObject()).Description);
                            holder.myTitle.setTextColor(Color.BLACK);
                            holder.myDescription.setTextColor(Color.BLACK);
                            view.setPadding(30, 0, 0, 0);
                            return view;
                        }
                    });
                    list.add(child);
                }
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        Tools.QuerySubmit(this,menuItem,MyValues.NORMA_VEHICULAR,CANTIDAD_ARTICULOS_NORMA,s);
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

}
