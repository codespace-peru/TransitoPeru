package pe.com.codespace.transito;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Carlos on 7/01/14.
 */
public class SQLiteHelperLicencias extends SQLiteOpenHelper {
    private final Context myContext;
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "licencias.db";
    private static final String DATABASE_NAME_TEMP = "licenciasTemp.db";
    private static final String DATABASE_PATH = "databases/";
    private static final String DATABASE_TABLE1 = "articulos";
    private static File DATABASE_FILE = null;
    private static File DATABASE_FILE_TEMP = null;
    private boolean mInvalidDatabaseFile = false;
    private boolean mIsUpgraded  = false;
    private int mOpenConnections=0;
    private static SQLiteHelperLicencias mInstance;

    public synchronized static SQLiteHelperLicencias getInstance (Context context){
        if(mInstance == null){
            mInstance = new SQLiteHelperLicencias(context.getApplicationContext());
        }
        return mInstance;
    }

    private SQLiteHelperLicencias(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext = context;

        SQLiteDatabase db = null;
        try{
            db = getReadableDatabase();
            DATABASE_FILE = context.getDatabasePath(DATABASE_NAME);
            if(mInvalidDatabaseFile){
                copyDatabase(DATABASE_FILE);
            }
            if(mIsUpgraded){
                doUpgrade();
            }
        }
        catch(SQLiteException ex){
            throw ex;
        }
        finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        mInvalidDatabaseFile = true;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        mInvalidDatabaseFile = false;
        mIsUpgraded = true;
    }

