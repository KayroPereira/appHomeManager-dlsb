package com.example.apphomemanager.listacompras;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.apphomemanager.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FragSaveProduct extends Fragment implements View.OnClickListener, View.OnFocusChangeListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public FragSaveProduct() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private ImageView ivSendSQL;
    private ImageView ivBackFMSP;
    private View viewMain;

    private RadioButton rbUn;
    private RadioButton rbMl;
    private RadioButton rbKg;

    DBProduto sProduto = new DBProduto();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();

    final DatabaseReference dbOutStatus = reference;
    final int PAGER_4 = 4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        sProduto.setCategoria(-1);

        viewMain = inflater.inflate(R.layout.frag_save_product, container, false);

        ivSendSQL = (ImageView) viewMain.findViewById(R.id.ivSendSQL);
        ivBackFMSP = (ImageView) viewMain.findViewById(R.id.ivBackFMSP);

        rbUn = (RadioButton) viewMain.findViewById(R.id.rbUn);
        rbMl = (RadioButton) viewMain.findViewById(R.id.rbMl);
        rbKg = (RadioButton) viewMain.findViewById(R.id.rbKg);

        ConstraintLayout clFSP_1 = (ConstraintLayout) viewMain.findViewById(R.id.clFSP_1);
        clFSP_1.setBackgroundResource(R.drawable.gradient_1);

        TextView tvCategoria = (TextView) viewMain.findViewById(R.id.tvCategoria);

        tvCategoria.setText("Cadastro Produto");

        clFSP_1.setOnClickListener(this);
        viewMain.setOnClickListener(this);
        rbUn.setOnClickListener(this);
        rbMl.setOnClickListener(this);
        rbKg.setOnClickListener(this);

        ivBackFMSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.frmLClearOther, new DashBoardOpcoesLCP()).commit();
                hideSoftKeyboard(ivBackFMSP);
            }
        });

        ivSendSQL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //carregando os campos
                EditText etProduto = (EditText) viewMain.findViewById(R.id.etProduto);
                RadioGroup rgUnidade = (RadioGroup) viewMain.findViewById(R.id.rgUnidade);

                View radioButton = rgUnidade.findViewById(rgUnidade.getCheckedRadioButtonId());
                RadioButton rbCheck = (RadioButton) rgUnidade.getChildAt(0);

                hideSoftKeyboard(ivBackFMSP);

                if (!etProduto.getText().toString().isEmpty()) {
                    //pegando os valores
                    String produto = etProduto.getText().toString().toLowerCase();
                    int unidade = rbCheck.isChecked() ? 0 : ((RadioButton) rgUnidade.getChildAt(1)).isChecked() ? 1 : 2;

                    produto = produto.substring(0, 1).toUpperCase().concat(produto.substring(1));
                    sProduto.setNome(produto);
                    sProduto.setUnidade(unidade);

                    ((BuyListActivity) getActivity()).setsProduto(sProduto);

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frmLClearOther, new DashBoardCategoria(), "frag" + PAGER_4).commit();
                }else{
                    Snackbar.make(view, getString(R.string.msgProductEmpt), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });
        return viewMain;
    }

    public void hideSoftKeyboard(View view) {
        view.clearFocus();
        InputMethodManager imm =  (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onClick(View view) {



        hideSoftKeyboard(ivBackFMSP);
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        hideSoftKeyboard(ivBackFMSP);
    }
}