package com.example.apphomemanager.listacompras;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.apphomemanager.R;


public class DashBoardOpcoesLCP extends Fragment  implements View.OnClickListener{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ConstantsApp constants = new ConstantsApp();

    public DashBoardOpcoesLCP() {
    }

    public static DashBoardOpcoesLCP newInstance(String param1, String param2) {
        DashBoardOpcoesLCP fragment = new DashBoardOpcoesLCP();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /*
    void handler1(View v){
    }
     */


    //@RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dash_board_opcoes_lcp, container, false);

        //view.setOnClickListener(this::handler1);

        ConstraintLayout fml = (ConstraintLayout) ((FrameLayout) view).getChildAt(0);
        fml = (ConstraintLayout) fml.getChildAt(0);

        int categoriaItem = 0;
        for (int i = 0; i < fml.getChildCount(); i++) {
            View comp = fml.getChildAt(i);

            if (comp instanceof ConstraintLayout) {
                int cont = 0;
                for (int item = 0; item < ((ConstraintLayout) comp).getChildCount(); item++) {
                    View vTemp = ((ConstraintLayout) comp).getChildAt(item);

                    if (vTemp instanceof TextView) {
                        //((TextView) vTemp).setTextColor(getContext().getColor(R.color.colorTab2));
                        ((TextView) vTemp).setTextColor(getResources().getColor(R.color.colorTab2));
                        vTemp.setTag(categoriaItem);
                        cont++;
                    }

                    if (vTemp instanceof ImageView) {
                        vTemp.setTag(categoriaItem);
                        cont++;
                        vTemp.setOnClickListener(this);
                    }

                    if (cont == 2)
                        categoriaItem++;
                }
            }
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        if ((view instanceof ImageView) || (view instanceof TextView)) {

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            switch ((int) view.getTag()){
                case 0:
                    fragmentTransaction.replace(R.id.frmLClearOther, new FragSaveProduct(), "frag" + constants.getPAGER_2()).commit(); //todos do primeiro nível devem ser camados com PAGER_2
                    break;

                case 1:
                    ((BuyListActivity) getActivity()).shareWhatsApp(getString(R.string.msgCompartilharLst), ((BuyListActivity) getActivity()).formatSharedMensage());
                    break;
            }
        }
    }
}