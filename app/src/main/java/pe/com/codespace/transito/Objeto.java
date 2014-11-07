package pe.com.codespace.transito;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carlos on 08/04/2014.
 */
public class Objeto {
    public int Nivel;//1=Primer nivel ,2=Segundo nivel, 3=Tercer nivel
    public int Num;
    public String Title;
    public String Description;
    public List<Objeto> children;
    public Objeto(){ children = new ArrayList<Objeto>(); }
}
