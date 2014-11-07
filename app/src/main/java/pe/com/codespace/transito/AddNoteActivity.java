package pe.com.codespace.transito;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.analytics.tracking.android.EasyTracker;

/**
 * Created by Carlos on 01/03/14.
 */
public class AddNoteActivity extends ActionBarActivity {
    SQLiteHelperTransito myDBHelper1;
    SQLiteHelperVehiculos myDBHelper2;
    SQLiteHelperLicencias myDBHelper3;
    private static final int NORMA_TRANSITO = 1;
    private static final int NORMA_VEHICULAR = 2;
    private static final int NORMA_LICENCIAS = 3;
    String nota = "";
    float numArticulo = -1;
    int tipoNorma = 0;
    String nombreArticulo = "";
    EditText editText;
    boolean modify = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnote);
        try{
            Intent intent = getIntent();
            numArticulo = intent.getExtras().getFloat("numeroArticulo");
            nombreArticulo = intent.getExtras().getString("nombreArticulo");
            tipoNorma = intent.getExtras().getInt("tipoNorma");
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
                case NORMA_TRANSITO:
                    myDBHelper1 = SQLiteHelperTransito.getInstance(this);
                    if(myDBHelper1.hay_nota(numArticulo)){
                        TextView textView1 = (TextView) findViewById(R.id.addnote_title);
                        textView1.setText("MODIFICAR NOTA");
                        nota = myDBHelper1.getNota(numArticulo)[2];
                        editText.setText(nota);
                        int end = editText.getText().length();
                        editText.setSelection(end); // Colocar el cursor al final
                        modify=true;
                    }
                    else{
                        TextView textView1 = (TextView) findViewById(R.id.addnote_title);
                        textView1.setText("AGREGAR NOTA");
                    }
                    break;
                case NORMA_VEHICULAR:
                    myDBHelper2 = SQLiteHelperVehiculos.getInstance(this);
                    if(myDBHelper2.hay_nota(numArticulo)){
                        TextView textView1 = (TextView) findViewById(R.id.addnote_title);
                        textView1.setText("MODIFICAR NOTA");
                        nota = myDBHelper2.getNota(numArticulo)[2];
                        editText.setText(nota);
                        int end = editText.getText().length();
                        editText.setSelection(end); // Colocar el cursor al final
                        modify=true;
                    }
                    else{
                        TextView textView1 = (TextView) findViewById(R.id.addnote_title);
                        textView1.setText("AGREGAR NOTA");
                    }
                    break;
                case NORMA_LICENCIAS:
                    myDBHelper3 = SQLiteHelperLicencias.getInstance(this);
                    if(myDBHelper3.hay_nota(numArticulo)){
                        TextView textView1 = (TextView) findViewById(R.id.addnote_title);
                        textView1.setText("MODIFICAR NOTA");
                        nota = myDBHelper3.getNota(numArticulo)[2];
                        editText.setText(nota);
                        int end = editText.getText().length();
                        editText.setSelection(end); // Colocar el cursor al final
                        modify=true;
                    }
                    else{
                        TextView textView1 = (TextView) findViewById(R.id.addnote_title);
                        textView1.setText("AGREGAR NOTA");
                    }
                    break;
                default: break;
            }


            // Agregar el adView
            AdView adView = (AdView)this.findViewById(R.id.adViewAddNote);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);

        }catch (Exception ex){
            Log.e("Debug", "MessageError: " + ex);
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
                    case NORMA_TRANSITO:
                        if(modify==true){
                            if(myDBHelper1.UpdateNota(numArticulo,nota)) {
                                Toast.makeText(AddNoteActivity.this,"Se modificó la nota de " + nombreArticulo,Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            if(myDBHelper1.AddNota(numArticulo,nota)) {
                                Toast.makeText(AddNoteActivity.this,"Se agregó la nota a " + nombreArticulo,Toast.LENGTH_LONG).show();
                            }
                        }
                        break;
                    case NORMA_VEHICULAR:
                        if(modify==true){
                            if(myDBHelper2.UpdateNota(numArticulo,nota)) {
                                Toast.makeText(AddNoteActivity.this,"Se modificó la nota de " + nombreArticulo,Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            if(myDBHelper2.AddNota(numArticulo,nota)) {
                                Toast.makeText(AddNoteActivity.this,"Se agregó la nota a " + nombreArticulo,Toast.LENGTH_LONG).show();
                            }
                        }
                        break;
                    case NORMA_LICENCIAS:
                        if(modify==true){
                            if(myDBHelper3.UpdateNota(numArticulo,nota)) {
                                Toast.makeText(AddNoteActivity.this,"Se modificó la nota de " + nombreArticulo,Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            if(myDBHelper3.AddNota(numArticulo,nota)) {
                                Toast.makeText(AddNoteActivity.this,"Se agregó la nota a " + nombreArticulo,Toast.LENGTH_LONG).show();
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
