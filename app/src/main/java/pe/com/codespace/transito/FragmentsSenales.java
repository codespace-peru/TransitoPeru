package pe.com.codespace.transito;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Creado por Carlos el 08/02/2016.
 */
public class FragmentsSenales extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private int[] imagesReglamentarias = {R.drawable.reglamen1, R.drawable.reglamen2, R.drawable.reglamen3, R.drawable.reglamen4, R.drawable.reglamen5, R.drawable.reglamen6, R. drawable.reglamen7};
    private int[] imagesPreventivas = {R.drawable.preven1, R.drawable.preven2, R.drawable.preven3, R.drawable.preven4, R.drawable.preven5, R.drawable.preven6, R. drawable.preven7, R.drawable.preven8};
    private int[] imagesInformativas = {R.drawable.inform1, R.drawable.inform2, R.drawable.inform3, R.drawable.inform4, R.drawable.inform5, R.drawable.inform6};
    private int index = 0;

    public static FragmentsSenales newInstance(int sectionNumber) {
        FragmentsSenales fragment = new FragmentsSenales();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public FragmentsSenales() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_senales, container, false);
        final ImageView imageView = (ImageView) rootView.findViewById(R.id.image_transitsign);
        PhotoViewAttacher mAttacher;
        final TextView textView = (TextView) rootView.findViewById(R.id.tvNroPag);
        final int seccion = getArguments().getInt(ARG_SECTION_NUMBER);
        switch (seccion){
            case 1:
                imageView.setImageResource(imagesReglamentarias[index]);
                mAttacher = new PhotoViewAttacher(imageView);
                mAttacher.update();
                textView.setText("Pag. " + String.valueOf(index+1));
                break;
            case 2:
                imageView.setImageResource(imagesPreventivas[index]);
                mAttacher = new PhotoViewAttacher(imageView);
                mAttacher.update();
                textView.setText("Pag. " + String.valueOf(index+1));
                break;
            case 3:
                imageView.setImageResource(imagesInformativas[index]);
                mAttacher = new PhotoViewAttacher(imageView);
                mAttacher.update();
                textView.setText("Pag. " + String.valueOf(index+1));
                break;
        }

        Button btnNext, btnPrev;
        btnNext = (Button) rootView.findViewById(R.id.btnNextImage);
        btnPrev = (Button) rootView.findViewById(R.id.btnPrevImage);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index++;
                switch (seccion){
                    case 1:
                        if(index<=6){
                            imageView.setImageResource(imagesReglamentarias[index]);
                            textView.setText("Pag. " + String.valueOf(index+1));
                        }
                        else{
                            imageView.setImageResource(imagesReglamentarias[0]);
                            index = 0;
                            textView.setText("Pag. " + String.valueOf(index+1));
                        }

                        break;
                    case 2:
                        if(index<=7){
                            imageView.setImageResource(imagesPreventivas[index]);
                            textView.setText("Pag. " + String.valueOf(index+1));
                        }
                        else {
                            imageView.setImageResource(imagesPreventivas[0]);
                            index = 0;
                            textView.setText("Pag. " + String.valueOf(index+1));
                        }
                        break;
                    case 3:
                        if(index<=5){
                            imageView.setImageResource(imagesInformativas[index]);
                            textView.setText("Pag. " + String.valueOf(index+1));
                        }
                        else {
                            imageView.setImageResource(imagesInformativas[0]);
                            index = 0;
                            textView.setText("Pag. " + String.valueOf(index+1));
                        }
                        break;
                }
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index--;
                switch (seccion){
                    case 1:
                        if(index<0){
                            imageView.setImageResource(imagesReglamentarias[6]);
                            index=6;
                            textView.setText("Pag. " + String.valueOf(index+1));
                        }
                        else{
                            imageView.setImageResource(imagesReglamentarias[index]);
                            textView.setText("Pag. " + String.valueOf(index+1));
                        }

                        break;
                    case 2:
                        if(index<0){
                            imageView.setImageResource(imagesPreventivas[7]);
                            index=7;
                            textView.setText("Pag. " + String.valueOf(index+1));
                        }
                        else {
                            imageView.setImageResource(imagesPreventivas[index]);
                            textView.setText("Pag. " + String.valueOf(index+1));
                        }
                        break;
                    case 3:
                        if(index<0){
                            imageView.setImageResource(imagesInformativas[5]);
                            index=5;
                            textView.setText("Pag. " + String.valueOf(index+1));
                        }
                        else{
                            imageView.setImageResource(imagesInformativas[index]);
                            textView.setText("Pag. " + String.valueOf(index+1));
                        }
                        break;
                }
            }
        });
        return rootView;
    }

}
