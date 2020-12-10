package com.example.apphomemanager.listacompras;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.apphomemanager.Communication.CommFirebase;
import com.example.apphomemanager.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;

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

import java.util.Random;

public class DashBoardCategoria extends Fragment implements View.OnClickListener{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ConstantsApp constants = new ConstantsApp();

    public DashBoardCategoria() {
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
    void handler(View v){
        Log.println(Log.VERBOSE, "Teste", "Ok");
    }
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dash_board_categoria, container, false);

        //view.setOnClickListener(this::handler);

        //ConstantsApp constants = new ConstantsApp();
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
                        ((TextView) vTemp).setText(constants.getNameCategoryItem(categoriaItem));
                        vTemp.setTag(categoriaItem);
                        //((TextView) vTemp).setTextColor(getContext().getColor(R.color.colorTab1));
                        ((TextView) vTemp).setTextColor(getResources().getColor(R.color.colorTab1));
                        cont++;
                    }

                    if (vTemp instanceof ImageView) {
                        ((ImageView) vTemp).setImageResource(this.getContext().getApplicationContext().getResources().getIdentifier("ctg"+categoriaItem, "drawable", getActivity().getPackageName()));
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
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        int page = ((BuyListActivity) getContext()).getPage();
        DatabaseReference dbOutStatus = ((BuyListActivity) getContext()).getDbOutStatus();

        if ((view instanceof ImageView) || (view instanceof TextView)) {
            switch (page){
                case 1:
                    fragmentTransaction.replace(R.id.frmLClear, new FragCategoria((int) view.getTag()), "frag" + page).commit();
                    break;

                case 2:
                    DBProduto sProduto = ((BuyListActivity) getContext()).getsProduto();
                    new CommFirebase().sendDataInt(dbOutStatus, constants.getPathDespensa()+"/"+view.getTag()+"/"+sProduto.getNome(), sProduto.getUnidade());
                    new CommFirebase().sendDataInt(dbOutStatus, constants.getPathDespensa()+constants.getPathFlgDsp(), new Random().nextInt(constants.getRangeRandom()));

                    sProduto.setCategoria(-1);
                    Snackbar.make(view, getString(R.string.saveOk), Snackbar.LENGTH_LONG).setAction("Action", null).show();

                    fragmentTransaction.replace(R.id.frmLClearOther, new FragSaveProduct(), "frag" + constants.getPAGER_2()).commit();
                    break;
            }
        }
    }
}