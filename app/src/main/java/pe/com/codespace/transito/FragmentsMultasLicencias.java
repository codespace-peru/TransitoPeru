package pe.com.codespace.transito;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Creado por Carlos el 08/02/2016.
 */
public class FragmentsMultasLicencias extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private final static int PRIMERA = 1;
    private final static int SEGUNDA = 2;
    private final static int TERCERA = 3;

    public static FragmentsMultasLicencias newInstance(int sectionNumber) {
        FragmentsMultasLicencias fragment = new FragmentsMultasLicencias();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public FragmentsMultasLicencias() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_multas, container, false);
        //int anexo = getActivity().getIntent().getExtras().getInt(MyValues.ANEXO);
        int tipo = getArguments().getInt(ARG_SECTION_NUMBER);

        prepararData(tipo);
        final ExpandableListView myExpand;
        myExpand = (ExpandableListView) rootView.findViewById(R.id.myExpand);

        AdapterExpandListMultas myAdapter = new AdapterExpandListMultas(getActivity(), listDataHeader, listDataChild);
        myExpand.setAdapter(myAdapter);
        //myExpand.setGroupIndicator(null);
        myExpand.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int lastExpandedPosition = -1;

            @Override
            public void onGroupExpand(int pos) {
                if (lastExpandedPosition != -1 && pos != lastExpandedPosition)
                    myExpand.collapseGroup(lastExpandedPosition);
                lastExpandedPosition = pos;
            }
        });

        return rootView;

    }

    private void prepararData(int tipo){
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        SQLiteHelperLicencias myDBHelper = SQLiteHelperLicencias.getInstance(getActivity());
        String[] temp;

            switch (tipo){
                case PRIMERA:
                    listDataHeader = myDBHelper.getInfracciones("A");
                    List<String> Primera;
                    for(int i=0; i<listDataHeader.size(); i++){
                        temp = myDBHelper.getInfraccion(listDataHeader.get(i));
                        Primera = new ArrayList<>();
                        Primera.add("<b>Infracción:</b><br>" + temp[0] + "<br><br>" + "<b>Calificación:</b><br>" + temp[1] + "<br><br><b>Sanción:</b>" + temp[2] + "<br>");
                        listDataChild.put(listDataHeader.get(i), Primera);
                    }
                    break;
                case SEGUNDA:
                    listDataHeader = myDBHelper.getInfracciones("B");
                    List<String> Segunda;
                    for(int i=0; i<listDataHeader.size(); i++){
                        temp = myDBHelper.getInfraccion(listDataHeader.get(i));
                        Segunda = new ArrayList<>();
                        Segunda.add("<b>Infracción:</b><br>" + temp[0] + "<br><br>" + "<b>Calificación:</b><br>" + temp[1] + "<br><br><b>Sanción:</b>" + temp[2] + "<br>");
                        listDataChild.put(listDataHeader.get(i), Segunda);
                    }
                    break;
                case TERCERA:
                    listDataHeader = myDBHelper.getInfracciones("C");
                    List<String> Tercera;
                    for(int i=0; i<listDataHeader.size(); i++){
                        temp = myDBHelper.getInfraccion(listDataHeader.get(i));
                        Tercera = new ArrayList<>();
                        Tercera.add("<b>Infracción:</b><br>" + temp[0] + "<br><br>" + "<b>Calificación:</b><br>" + temp[1] + "<br><br><b>Sanción:</b>" + temp[2] + "<br>");
                        listDataChild.put(listDataHeader.get(i), Tercera);
                    }
                    break;
            }
        }
}
