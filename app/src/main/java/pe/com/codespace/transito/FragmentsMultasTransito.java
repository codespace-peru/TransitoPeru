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
public class FragmentsMultasTransito extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private final static int MUY_GRAVES = 1;
    private final static int GRAVES = 2;
    private final static int LEVES = 3;

    public static FragmentsMultasTransito newInstance(int sectionNumber) {
        FragmentsMultasTransito fragment = new FragmentsMultasTransito();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public FragmentsMultasTransito() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_multas, container, false);
        int anexo = getActivity().getIntent().getExtras().getInt(MyValues.ANEXO);
        int tipo = getArguments().getInt(ARG_SECTION_NUMBER);

        prepararData(anexo, tipo);
        final ExpandableListView myExpand;
        myExpand = (ExpandableListView) rootView.findViewById(R.id.myExpand);

        AdapterExpandListMultas myAdapter = new AdapterExpandListMultas(getActivity(), listDataHeader, listDataChild);
        myExpand.setAdapter(myAdapter);
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

    private void prepararData(int anexo, int tipo){
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        SQLiteHelperTransito myDBHelper = SQLiteHelperTransito.getInstance(getActivity());
        String[] temp;
        switch (anexo){
            case 9:
                switch (tipo){
                    case MUY_GRAVES:
                        listDataHeader = myDBHelper.getInfraccionesConductor("M");
                        List<String> MuyGraves;
                        for(int i=0; i<listDataHeader.size(); i++){
                            temp = myDBHelper.getInfraccionConductor(listDataHeader.get(i));
                            MuyGraves = new ArrayList<>();
                            MuyGraves.add("<b>Infracción:</b><br>" + temp[0] + "<br><br>" + "<b>Sanción:</b><br>" + temp[1] + "<br><br><b>Puntos que acumula:</b>" + temp[2] + "<br><br>" +  "<b>Medidas Preventivas:</b>\n" + temp[3] + "<br><br>" + "<b>Responsabilidad solidaria:</b>" + temp[4] + "<br>");
                            listDataChild.put(listDataHeader.get(i), MuyGraves);
                        }
                        break;
                    case GRAVES:
                        listDataHeader = myDBHelper.getInfraccionesConductor("G");
                        List<String> Graves;
                        for(int i=0; i<listDataHeader.size(); i++){
                            temp = myDBHelper.getInfraccionConductor(listDataHeader.get(i));
                            Graves = new ArrayList<>();
                            Graves.add("<b>Infracción:</b><br>" + temp[0] + "<br><br>" + "<b>Sanción:</b><br>" + temp[1] + "<br><br><b>Puntos que acumula:</b>" + temp[2] + "<br><br>" +  "<b>Medidas Preventivas:</b>\n" + temp[3] + "<br><br>" + "<b>Responsabilidad solidaria:</b>" + temp[4] + "<br>");
                            listDataChild.put(listDataHeader.get(i), Graves);
                        }
                        break;
                    case LEVES:
                        listDataHeader = myDBHelper.getInfraccionesConductor("L");
                        List<String> Leves;
                        for(int i=0; i<listDataHeader.size(); i++){
                            temp = myDBHelper.getInfraccionConductor(listDataHeader.get(i));
                            Leves = new ArrayList<>();
                            Leves.add("<b>Infracción:</b><br>" + temp[0] + "<br><br>" + "<b>Sanción:</b><br>" + temp[1] + "<br><br><b>Puntos que acumula:</b> " + temp[2] + "<br><br>" +  "<b>Medidas Preventivas:</b>\n" + temp[3] + "<br><br>" + "<b>Responsabilidad solidaria:</b> " + temp[4] + "<br>");
                            listDataChild.put(listDataHeader.get(i), Leves);
                        }
                        break;
                }
                break;
            case 10:
                switch (tipo){
                    case MUY_GRAVES:
                        listDataHeader = myDBHelper.getInfraccionesPeaton("A");
                        List<String> MuyGraves;
                        for(int i=0; i<listDataHeader.size(); i++){
                            temp = myDBHelper.getInfraccionPeaton(listDataHeader.get(i));
                            MuyGraves = new ArrayList<>();
                            MuyGraves.add("<b>Infracción:</b><br>" + temp[0] + "<br><br><b>Sanción:</b><br>" + temp[1] + "<br><br><b>Medida Preventiva:</b><br>" + temp[2] + "<br>");
                            listDataChild.put(listDataHeader.get(i), MuyGraves);
                        }
                        break;
                    case GRAVES:
                        listDataHeader = myDBHelper.getInfraccionesPeaton("B");
                        List<String> Graves;
                        for(int i=0; i<listDataHeader.size(); i++){
                            temp = myDBHelper.getInfraccionPeaton(listDataHeader.get(i));
                            Graves = new ArrayList<>();
                            Graves.add("<b>Infracción:</b><br>" + temp[0] + "<br><br><b>Sanción:</b><br>" + temp[1] + "<br><br><b>Medida Preventiva:</b><br>" + temp[2] + "<br>");
                            listDataChild.put(listDataHeader.get(i), Graves);
                        }
                        break;
                    case LEVES:
                        listDataHeader = myDBHelper.getInfraccionesPeaton("C");
                        List<String> Leves;
                        for(int i=0; i<listDataHeader.size(); i++){
                            temp = myDBHelper.getInfraccionPeaton(listDataHeader.get(i));
                            Leves = new ArrayList<>();
                            Leves.add("<b>Infracción:</b><br>" + temp[0] + "<br><br><b>Sanción:</b><br>" + temp[1] + "<br><br><b>Medida Preventiva:</b><br>" + temp[2] + "<br>");
                            listDataChild.put(listDataHeader.get(i), Leves);
                        }
                        break;
                }
                break;
        }

    }

}
