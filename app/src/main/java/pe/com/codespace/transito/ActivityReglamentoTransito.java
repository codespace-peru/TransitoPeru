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


public class ActivityReglamentoTransito extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private static final int CANTIDAD_ARTICULOS_NORMA = 343;
    private List<NLevelItem> list;
    private ListView listView;
    private LayoutInflater inflater;
    private SQLiteHelperTransito myDBHelper;
    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reglamento);
        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.reglamento_transito_title));
        }

        myDBHelper = SQLiteHelperTransito.getInstance(this);
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

                Intent intent = new Intent(view.getContext(), ActivityText.class);
                intent.putExtra(MyValues.TIPO_NORMA, MyValues.NORMA_TRANSITO);
                intent.putExtra(MyValues.CANTIDAD_ARTICULOS, CANTIDAD_ARTICULOS_NORMA);

                if(padre==null){
                    switch(item.Num){
                        case 2:case 6:case 8: //Titulos 2,6 y 8
                            intent.putExtra(MyValues.NUMERO_TITULO, item.Num);
                            intent.putExtra(MyValues.NUMERO_CAPITULO, 0);
                            intent.putExtra(MyValues.NUMERO_SECCION, 0);
                            view.getContext().startActivity(intent);
                            break;
                        case 9:case 10: // Anexos I y II
                            Intent intent2 = new Intent(view.getContext(), ActivityMultasTransito.class);
                            intent2.putExtra(MyValues.TIPO_NORMA, MyValues.NORMA_TRANSITO);
                            intent2.putExtra(MyValues.CANTIDAD_ARTICULOS, CANTIDAD_ARTICULOS_NORMA);
                            intent2.putExtra(MyValues.ANEXO, item.Num);
                            view.getContext().startActivity(intent2);
                            break;
                    }
                }else{
                    Objeto objPadre = (Objeto) padre.getWrappedObject();
                    NLevelItem abuelo = (NLevelItem)obj.getParent().getParent();
                    if(abuelo==null){
                        if(objPadre.Num==1 || objPadre.Num==3 && item.Num==1 ||
                           objPadre.Num==4 && item.Num==1 || objPadre.Num==5 ||
                           objPadre.Num==7 && (item.Num==2 || item.Num==4) ) {
                            intent.putExtra(MyValues.NUMERO_TITULO, objPadre.Num);
                            intent.putExtra(MyValues.NUMERO_CAPITULO, item.Num);
                            intent.putExtra(MyValues.NUMERO_SECCION, 0);
                            view.getContext().startActivity(intent);

                        }
                    }else{
                        Objeto objAbuelo = (Objeto) abuelo.getWrappedObject();
                        intent.putExtra(MyValues.NUMERO_TITULO, objAbuelo.Num);
                        intent.putExtra(MyValues.NUMERO_CAPITULO, objPadre.Num);
                        intent.putExtra(MyValues.NUMERO_SECCION, item.Num);
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
                Tools.GoTo(this,MyValues.NORMA_TRANSITO,CANTIDAD_ARTICULOS_NORMA);
                break;
            case R.id.action_favorites:
                Tools.MostrarFavoritos(this,MyValues.NORMA_TRANSITO,CANTIDAD_ARTICULOS_NORMA);
                break;
            case R.id.action_notes:
                Tools.MostrarNotas(this,MyValues.NORMA_TRANSITO, CANTIDAD_ARTICULOS_NORMA);
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
        String[][] titulos =  myDBHelper.getTitulos();
        Objeto obj1;
        Objeto obj2;
        Objeto obj3;
        for (int i = 1; i <= titulos.length; i++) {
            obj1 = new Objeto();
            obj1.Nivel = 1;
            obj1.Num = Integer.parseInt(titulos[i-1][0]);
            obj1.Title = titulos[i-1][1];
            obj1.Description = titulos[i-1][2];
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
            String[][] capitulos = myDBHelper.getCapitulos(i);
            int numChildren = capitulos.length;
            for (int j = 1; j <= numChildren; j++) {
                obj2 = new Objeto();
                obj2.Nivel = 2;
                obj2.Num = Integer.parseInt(capitulos[j-1][0]);
                obj2.Title = capitulos[j-1][1];
                obj2.Description = capitulos[j-1][2];
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
                String[][] secciones = myDBHelper.getSecciones(i,j);
                int grandChildren = secciones.length;
                for (int k = 1; k <= grandChildren; k++) {
                    obj3 = new Objeto();
                    obj3.Nivel = 3;
                    obj3.Num = Integer.parseInt(secciones[k-1][0]);
                    obj3.Title = secciones[k-1][1];
                    obj3.Description = secciones[k-1][2];
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
        Tools.QuerySubmit(this,menuItem,MyValues.NORMA_TRANSITO, CANTIDAD_ARTICULOS_NORMA, s);
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
