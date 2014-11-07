package pe.com.codespace.transito;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Carlos on 01/03/14.
 */
public class Tools {

    private static final int NORMA_TRANSITO = 1;
    private static final int NORMA_VEHICULAR = 2;
    private static final int NORMA_LICENCIAS = 3;

    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
            return true;
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
    }

    public static void MostrarFavoritos(Context context, int tipoNorma, int cantidadArticulos){
        Intent intent = new Intent(context, FavoritosActivity.class);
        intent.putExtra("tipoNorma",tipoNorma);
        intent.putExtra("cantidadArticulosNorma",cantidadArticulos);
        context.startActivity(intent);
    }

    public static void MostrarNotas(Context context, int tipoNorma, int cantidadArticulos){
        Intent intent = new Intent(context, NotesActivity.class);
        intent.putExtra("tipoNorma",tipoNorma);
        intent.putExtra("cantidadArticulosNorma",cantidadArticulos);
        context.startActivity(intent);
    }

    public static void GoTo(final Context ctx, final int tipoNorma, final int cantidadArticulos){
        AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
        alert.setTitle("Ir al Artículo");
        final EditText input = new EditText(ctx);
        //Para que acepte maximo 3 caracteres
        input.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(3),
        });
        //Para que use el softkeyborad con solo numeros
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);
        alert.setPositiveButton("Mostrar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                if(Tools.isNumeric(value)){
                    String[] articulo = null;
                    switch (tipoNorma){
                        case NORMA_TRANSITO:
                            SQLiteHelperTransito myDBHelper1;
                            myDBHelper1 = SQLiteHelperTransito.getInstance(ctx);
                            articulo = myDBHelper1.getArticulo(Float.parseFloat(value));
                            break;
                        case NORMA_VEHICULAR:
                            SQLiteHelperVehiculos myDBHelper2;
                            myDBHelper2 = SQLiteHelperVehiculos.getInstance(ctx);
                            articulo = myDBHelper2.getArticulo(Float.parseFloat(value));
                            break;
                        case NORMA_LICENCIAS:
                            SQLiteHelperLicencias myDBHelper3;
                            myDBHelper3 = SQLiteHelperLicencias.getInstance(ctx);
                            articulo = myDBHelper3.getArticulo(Float.parseFloat(value));
                            break;
                    }

                    float art = Float.parseFloat(value);
                    if(art>0 && art<=cantidadArticulos){
                        Intent intent = new Intent(ctx,TextActivity.class);
                        intent.putExtra("tipoNorma", tipoNorma);
                        intent.putExtra("cantidadArticulosNorma", cantidadArticulos);
                        intent.putExtra("titulo", Integer.parseInt(articulo[0]));
                        if(tipoNorma != NORMA_LICENCIAS) {
                            intent.putExtra("capitulo", Integer.parseInt(articulo[1]));
                            intent.putExtra("seccion", Integer.parseInt(articulo[2]));
                        }
                        intent.putExtra("gotoArticulo",Float.parseFloat(value));
                        intent.putExtra("ir",true);
                        if(ctx instanceof TextActivity) {
                            ((Activity) ctx).finish();
                        }
                        ctx.startActivity(intent);
                    }
                    else
                        Toast.makeText(ctx, "Número de artículo no válido", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(ctx,"Número de artículo no válido", Toast.LENGTH_SHORT).show();
            }
        });
        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) { }
        });
        alert.show();
    }

    public static void CopyArticuloToClipboard(Context context, int tipoNorma, float art){
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE) ;
        String[] articulo = null;
        ClipData clip = null;
        switch (tipoNorma){
            case NORMA_TRANSITO:
                SQLiteHelperTransito myDBHelper1;
                myDBHelper1 = SQLiteHelperTransito.getInstance(context);
                articulo = myDBHelper1.getArticulo(art);
                clip = ClipData.newPlainText("text",articulo[3] + " " + articulo[4] + ":" + "\n\n" + articulo[5]);
                break;
            case NORMA_VEHICULAR:
                SQLiteHelperVehiculos myDBHelper2;
                myDBHelper2 = SQLiteHelperVehiculos.getInstance(context);
                articulo = myDBHelper2.getArticulo(art);
                clip = ClipData.newPlainText("text",articulo[3] + " " + articulo[4] + ":" + "\n\n" + articulo[5]);
                break;
            case NORMA_LICENCIAS:
                SQLiteHelperLicencias myDBHelper3;
                myDBHelper3 = SQLiteHelperLicencias.getInstance(context);
                articulo = myDBHelper3.getArticulo(art);
                clip = ClipData.newPlainText("text",articulo[1] + " " + articulo[2] + ":" + "\n\n" + articulo[3]);
                break;
        }
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "El " + articulo[3] + " ha sido copiado al portapapeles.", Toast.LENGTH_LONG).show();
    }

    public static void CopyNotaToClipboard(Context context, int tipoNorma, float art){
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE) ;
        String[] nota = null;
        switch (tipoNorma){
            case NORMA_TRANSITO:
                SQLiteHelperTransito myDBHelper1;
                myDBHelper1 = SQLiteHelperTransito.getInstance(context);
                nota = myDBHelper1.getNota(art);
                break;
            case NORMA_VEHICULAR:
                SQLiteHelperVehiculos myDBHelper2;
                myDBHelper2 = SQLiteHelperVehiculos.getInstance(context);
                nota = myDBHelper2.getNota(art);
                break;
            case NORMA_LICENCIAS:
                SQLiteHelperLicencias myDBHelper3;
                myDBHelper3 = SQLiteHelperLicencias.getInstance(context);
                nota = myDBHelper3.getNota(art);
                break;
        }
        ClipData clip = ClipData.newPlainText("text",nota[0] + " " + nota[1] + ":" + "\n\n" + nota[2]);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "El " + nota[0] + " ha sido copiado al portapapeles.", Toast.LENGTH_LONG).show();
    }

    public static final void AgregarFavorito(Context context, int tipoNorma, float numArticulo, String nombreArticulo){
        boolean flag = false;
        switch (tipoNorma){
            case NORMA_TRANSITO:
                SQLiteHelperTransito myDBHelper1;
                myDBHelper1 = SQLiteHelperTransito.getInstance(context);
                if(myDBHelper1.setFavorito(numArticulo))
                    flag = true;
                break;
            case NORMA_VEHICULAR:
                SQLiteHelperVehiculos myDBHelper2;
                myDBHelper2 = SQLiteHelperVehiculos.getInstance(context);
                if(myDBHelper2.setFavorito(numArticulo))
                    flag = true;
                break;
            case NORMA_LICENCIAS:
                SQLiteHelperLicencias myDBHelper3;
                myDBHelper3 = SQLiteHelperLicencias.getInstance(context);
                if(myDBHelper3.setFavorito(numArticulo))
                    flag = true;
                break;
        }
        if(flag == true)
            Toast.makeText(context,"Se agregó " + nombreArticulo.toLowerCase() + " a Favoritos",Toast.LENGTH_LONG).show();
    }

    public static final void EliminarFavorito(final Context context, final int tipoNorma, final float numArticulo, final String nombreArticulo){
        final Intent intent = new Intent(context,FavoritosActivity.class);
        intent.putExtra("tipoNorma",tipoNorma);
        AlertDialog.Builder confirmar = new AlertDialog.Builder(context);
        confirmar.setTitle("Eliminar de Favoritos");
        confirmar.setMessage("¿Está seguro que desea quitar el " + nombreArticulo + " de Mis Favoritos?");
        confirmar.setCancelable(false);
        confirmar.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface confirmar, int i) {
            switch (tipoNorma) {
                case NORMA_TRANSITO:
                    SQLiteHelperTransito myDBHelper1;
                    myDBHelper1 = SQLiteHelperTransito.getInstance(context);
                    if (myDBHelper1.eliminarFavorito(numArticulo)) {
                        Toast.makeText(context, "Se eliminó " + nombreArticulo.toLowerCase() + " de Favoritos", Toast.LENGTH_LONG).show();
                        if(context instanceof FavoritosActivity){
                            ((Activity) context).finish();
                            context.startActivity(intent);
                        }
                    }
                    break;
                case NORMA_VEHICULAR:
                    SQLiteHelperVehiculos myDBHelper2;
                    myDBHelper2 = SQLiteHelperVehiculos.getInstance(context);
                    if (myDBHelper2.eliminarFavorito(numArticulo)) {
                        Toast.makeText(context, "Se eliminó " + nombreArticulo.toLowerCase() + " de Favoritos", Toast.LENGTH_LONG).show();
                        if(context instanceof FavoritosActivity){
                            ((Activity) context).finish();
                            context.startActivity(intent);
                        }
                    }
                    break;
                case NORMA_LICENCIAS:
                    SQLiteHelperLicencias myDBHelper3;
                    myDBHelper3 = SQLiteHelperLicencias.getInstance(context);
                    if (myDBHelper3.eliminarFavorito(numArticulo)) {
                        Toast.makeText(context, "Se eliminó " + nombreArticulo.toLowerCase() + " de Favoritos", Toast.LENGTH_LONG).show();
                        if(context instanceof FavoritosActivity){
                            ((Activity) context).finish();
                            context.startActivity(intent);
                        }
                    }
                    break;
            }
            }
        });
        confirmar.setNegativeButton("No", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface confirmar, int i){ }
        });
        confirmar.show();
    }

    public static final void AgregarNota(Context context, int tipoNorma, float numArticulo, String nombreArticulo, int cantidadArticulos){
        Intent intent1 = new Intent(context,AddNoteActivity.class);
        intent1.putExtra("numeroArticulo",numArticulo);
        intent1.putExtra("nombreArticulo",nombreArticulo);
        intent1.putExtra("tipoNorma", tipoNorma);
        intent1.putExtra("cantidadArticulosNorma", cantidadArticulos);
        context.startActivity(intent1);
    }

    public static final void EliminarNota(final Context context, final int tipoNorma, final float numArticulo, final String nombreArticulo, int cantidadArticulos){
        final Intent intent = new Intent(context,NotesActivity.class);
        intent.putExtra("tipoNorma",tipoNorma);
        intent.putExtra("cantidadArticulosNorma",cantidadArticulos);
        AlertDialog.Builder confirmar = new AlertDialog.Builder(context);
        confirmar.setTitle("Eliminar Anotación");
        confirmar.setMessage("¿Está seguro que desea eliminar la nota del " + nombreArticulo + "?");
        confirmar.setCancelable(false);
        confirmar.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface confirmar, int i) {
                switch (tipoNorma) {
                    case NORMA_TRANSITO:
                        SQLiteHelperTransito myDBHelper1;
                        myDBHelper1 = SQLiteHelperTransito.getInstance(context);
                        if (myDBHelper1.EliminarNota(numArticulo)) {
                            Toast.makeText(context, "Se eliminó la nota del " + nombreArticulo.toLowerCase() + " satisfactoriamente", Toast.LENGTH_LONG).show();
                            ((Activity) context).finish();
                            context.startActivity(intent);
                        }
                        break;
                    case NORMA_VEHICULAR:
                        SQLiteHelperVehiculos myDBHelper2;
                        myDBHelper2 = SQLiteHelperVehiculos.getInstance(context);
                        if (myDBHelper2.EliminarNota(numArticulo)) {
                            Toast.makeText(context, "Se eliminó la nota del " + nombreArticulo.toLowerCase() + " satisfactoriamente", Toast.LENGTH_LONG).show();
                            ((Activity) context).finish();
                            context.startActivity(intent);
                        }
                        break;
                    case NORMA_LICENCIAS:
                        SQLiteHelperLicencias myDBHelper3;
                        myDBHelper3 = SQLiteHelperLicencias.getInstance(context);
                        if (myDBHelper3.EliminarNota(numArticulo)) {
                            Toast.makeText(context, "Se eliminó la nota del " + nombreArticulo.toLowerCase() + " satisfactoriamente", Toast.LENGTH_LONG).show();
                            ((Activity) context).finish();
                            context.startActivity(intent);
                        }
                        break;
                }
            }
        });
        confirmar.setNegativeButton("No", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface confirmar, int i){ }
        });
        confirmar.show();
    }

    public static final void ShowNota(Context context, int tipoNorma, float numArticulo, String nombreArticulo){
        String nota = "";
        SQLiteHelperTransito myDBHelper1;
        SQLiteHelperVehiculos myDBHelper2;
        SQLiteHelperLicencias myDBHelper3;
        switch (tipoNorma){
            case NORMA_TRANSITO:
                myDBHelper1 = SQLiteHelperTransito.getInstance(context);
                nota = myDBHelper1.getNota(numArticulo)[2];
                break;
            case NORMA_VEHICULAR:
                myDBHelper2 = SQLiteHelperVehiculos.getInstance(context);
                nota = myDBHelper2.getNota(numArticulo)[2];
                break;
            case NORMA_LICENCIAS:
                myDBHelper3 = SQLiteHelperLicencias.getInstance(context);
                nota = myDBHelper3.getNota(numArticulo)[2];
                break;
            default: break;
        }
        AlertDialog.Builder dialogoNota = new AlertDialog.Builder(context);
        dialogoNota.setTitle("Notas del " + nombreArticulo);
        dialogoNota.setMessage(nota);
        dialogoNota.setCancelable(true);
        dialogoNota.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog1, int i) {
                dialog1.cancel();
            }
        });
        dialogoNota.show();
    }

    public static final void CheckInternetAccessToWebview(Context context, String url){
        final ConnectivityManager network = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = network.getActiveNetworkInfo();
        if(activeNetwork != null && activeNetwork.isConnected()){
            Intent intent = new Intent(context, WebviewActivity.class);
            intent.putExtra("url",url);
            context.startActivity(intent);
        }
        else{
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle("Error de Conexión");
            dialog.setMessage("No está conectado a internet");
            dialog.setCancelable(false);
            dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog1, int i) {
                    dialog1.cancel();
                }
            });
            dialog.show();
        }
    }

    public static final void QuerySubmit(Context context, MenuItem menuItem, int tipoNorma, int cantidadArticulos, String query){
        Intent intent = new Intent(context,SearchResultsTransitoActivity.class);
        intent.putExtra("searchText", query);
        intent.putExtra("tipoNorma", tipoNorma);
        intent.putExtra("cantidadArticulosNorma", cantidadArticulos);
        context.startActivity(intent);
        MenuItemCompat.collapseActionView(menuItem);
        //menuItem.collapseActionView();
    }

}