    @Override
    public synchronized void onOpen(SQLiteDatabase db){
        super.onOpen(db);
        mOpenConnections++;
        if(!db.isReadOnly()){
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public synchronized void close(){
        mOpenConnections--;
        if(mOpenConnections == 0){
            super.close();
        }
    }

    public void copyDatabase(File dbFile)  {
        AssetManager assetManager = myContext.getResources().getAssets();
        InputStream myInput = null;
        OutputStream myOutput = null;
        try{
            myInput = assetManager.open(DATABASE_PATH + DATABASE_NAME);
            myOutput = new FileOutputStream(dbFile);
            byte[] buffer = new byte[1024];
            int read=0;
            while ((read = myInput.read(buffer)) != -1) {
                myOutput.write(buffer, 0, read);
            }
        }
        catch (IOException ex){
        }
        finally {
            if(myInput != null){
                try{ myInput.close(); }
                catch(IOException ex){ }
            }
            if(myOutput!=null){
                try{ myOutput.close(); }
                catch (IOException ex){ }
            }
            setDataBaseVersion();
            mInvalidDatabaseFile = false;
        }
    }

    private void setDataBaseVersion(){
        SQLiteDatabase db = null;
        try{
            db = SQLiteDatabase.openDatabase(DATABASE_FILE.getAbsolutePath(),null,SQLiteDatabase.OPEN_READWRITE);
            db.execSQL("PRAGMA user_version=" + DATABASE_VERSION);
        }
        catch (SQLiteException ex){
            throw ex;
        }
        finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
    }

    private void doUpgrade(){
        SQLiteDatabase db;
        SQLiteDatabase dbTemp;
        try{
            db = SQLiteDatabase.openDatabase(DATABASE_FILE.getAbsolutePath(),null,SQLiteDatabase.OPEN_READWRITE);
            DATABASE_FILE_TEMP = myContext.getDatabasePath(DATABASE_NAME_TEMP);
            copyDatabase(DATABASE_FILE_TEMP);
            db.delete(DATABASE_TABLE1,null,null);
            dbTemp = SQLiteDatabase.openDatabase(DATABASE_FILE_TEMP.getAbsolutePath(),null,SQLiteDatabase.OPEN_READWRITE);
            Cursor cursor = dbTemp.rawQuery("SELECT * FROM articulos", null);
            if (cursor.moveToFirst()) {
                while ( !cursor.isAfterLast() ) {
                    ContentValues values = new ContentValues();
                    values.put("numTitulo",cursor.getInt(0));
                    values.put("numArticulo",cursor.getInt(1));
                    values.put("nombreArticulo",cursor.getString(2));
                    values.put("descripArticulo",cursor.getString(3));
                    values.put("textArticulo",cursor.getString(4));
                    db.insert("articulos",null,values);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            myContext.deleteDatabase(DATABASE_NAME_TEMP);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

    }

    public String[][] getTitulos(){
        SQLiteDatabase db = null;
        try{
            db = getWritableDatabase();
            Cursor cursor = db.rawQuery("select numTitulo, nombreTitulo, descripTitulo from TITULOS", null);
            String[][] arrayOfString = (String[][])Array.newInstance(String.class, new int[] { cursor.getCount(),3 });
            int i = 0;
            if (cursor.moveToFirst()) {
                while ( !cursor.isAfterLast() ) {
                    arrayOfString[i][0] = cursor.getString(0);
                    arrayOfString[i][1] = cursor.getString(1);
                    arrayOfString[i][2] = cursor.getString(2);
                    i++;
                    cursor.moveToNext();
                }
            }
            cursor.close();
            return arrayOfString;
        }
        catch (SQLiteException ex){
            throw ex;
        }
        finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
    }


   public String[] getTitulo(int tit){
        SQLiteDatabase db = null;
        try{
            db = getReadableDatabase();
            Cursor cursor = db.rawQuery("select nombreTitulo, descripTitulo from titulos WHERE numTitulo = ?", new String[] {String.valueOf(tit)});
            String[] arrayOfString = (String[]) Array.newInstance(String.class, new int[]{2});
            int i = 0;
            if (cursor.moveToFirst()) {
                while ( !cursor.isAfterLast() ) {
                    arrayOfString[0] = cursor.getString(0);
                    arrayOfString[1] = cursor.getString(1);
                    i++;
                    cursor.moveToNext();
                }
            }
            cursor.close();
            return arrayOfString;
        }
        catch (SQLiteException ex){
           throw ex;
        }
        finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
    }


    public String[] getArticulo(float art){ // Es float para que soporte "11A" ("11.1"), "13A" (13.1), etc.
        SQLiteDatabase db = null;
        try{
            db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT numTitulo, nombreArticulo, descripArticulo, textArticulo FROM ARTICULOS WHERE numArticulo = ?", new String[] {String.valueOf(art)});
            String[] arrayOfString = (String[]) Array.newInstance(String.class, new int[]{4});
            int i = 0;
            if (cursor.moveToFirst()) {
                while ( !cursor.isAfterLast() ) {
                    arrayOfString[0] = cursor.getString(0);
                    arrayOfString[1] = cursor.getString(1);
                    arrayOfString[2] = cursor.getString(2);
                    arrayOfString[3] = cursor.getString(3);
                    i++;
                    cursor.moveToNext();
                }
            }
            cursor.close();
            return arrayOfString;
        }
        catch (SQLiteException ex){
            throw ex;
        }
        finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
    }

    public String[][] getListaArticulos(int titulo) {
        SQLiteDatabase db = null;
        try{
            db = getReadableDatabase();
            String[] array = new String[1];
            array[0] = String.valueOf(titulo);
            Cursor cursor = db.rawQuery("select numArticulo, nombreArticulo, descripArticulo, textArticulo from ARTICULOS WHERE numTitulo=? ORDER BY numArticulo", array);
            String[][] arrayOfString = (String[][]) Array.newInstance(String.class, new int[]{cursor.getCount(),4});

            int i = 0;
            if (cursor.moveToFirst()) {
                while ( !cursor.isAfterLast() ) {
                    arrayOfString[i][0] = cursor.getString(0);
                    arrayOfString[i][1] = cursor.getString(1);
                    arrayOfString[i][2] = cursor.getString(2);
                    arrayOfString[i][3] = cursor.getString(3);
                    i++;
                    cursor.moveToNext();
                }
            }
            cursor.close();
            return arrayOfString;
        }
        catch (SQLiteException ex){
            throw ex;
        }
        finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
    }

    public boolean es_favorito(float art) {
        SQLiteDatabase db = null;
        try{
            db = getReadableDatabase();
            Cursor cursor = db.rawQuery("select numArticulo from FAVORITOS where numArticulo = ? ", new String[]{String.valueOf(art)});
            if(cursor.moveToFirst()){
                if(cursor.getInt(0) == art)
                    return true;
                else
                    return false;
            }
            else
                return false;
        }catch (SQLiteException ex){
            throw ex;
        }
        finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
    }

    public String[][] getFavoritos(){
        SQLiteDatabase db = null;
        try{
            db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT numArticulo, nombreArticulo, descripArticulo, textArticulo FROM FAVORITOS ORDER BY numArticulo",null);
            String[][] arrayOfString = (String[][])Array.newInstance(String.class, new int[] {cursor.getCount(),4});
            int i = 0;
            if (cursor.moveToFirst()) {
                while ( !cursor.isAfterLast() ) {
                    arrayOfString[i][0] = cursor.getString(0);
                    arrayOfString[i][1] = cursor.getString(1);
                    arrayOfString[i][2] = cursor.getString(2);
                    arrayOfString[i][3] = cursor.getString(3);
                    i++;
                    cursor.moveToNext();
                }
            }
            cursor.close();
            return arrayOfString;
        }catch(SQLiteException ex){
            throw ex;
        }
        finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
    }

   public boolean setFavorito(float art){
        SQLiteDatabase db=null;
        try{
            boolean flag = false;
            db = getReadableDatabase();
            String[] myArt =  getArticulo(art);
            db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("nombreArticulo",myArt[1]);
            values.put("numArticulo",art);
            values.put("descripArticulo",myArt[2]);
            values.put("textArticulo",myArt[3]);
            long x = db.insert("FAVORITOS",null,values);
            if (x > 0){
                flag=true;
            }
            return flag;
        } catch (SQLiteException ex){
          throw ex;
        }
        finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
   }

   public boolean eliminarFavorito(float art){
        SQLiteDatabase db = null;
        try{
            boolean flag = false;
            db = getWritableDatabase();
            String[] whereArgs={String.valueOf(art)};
            long x = db.delete("FAVORITOS","numArticulo = ? ",whereArgs);
            if (x > 0){
                flag=true;
            }
            return flag;
        } catch (SQLiteException ex){
            throw ex;
        }
        finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
   }

    public String[][] getNotes(){
        SQLiteDatabase db = null;
        try{
            db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT numArticulo, nombreArticulo, descripArticulo, nota FROM NOTAS ORDER BY numArticulo", null);
            String[][] arrayOfString = (String[][])Array.newInstance(String.class, new int[] {cursor.getCount(),4});
            int i = 0;
            if (cursor.moveToFirst()) {
                while ( !cursor.isAfterLast() ) {
                    arrayOfString[i][0] = cursor.getString(0);
                    arrayOfString[i][1] = cursor.getString(1);
                    arrayOfString[i][2] = cursor.getString(2);
                    arrayOfString[i][3] = cursor.getString(3);
                    i++;
                    cursor.moveToNext();
                }
            }
            cursor.close();
            return arrayOfString;
        }catch(SQLiteException ex){
            throw ex;
        }
        finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
    }

    public boolean hay_nota(float art) {
        SQLiteDatabase db = null;
        try{
            db = getReadableDatabase();
            Cursor cursor = db.rawQuery("select numArticulo from NOTAS where numArticulo = ? ", new String[]{String.valueOf(art)});
            if(cursor.moveToFirst()){
                if(cursor.getInt(0)  == art )
                    return true;
                else
                    return false;
            }
            else
                return false;
        }catch (SQLiteException ex){
            throw ex;
        }
        finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
    }

    public String[] getNota(float art){
        SQLiteDatabase db = null;
        try{
            db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT nombreArticulo, descripArticulo, nota FROM NOTAS WHERE numArticulo = ?", new String[] {String.valueOf(art)});
            String[] arrayOfString = (String[]) Array.newInstance(String.class, new int[]{3});
            int i=0;
            if (cursor.moveToFirst()) {
                while ( !cursor.isAfterLast() ) {
                    arrayOfString[0] = cursor.getString(0);
                    arrayOfString[1] = cursor.getString(1);
                    arrayOfString[2] = cursor.getString(2);
                    i++;
                    cursor.moveToNext();
                }
            }
            cursor.close();
            return arrayOfString;
        }
        catch (SQLiteException ex){
            throw ex;
        }
        finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
    }

    public boolean AddNota(float art, String nota){
        SQLiteDatabase db = null;
        try{
            boolean flag = false;
            String[] articulo = getArticulo(art);
            db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("numArticulo",art);
            values.put("nombreArticulo",articulo[1]);
            values.put("descripArticulo",articulo[2]);
            values.put("nota",nota);
            long x = db.insert("notas",null,values);
            if (x > 0){
                flag = true;
            }
            return flag;
        } catch (SQLiteException ex){
            throw ex;
        }
        finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
    }

    public boolean UpdateNota(float art, String nota){
        SQLiteDatabase db = null;
        try{
            boolean flag = false;
            String[] whereArgs={String.valueOf(art)};
            db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("nota",nota);
            long x = db.update("notas",values,"numArticulo = ?",whereArgs);
            if (x > 0){
                flag = true;
            }
            return flag;
        } catch (SQLiteException ex){
            throw ex;
        }
        finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
    }

    public boolean EliminarNota(float art){
        SQLiteDatabase db = null;
        try{
            boolean flag = false;
            db = getWritableDatabase();
            String[] whereArgs={String.valueOf(art)};
            long x = db.delete("NOTAS","numArticulo = ? ",whereArgs);
            if (x > 0){
                flag=true;
            }
            return flag;
        } catch (SQLiteException ex){
            throw ex;
        }
        finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
    }

    public String[][] searchArticulo(String cadena) {
        SQLiteDatabase db = null;
        String[] cad = cadena.split(" ");
        String sqlLike = "SELECT numArticulo, nombreArticulo, descripArticulo, textArticulo FROM ARTICULOS where ";
        for(int i=0;i<cad.length;i++){
            if(i<cad.length-1){
                sqlLike = sqlLike + "textArticulo LIKE " + "\'%" + cad[i]  + "%\' AND ";
            }
            else{
                sqlLike = sqlLike + "textArticulo LIKE " + "\'%" + cad[i]  + "%\' COLLATE NOCASE";
            }
        }
        try{
            db = getReadableDatabase();
            Cursor cursor = db.rawQuery(sqlLike, null);
            int j = 0;
            String[][] arrayOfString = (String[][])Array.newInstance(String.class, new int[] { cursor.getCount(),4 });
            if(cursor.moveToFirst()){
                while(!cursor.isAfterLast()){
                    arrayOfString[j][0] = cursor.getString(0);
                    arrayOfString[j][1] = cursor.getString(1);
                    arrayOfString[j][2] = cursor.getString(2);
                    arrayOfString[j][3] = cursor.getString(3);
                    j++;
                    cursor.moveToNext();
                }
            }
            cursor.close();
            return arrayOfString;
        }
        catch (SQLiteException ex){
            throw ex;
        }
        finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
    }


    public ArrayList<String> getInfracciones(String tipo){
        SQLiteDatabase db = null;
        try{
            db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT codigo FROM infracciones WHERE tipo = ?", new String[]{String.valueOf(tipo)});
            //String[][] arrayOfString = (String[][])Array.newInstance(String.class, new int[] {cursor.getCount(),1});
            ArrayList<String> myList = new ArrayList<String>();
            int i = 0;
            if (cursor.moveToFirst()) {
                while ( !cursor.isAfterLast() ) {
                    //arrayOfString[i][0] = cursor.getString(0);
                    myList.add(cursor.getString(0));
                    //i++;
                    cursor.moveToNext();
                }
            }
            cursor.close();
            return myList;
        }catch(SQLiteException ex){
            throw ex;
        }
        finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
    }

    public String[] getInfraccion(String codigo){
        SQLiteDatabase db = null;
        try{
            db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT infraccion, calificacion, sancion FROM infracciones WHERE codigo = ?", new String[]{String.valueOf(codigo)});
            String[] arrayOfString = (String[])Array.newInstance(String.class, new int[] {3});
            if (cursor.moveToFirst()) {
                while ( !cursor.isAfterLast() ) {
                    arrayOfString[0] = cursor.getString(0);
                    arrayOfString[1] = cursor.getString(1);
                    arrayOfString[2] = cursor.getString(2);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            return arrayOfString;
        }catch(SQLiteException ex){
            throw ex;
        }
        finally {
            if(db != null && db.isOpen()){
                db.close();
            }
        }
    }
}
