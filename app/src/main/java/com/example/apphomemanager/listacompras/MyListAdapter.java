package com.example.apphomemanager.listacompras;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.apphomemanager.Communication.CommFirebase;
import com.example.apphomemanager.R;
import com.google.firebase.database.DatabaseReference;

import java.util.List;
import java.util.Random;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {

private List<DBProduto> mProdutos;
private Context context;

private ConstantsApp constants = new ConstantsApp();

public MyListAdapter(List<DBProduto> produtos, Context context) {
        this.mProdutos = produtos;
        this.context = context;
    }

    @Override
    public MyListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View produtotView = LayoutInflater.from(context).inflate(R.layout.item_my_list, parent, false);

        ViewHolder viewHolder = new ViewHolder(produtotView);
        return viewHolder;
    }

    //@SuppressLint({"ResourceAsColor", "Range"})
    @Override
    public void onBindViewHolder(MyListAdapter.ViewHolder viewHolder, int position) {

        ViewHolder hold = (ViewHolder) viewHolder;
        DBProduto produtos = mProdutos.get(position);

        viewHolder.setIsRecyclable(false);

        switch(produtos.getId()){
            case -1:
                hold.tvProduto.setText(produtos.getNome());
                //hold.tvProduto.setTextColor(context.getColor(R.color.colorFontItemCategoria));
                hold.tvProduto.setTextColor(context.getResources().getColor(R.color.colorFontItemCategoria));
                hold.tvProduto.setTextSize(16);
                hold.ivCategoriaMyList.setImageResource(context.getApplicationContext().getResources().getIdentifier(produtos.getCategoria() != -1 ? "ctg"+produtos.getCategoria() : "basket", "drawable", context.getPackageName()));
                hold.itemView.setBackgroundResource(R.drawable.gradient);
                break;

            case -2:
                hold.tvProduto.setText(produtos.getNome());
                hold.tvProduto.setTextColor(context.getResources().getColor(R.color.colorPurchasedProducts));
                if (produtos.getCategoria() != -2)
                    hold.tvProduto.setPaintFlags(hold.tvProduto.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                hold.ivCategoriaMyList.setVisibility(View.GONE);
                break;

            default:
                hold.tvProduto.setTextColor(context.getResources().getColor(R.color.colorFontItem));
                hold.tvProduto.setTextSize(17);
                hold.tvProduto.setText(produtos.getNome()+ " - " + produtos.getQuantidade()  + " " + constants.getNameUnidade()[produtos.getUnidade()]);
                hold.ivCategoriaMyList.setVisibility(View.GONE);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final TextView tvProduto;
        final ImageView ivCategoriaMyList;

        public ViewHolder(View itemView) {
            super(itemView);

            tvProduto = (TextView) itemView.findViewById(R.id.tvProdutoMyList);
            ivCategoriaMyList = (ImageView) itemView.findViewById(R.id.ivCategoriaMyList);
            tvProduto.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                DatabaseReference dbOutStatus = ((BuyListActivity) context).getDbOutStatus();

                String path = constants.getPathMinhaLista();
                String value = mProdutos.get(position).getQuantidade()+"#"+mProdutos.get(position).getUnidade()+"#";

                if (mProdutos.get(position).getStatus() == constants.getStatusWait()) {
                    new CommFirebase().sendDataString(dbOutStatus,path + "/" + mProdutos.get(position).getCategoria() + "/" + mProdutos.get(position).getNome(), value+constants.getStatusOff());
                }else if (mProdutos.get(position).getStatus() == constants.getStatusOff()){
                    new CommFirebase().sendDataString(dbOutStatus,path + "/" + mProdutos.get(position).getCategoria() + "/" + mProdutos.get(position).getNome(), value+constants.getStatusWait());
                }
                new CommFirebase().sendDataInt(dbOutStatus, path+constants.getPathFlgMlst(), new Random().nextInt(constants.rangeRandom));
            }
        }
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
