package com.example.apphomemanager.listacompras;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apphomemanager.Communication.CommFirebase;
import com.example.apphomemanager.R;
import com.google.firebase.database.DatabaseReference;

import java.util.List;
import java.util.Random;

public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.ViewHolder>{

    private ContactsAdapterListener listener;

    private List<DBProduto> mProdutos;
    private Context context;

    private ConstantsApp constants = new ConstantsApp();


    public interface ContactsAdapterListener {
        void onContactSelected(DBProduto tag);
    }

    public ProdutoAdapter(List<DBProduto> produtos, Context context) {
        this.mProdutos = produtos;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ProdutoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View produtotView = LayoutInflater.from(context).inflate(R.layout.item, parent, false);

        ViewHolder viewHolder = new ViewHolder(produtotView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ProdutoAdapter.ViewHolder viewHolder, int position) {

        ViewHolder hold = (ViewHolder) viewHolder;
        DBProduto produtos = mProdutos.get(position);

        hideSoftKeyboard(hold.etQuantidade);

        boolean status = produtos.getStatus() == constants.getStatusOn() ? true : false;

        hold.tvProduto.setText(produtos.getNome());
        hold.tvProduto.setTextSize(16);
        hold.etQuantidade.setText(produtos.getQuantidade()+"");
        hold.ivSend.setImageResource(context.getApplicationContext().getResources().getIdentifier(status ? "basket" : "clbasket", "drawable", context.getPackageName()));

        switch (produtos.getUnidade()){
            case 0:
                hold.rbUn.setChecked(true);
                break;

            case 1:
                hold.rbMl.setChecked(true);
                break;

            case 2:
                hold.rbKg.setChecked(true);
                break;
        }

        hold.etQuantidade.setEnabled(status);
        hold.rgUnidade.setEnabled(status);
        hold.rbUn.setEnabled(status);
        hold.rbMl.setEnabled(status);
        hold.rbKg.setEnabled(status);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, View.OnFocusChangeListener {

        final TextView tvProduto;
        final EditText etQuantidade;
        final RadioButton rbUn;
        final RadioButton rbMl;
        final RadioButton rbKg;
        final ImageView ivSend;
        final RadioGroup rgUnidade;
        final ConstraintLayout clItem_2;

        public ViewHolder(View itemView) {
            super(itemView);

            tvProduto = (TextView) itemView.findViewById(R.id.tvProdutoItem);
            etQuantidade = (EditText) itemView.findViewById(R.id.etQuantidade);
            rbUn = (RadioButton) itemView.findViewById(R.id.rbUn);
            rbMl = (RadioButton) itemView.findViewById(R.id.rbMl);
            rbKg = (RadioButton) itemView.findViewById(R.id.rbKg);
            ivSend = (ImageView) itemView.findViewById(R.id.ivSendLTS);
            rgUnidade = (RadioGroup) itemView.findViewById(R.id.rgUnidade);
            clItem_2 = (ConstraintLayout) itemView.findViewById(R.id.clItem_2);

            tvProduto.setOnClickListener(this);
            clItem_2.setOnClickListener(this);
            ivSend.setOnClickListener(this);

            rgUnidade.setOnCheckedChangeListener(this);
            etQuantidade.setOnFocusChangeListener(this);
            ivSend.setOnFocusChangeListener(this);

            etQuantidade.setSelectAllOnFocus(true);
        }

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            View radioButton = rgUnidade.findViewById(rgUnidade.getCheckedRadioButtonId());

            int radioId = rgUnidade.indexOfChild(radioButton);
            RadioButton btn = (RadioButton) rgUnidade.getChildAt(radioId);

            hideSoftKeyboard(etQuantidade);

            if (btn.isChecked()){
                mProdutos.get(getAdapterPosition()).setUnidade(radioId);
            }
        }

        @Override
        public void onClick(View view) {
            hideSoftKeyboard(etQuantidade);

            int position = getAdapterPosition(); // gets item position
            if ((position != RecyclerView.NO_POSITION) && view.getTag() != null) { // Check if an item was deleted, but the user clicked it before the UI removed it
                switch (view.getTag().toString()){
                    case "ivSendLTS":
                        DatabaseReference dbOutStatus = ((BuyListActivity) context).getDbOutStatus();
                        String value = "";
                        String path = "";

                        DBProduto produtoTemp = new DBProduto();

                        if (((BuyListActivity) context).getPage() == 1){
                            path = constants.getPathMinhaLista();

                            produtoTemp = mProdutos.get(position);
                            produtoTemp.setQuantidade(Float.parseFloat(etQuantidade.getText().toString()));

                            ((BuyListActivity) context).setFlgUpdateTab1(false);

                            if (mProdutos.get(position).getStatus() == constants.getStatusOn()) {
                                produtoTemp.setStatus(constants.getStatusWait());

                                value = produtoTemp.getQuantidade()+"#"+produtoTemp.getUnidade()+"#"+ produtoTemp.getStatus();
                                new ProdutoDAO(context).updateProduto(produtoTemp, 2);
                                new CommFirebase().sendDataString(dbOutStatus,path + "/" + produtoTemp.getCategoria() + "/" + produtoTemp.getNome(), value);
                                Toast.makeText(view.getContext(), produtoTemp.getNome() + " " + context.getString(R.string.msgProductAdd), Toast.LENGTH_SHORT).show();
                            }else{
                                produtoTemp.setStatus(constants.getStatusOn());
                                new ProdutoDAO(context).updateProduto(produtoTemp, 2);
                                new CommFirebase().deleteItem(dbOutStatus,path + "/" + produtoTemp.getCategoria() + "/" + produtoTemp.getNome());
                                Toast.makeText(view.getContext(), produtoTemp.getNome() + " " + context.getString(R.string.msgProductRemove), Toast.LENGTH_SHORT).show();
                            }
                            mProdutos.set(position, produtoTemp);
                            updateListProduct(position);

                            int firebaseFlag =  new Random().nextInt(constants.rangeRandom);
                            new CommFirebase().sendDataInt(dbOutStatus, path+constants.getPathFlgMlst(), firebaseFlag);
                        }
                        break;
                }
                hideSoftKeyboard(etQuantidade);
            }
        }

        @Override
        public void onFocusChange(View view, boolean b) {
            if (!b) {
                mProdutos.get(getAdapterPosition()).setQuantidade(Float.parseFloat(etQuantidade.getText().toString()));
                hideSoftKeyboard(etQuantidade);
            }else{
                etQuantidade.selectAll();
            }
        }
    }

    public void updateListProduct(int position){
        notifyItemChanged(position);
    }

    public void hideSoftKeyboard(View view) {
        view.clearFocus();
        InputMethodManager imm =  (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void printLog(){
        for (DBProduto temp : mProdutos)
            Log.println(Log.VERBOSE, "Inform", "Produto: " + temp.getNome() + " - Qnt: " + temp.getQuantidade() + " - Un: " + temp.getUnidade());
    }

    @Override
    public int getItemCount() {
        return mProdutos.size();
    }
}