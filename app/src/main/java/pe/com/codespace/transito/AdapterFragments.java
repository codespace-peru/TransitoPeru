package pe.com.codespace.transito;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Locale;

/**
 * Creado por Carlos on 08/02/2016.
 */
public class AdapterFragments extends FragmentPagerAdapter {

    private static int NUM_ITEMS_SENALES = 3;
    private static int NUM_ITEMS_MULTAS_TRANSITO = 3;
    private static int NUM_ITEMS_MULTAS_LICENCIAS = 3;
    private int tipo_fragment;

    private Context context;

    public AdapterFragments(FragmentManager fragmentManager, Context ctx, int tipo_frag) {
        super(fragmentManager);
        context = ctx;
        tipo_fragment = tipo_frag;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        int temp = 0;
        switch (tipo_fragment){
            case MyValues.FRAGMENTS_MULTAS_TRANSITO:
                temp = NUM_ITEMS_MULTAS_TRANSITO;
                break;
            case MyValues.FRAGMENTS_MULTAS_LICENCIAS:
                temp = NUM_ITEMS_MULTAS_LICENCIAS;
                break;
            case MyValues.FRAGMENTS_SENALES:
                temp = NUM_ITEMS_SENALES;
                break;
        }
        return temp;
    }

    @Override
    public Fragment getItem(int position) {
        switch (tipo_fragment){
            case MyValues.FRAGMENTS_SENALES:
                switch (position) {
                    case 0: // Fragment # 0 - This will show FirstFragment
                        return FragmentsSenales.newInstance(1);
                    case 1: // Fragment # 0 - This will show FirstFragment different title
                        return FragmentsSenales.newInstance(2);
                    case 2: // Fragment # 1 - This will show SecondFragment
                        return FragmentsSenales.newInstance(3);
                }
                break;
            case MyValues.FRAGMENTS_MULTAS_TRANSITO:
                switch (position) {
                    case 0: // Fragment # 0 - This will show FirstFragment
                        return FragmentsMultasTransito.newInstance(1);
                    case 1: // Fragment # 0 - This will show FirstFragment different title
                        return FragmentsMultasTransito.newInstance(2);
                    case 2: // Fragment # 1 - This will show SecondFragment
                        return FragmentsMultasTransito.newInstance(3);
                }
                break;
            case MyValues.FRAGMENTS_MULTAS_LICENCIAS:
                switch (position) {
                    case 0: // Fragment # 0 - This will show FirstFragment
                        return FragmentsMultasLicencias.newInstance(1);
                    case 1: // Fragment # 0 - This will show FirstFragment different title
                        return FragmentsMultasLicencias.newInstance(2);
                    case 2: // Fragment # 1 - This will show SecondFragment
                        return FragmentsMultasLicencias.newInstance(3);
                }
                break;
        }
        return null;
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (tipo_fragment){
            case MyValues.FRAGMENTS_SENALES:
                switch (position) {
                    case 0:
                        return context.getString(R.string.title_section_senales1).toUpperCase(l);
                    case 1:
                        return context.getString(R.string.title_section_senales2).toUpperCase(l);
                    case 2:
                        return context.getString(R.string.title_section_senales3).toUpperCase(l);
                }
                break;
            case MyValues.FRAGMENTS_MULTAS_TRANSITO:
                switch (position) {
                    case 0:
                        return context.getString(R.string.title_section_infraccionestransito1).toUpperCase(l);
                    case 1:
                        return context.getString(R.string.title_section_infraccionestransito2).toUpperCase(l);
                    case 2:
                        return context.getString(R.string.title_section_infraccionestransito3).toUpperCase(l);
                }
                break;
            case MyValues.FRAGMENTS_MULTAS_LICENCIAS:
                switch (position) {
                    case 0:
                        return context.getString(R.string.title_section_infraccioneslicencias1).toUpperCase(l);
                    case 1:
                        return context.getString(R.string.title_section_infraccioneslicencias2).toUpperCase(l);
                    case 2:
                        return context.getString(R.string.title_section_infraccioneslicencias3).toUpperCase(l);
                }
                break;
        }

        return null;
    }

}
