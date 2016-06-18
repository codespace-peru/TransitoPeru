package pe.com.codespace.transito;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;


/**
 * Creado por Carlos el 01/03/14.
 */
public class ActivityAddNote extends AppCompatActivity {
    private SQLiteHelperTransito myDBHelper1;
    private SQLiteHelperVehiculos myDBHelper2;
    private SQLiteHelperLicencias myDBHelper3;
    private String nota = "";
    private float numArticulo = -1;
    private int tipoNorma = 0;
    private String nombreArticulo = "";
    private EditText editText;
    private boolean modify = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnote);

        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.add_note));
        }

        try{
            Intent intent = getIntent();
            numArticulo = intent.getExtras().getFloat(MyValues.NUMERO_ARTICULO);
            nombreArticulo = intent.getExtras().getString(MyValues.NOMBRE_ARTICULO);
            tipoNorma = intent.getExtras().getInt(MyValues.TIPO_NORMA);
            TextView textView = (TextView) findViewById(R.id.tvAddNota);
            textView.setText("Nota para el " + nombreArticulo + ":");
            editText = (EditText) findViewById(R.id.edtAddNota);
            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
                    }
            });

            switch (tipoNorma){
                case MyValues.NORMA_TRANSITO:
                    myDBHelper1 = SQLiteHelperTransito.getInstance(this);
                    if(myDBHelper1.hay_nota(numArticulo)){
                        getSupportActionBar().setTitle(getResources().getString(R.string.edit_note));
                        nota = myDBHelper1.getNota(numArticulo)[2];
                        editText.setText(nota);
                        int end = editText.getText().length();
                        editText.setSelection(end); // Colocar el cursor al final
                        modify=true;
                    }
                    break;
                case MyValues.NORMA_VEHICULAR:
                    myDBHelper2 = SQLiteHelperVehiculos.getInstance(this);
                    if(myDBHelper2.hay_nota(numArticulo)){
                        getSupportActionBar().setTitle(getResources().getString(R.string.edit_note));
                        nota = myDBHelper2.getNota(numArticulo)[2];
                        editText.setText(nota);
                        int end = editText.getText().length();
                        editText.setSelection(end); // Colocar el cursor al final
                        modify=true;
                    }
                    break;
                case MyValues.NORMA_LICENCIAS:
                    myDBHelper3 = SQLiteHelperLicencias.getInstance(this);
                    if(myDBHelper3.hay_nota(numArticulo)){
                        getSupportActionBar().setTitle(getResources().getString(R.string.edit_note));
                        nota = myDBHelper3.getNota(numArticulo)[2];
                        editText.setText(nota);
                        int end = editText.getText().length();
                        editText.setSelection(end); // Colocar el cursor al final
                        modify=true;
                    }
                    break;
                default: break;
            }


            // Agregar el adView
            AdView adView = (AdView)this.findViewById(R.id.adViewAddNote);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar_addnotes, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_saveNota:
                nota = editText.getText().toString();
                switch (tipoNorma){
                    case MyValues.NORMA_TRANSITO:
                        if(modify){
                            if(myDBHelper1.UpdateNota(numArticulo,nota)) {
                                Toast.makeText(ActivityAddNote.this,"Se modificó la nota de " + nombreArticulo,Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            if(myDBHelper1.AddNota(numArticulo,nota)) {
                                Toast.makeText(ActivityAddNote.this,"Se agregó la nota a " + nombreArticulo,Toast.LENGTH_LONG).show();
                            }
                        }
                        break;
                    case MyValues.NORMA_VEHICULAR:
                        if(modify){
                            if(myDBHelper2.UpdateNota(numArticulo,nota)) {
                                Toast.makeText(ActivityAddNote.this,"Se modificó la nota de " + nombreArticulo,Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            if(myDBHelper2.AddNota(numArticulo,nota)) {
                                Toast.makeText(ActivityAddNote.this,"Se agregó la nota a " + nombreArticulo,Toast.LENGTH_LONG).show();
                            }
                        }
                        break;
                    case MyValues.NORMA_LICENCIAS:
                        if(modify){
                            if(myDBHelper3.UpdateNota(numArticulo,nota)) {
                                Toast.makeText(ActivityAddNote.this,"Se modificó la nota de " + nombreArticulo,Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            if(myDBHelper3.AddNota(numArticulo,nota)) {
                                Toast.makeText(ActivityAddNote.this,"Se agregó la nota a " + nombreArticulo,Toast.LENGTH_LONG).show();
                            }
                        }
                        break;
                    default: break;
                }
                this.finish();
                break;
            case R.id.action_cancelarNota:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
