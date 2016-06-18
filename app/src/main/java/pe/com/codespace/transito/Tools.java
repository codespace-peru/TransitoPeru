package pe.com.codespace.transito;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.view.MenuItemCompat;
import android.text.InputFilter;
import android.text.InputType;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Creado por Carlos el 01/03/14.
 */
public class Tools {

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
        Intent intent = new Intent(context, ActivityFavoritos.class);
        intent.putExtra(MyValues.TIPO_NORMA,tipoNorma);
        intent.putExtra(MyValues.CANTIDAD_ARTICULOS,cantidadArticulos);
        context.startActivity(intent);
    }

    public static void MostrarNotas(Context context, int tipoNorma, int cantidadArticulos){
        Intent intent = new Intent(context, ActivityNotes.class);
        intent.putExtra(MyValues.TIPO_NORMA,tipoNorma);
        intent.putExtra(MyValues.CANTIDAD_ARTICULOS,cantidadArticulos);
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
                        case MyValues.NORMA_TRANSITO:
                            SQLiteHelperTransito myDBHelper1;
                            myDBHelper1 = SQLiteHelperTransito.getInstance(ctx);
                            articulo = myDBHelper1.getArticulo(Float.parseFloat(value));
                            break;
                        case MyValues.NORMA_VEHICULAR:
                            SQLiteHelperVehiculos myDBHelper2;
                            myDBHelper2 = SQLiteHelperVehiculos.getInstance(ctx);
                            articulo = myDBHelper2.getArticulo(Float.parseFloat(value));
                            break;
                        case MyValues.NORMA_LICENCIAS:
                            SQLiteHelperLicencias myDBHelper3;
                            myDBHelper3 = SQLiteHelperLicencias.getInstance(ctx);
                            articulo = myDBHelper3.getArticulo(Float.parseFloat(value));
                            break;
                    }

                    float art = Float.parseFloat(value);
                    if(art>0 && art<=cantidadArticulos){
                        Intent intent = new Intent(ctx,ActivityText.class);
                        intent.putExtra(MyValues.TIPO_NORMA, tipoNorma);
                        intent.putExtra(MyValues.CANTIDAD_ARTICULOS, cantidadArticulos);
                        if(articulo!=null) {
                            intent.putExtra(MyValues.NUMERO_TITULO, Integer.parseInt(articulo[0]));//0
                        }
                        if(tipoNorma != MyValues.NORMA_LICENCIAS && articulo != null) {
                            intent.putExtra(MyValues.NUMERO_CAPITULO, Integer.parseInt(articulo[1]));//1
                            intent.putExtra(MyValues.NUMERO_SECCION, Integer.parseInt(articulo[2]));//2
                        }
                        intent.putExtra(MyValues.GOTO_ARTICULO,Float.parseFloat(value));
                        intent.putExtra(MyValues.GOTO_BOOLEAN,true);
                        if(ctx instanceof ActivityText) {
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
            case MyValues.NORMA_TRANSITO:
                SQLiteHelperTransito myDBHelper1;
                myDBHelper1 = SQLiteHelperTransito.getInstance(context);
                articulo = myDBHelper1.getArticulo(art);
                clip = ClipData.newPlainText("text",articulo[3] + " " + articulo[4] + ":" + "\n\n" + articulo[5]);
                break;
            case MyValues.NORMA_VEHICULAR:
                SQLiteHelperVehiculos myDBHelper2;
                myDBHelper2 = SQLiteHelperVehiculos.getInstance(context);
                articulo = myDBHelper2.getArticulo(art);
                clip = ClipData.newPlainText("text",articulo[3] + " " + articulo[4] + ":" + "\n\n" + articulo[5]);
                break;
            case MyValues.NORMA_LICENCIAS:
                SQLiteHelperLicencias myDBHelper3;
                myDBHelper3 = SQLiteHelperLicencias.getInstance(context);
                articulo = myDBHelper3.getArticulo(art);
                clip = ClipData.newPlainText("text",articulo[1] + " " + articulo[2] + ":" + "\n\n" + articulo[3]);
                break;
        }
        clipboard.setPrimaryClip(clip);
        if(articulo!=null) {
            Toast.makeText(context, "El " + articulo[3] + " ha sido copiado al portapapeles.", Toast.LENGTH_LONG).show();
        }
    }

    public static void CopyNotaToClipboard(Context context, int tipoNorma, float art){
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE) ;
        String[] nota = null;
        switch (tipoNorma){
            case MyValues.NORMA_TRANSITO:
                SQLiteHelperTransito myDBHelper1;
                myDBHelper1 = SQLiteHelperTransito.getInstance(context);
                nota = myDBHelper1.getNota(art);
                break;
            case MyValues.NORMA_VEHICULAR:
                SQLiteHelperVehiculos myDBHelper2;
                myDBHelper2 = SQLiteHelperVehiculos.getInstance(context);
                nota = myDBHelper2.getNota(art);
                break;
            case MyValues.NORMA_LICENCIAS:
                SQLiteHelperLicencias myDBHelper3;
                myDBHelper3 = SQLiteHelperLicencias.getInstance(context);
                nota = myDBHelper3.getNota(art);
                break;
        }
        if(nota != null) {
            ClipData clip = ClipData.newPlainText("text", nota[0] + " " + nota[1] + ":" + "\n\n" + nota[2]);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "El " + nota[0] + " ha sido copiado al portapapeles.", Toast.LENGTH_LONG).show();
        }
    }

    public static void AgregarFavorito(Context context, int tipoNorma, float numArticulo, String nombreArticulo){
        boolean flag = false;
        switch (tipoNorma){
            case MyValues.NORMA_TRANSITO:
                SQLiteHelperTransito myDBHelper1;
                myDBHelper1 = SQLiteHelperTransito.getInstance(context);
                if(myDBHelper1.setFavorito(numArticulo))
                    flag = true;
                break;
            case MyValues.NORMA_VEHICULAR:
                SQLiteHelperVehiculos myDBHelper2;
                myDBHelper2 = SQLiteHelperVehiculos.getInstance(context);
                if(myDBHelper2.setFavorito(numArticulo))
                    flag = true;
                break;
            case MyValues.NORMA_LICENCIAS:
                SQLiteHelperLicencias myDBHelper3;
                myDBHelper3 = SQLiteHelperLicencias.getInstance(context);
                if(myDBHelper3.setFavorito(numArticulo))
                    flag = true;
                break;
        }
        if(flag)
            Toast.makeText(context,"Se agregó " + nombreArticulo.toLowerCase() + " a Favoritos",Toast.LENGTH_LONG).show();
    }

    public static void EliminarFavorito(final Context context, final int tipoNorma, final float numArticulo, final String nombreArticulo){
        final Intent intent = new Intent(context,ActivityFavoritos.class);
        intent.putExtra(MyValues.TIPO_NORMA,tipoNorma);
        AlertDialog.Builder confirmar = new AlertDialog.Builder(context);
        confirmar.setTitle("Eliminar de Favoritos");
        confirmar.setMessage("¿Está seguro que desea quitar el " + nombreArticulo + " de Mis Favoritos?");
        confirmar.setCancelable(false);
        confirmar.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface confirmar, int i) {
            switch (tipoNorma) {
                case MyValues.NORMA_TRANSITO:
                    SQLiteHelperTransito myDBHelper1;
                    myDBHelper1 = SQLiteHelperTransito.getInstance(context);
                    if (myDBHelper1.eliminarFavorito(numArticulo)) {
                        Toast.makeText(context, "Se eliminó " + nombreArticulo.toLowerCase() + " de Favoritos", Toast.LENGTH_LONG).show();
                        if(context instanceof ActivityFavoritos){
                            ((Activity) context).finish();
                            context.startActivity(intent);
                        }
                    }
                    break;
                case MyValues.NORMA_VEHICULAR:
                    SQLiteHelperVehiculos myDBHelper2;
                    myDBHelper2 = SQLiteHelperVehiculos.getInstance(context);
                    if (myDBHelper2.eliminarFavorito(numArticulo)) {
                        Toast.makeText(context, "Se eliminó " + nombreArticulo.toLowerCase() + " de Favoritos", Toast.LENGTH_LONG).show();
                        if(context instanceof ActivityFavoritos){
                            ((Activity) context).finish();
                            context.startActivity(intent);
                        }
                    }
                    break;
                case MyValues.NORMA_LICENCIAS:
                    SQLiteHelperLicencias myDBHelper3;
                    myDBHelper3 = SQLiteHelperLicencias.getInstance(context);
                    if (myDBHelper3.eliminarFavorito(numArticulo)) {
                        Toast.makeText(context, "Se eliminó " + nombreArticulo.toLowerCase() + " de Favoritos", Toast.LENGTH_LONG).show();
                        if(context instanceof ActivityFavoritos){
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

    public static void AgregarNota(Context context, int tipoNorma, float numArticulo, String nombreArticulo, int cantidadArticulos){
        Intent intent1 = new Intent(context,ActivityAddNote.class);
        intent1.putExtra(MyValues.NUMERO_ARTICULO,numArticulo);
        intent1.putExtra(MyValues.NOMBRE_ARTICULO,nombreArticulo);
        intent1.putExtra(MyValues.TIPO_NORMA, tipoNorma);
        intent1.putExtra(MyValues.CANTIDAD_ARTICULOS, cantidadArticulos);
        context.startActivity(intent1);
    }

    public static void EliminarNota(final Context context, final int tipoNorma, final float numArticulo, final String nombreArticulo, int cantidadArticulos){
        final Intent intent = new Intent(context,ActivityNotes.class);
        intent.putExtra(MyValues.TIPO_NORMA,tipoNorma);
        intent.putExtra(MyValues.CANTIDAD_ARTICULOS,cantidadArticulos);
        AlertDialog.Builder confirmar = new AlertDialog.Builder(context);
        confirmar.setTitle("Eliminar Anotación");
        confirmar.setMessage("¿Está seguro que desea eliminar la nota del " + nombreArticulo + "?");
        confirmar.setCancelable(false);
        confirmar.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface confirmar, int i) {
                switch (tipoNorma) {
                    case MyValues.NORMA_TRANSITO:
                        SQLiteHelperTransito myDBHelper1;
                        myDBHelper1 = SQLiteHelperTransito.getInstance(context);
                        if (myDBHelper1.EliminarNota(numArticulo)) {
                            Toast.makeText(context, "Se eliminó la nota del " + nombreArticulo.toLowerCase() + " satisfactoriamente", Toast.LENGTH_LONG).show();
                            ((Activity) context).finish();
                            context.startActivity(intent);
                        }
                        break;
                    case MyValues.NORMA_VEHICULAR:
                        SQLiteHelperVehiculos myDBHelper2;
                        myDBHelper2 = SQLiteHelperVehiculos.getInstance(context);
                        if (myDBHelper2.EliminarNota(numArticulo)) {
                            Toast.makeText(context, "Se eliminó la nota del " + nombreArticulo.toLowerCase() + " satisfactoriamente", Toast.LENGTH_LONG).show();
                            ((Activity) context).finish();
                            context.startActivity(intent);
                        }
                        break;
                    case MyValues.NORMA_LICENCIAS:
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

    public static void ShowNota(Context context, int tipoNorma, float numArticulo, String nombreArticulo){
        String nota = "";
        SQLiteHelperTransito myDBHelper1;
        SQLiteHelperVehiculos myDBHelper2;
        SQLiteHelperLicencias myDBHelper3;
        switch (tipoNorma){
            case MyValues.NORMA_TRANSITO:
                myDBHelper1 = SQLiteHelperTransito.getInstance(context);
                nota = myDBHelper1.getNota(numArticulo)[2];
                break;
            case MyValues.NORMA_VEHICULAR:
                myDBHelper2 = SQLiteHelperVehiculos.getInstance(context);
                nota = myDBHelper2.getNota(numArticulo)[2];
                break;
            case MyValues.NORMA_LICENCIAS:
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

    public static void CheckInternetAccessToWebview(Context context, String url){
        final ConnectivityManager network = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = network.getActiveNetworkInfo();
        if(activeNetwork != null && activeNetwork.isConnected()){
            Intent intent = new Intent(context, ActivityWebview.class);
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

    public static void QuerySubmit(Context context, MenuItem menuItem, int tipoNorma, int cantidadArticulos, String query){
        Intent intent = new Intent(context,ActivitySearchResultsTransito.class);
        intent.putExtra(MyValues.SEARCH_TEXT, query);
        intent.putExtra(MyValues.TIPO_NORMA, tipoNorma);
        intent.putExtra(MyValues.CANTIDAD_ARTICULOS, cantidadArticulos);
        context.startActivity(intent);
        MenuItemCompat.collapseActionView(menuItem);
    }

    public static void ShareApp(Context context){
        Social.share(context, context.getResources().getString(R.string.action_share), context.getResources().getString(R.string.share_description) + " " + Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName()));
    }

    public static void RateApp(Context mContext){
        try {
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + mContext.getPackageName())));
        } catch (android.content.ActivityNotFoundException anfe) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + mContext.getPackageName()));
            if (null != intent.resolveActivity(mContext.getPackageManager())) {
                mContext.startActivity(intent);
            }
        }
    }
}