package com.example.apphomemanager.listacompras;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.apphomemanager.Communication.CommFirebase;
import com.example.apphomemanager.R;
//import com.example.applistadecompras.Communication.CommFirebase;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BuyListActivity extends AppCompatActivity {

    private ArrayList<DBProduto> produtosLista;

    private DBProduto sProduto = new DBProduto();

    private ConstantsApp constants = new ConstantsApp();

    private TabLayout tabLayout;

    private int[] tabIcons = {
            R.drawable.mylistoff,
            R.drawable.myliston,
            R.drawable.despensaoff,
            R.drawable.despensaon,
            R.drawable.opcaooff,
            R.drawable.opcaoon
    };

    private ViewPager viewPager;
    private AbasAdapter adapter;
    private Callbacks callbacks;
    private Callbacks callbacksProductCategoria;

    private ImageView ivTab0;
    private ImageView ivTab1;
    private ImageView ivTab2;

    private int page = 0;
    private boolean flgUpdateTab0 = false;
    private boolean flgUpdateTab1 = true;
    //private boolean refreshMylist = false;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();

    final DatabaseReference dbOutStatus = reference;

    final int PAGER_0 = 0;
    final int PAGER_1 = 1;
    final int PAGER_2 = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_buy_list);

        ivTab0 = (ImageView) findViewById(R.id.ivTab0);
        ivTab1 = (ImageView) findViewById(R.id.ivTab1);
        ivTab2 = (ImageView) findViewById(R.id.ivTab2);

        sProduto.setCategoria(-1);

        adapter = new AbasAdapter(getSupportFragmentManager());
        adapter.adicionar(new FragMyList(), "Minha Lista");
        adapter.adicionar(new FragClear(), "Despensa");
        adapter.adicionar(new FragClearOther() , "Opções");

        viewPager = (ViewPager) findViewById(R.id.vp1);
        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons(0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                page = position;
                setupTabIcons(page);
                hideSoftKeyboard(ivTab0);

                if (page == PAGER_0 && flgUpdateTab0) {
                    callbacks.updateMyList();
                    flgUpdateTab0 = false;
                }

                if (page == PAGER_1) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frmLClear, new DashBoardCategoria()).commit();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        dbOutStatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int upFlag = new ProdutoDAO(getApplicationContext()).getFlagProduto(constants.getFlgDsp());

                if (upFlag == -1) {
                    new ProdutoDAO(getApplicationContext()).updateFlagProduto(-1, 0);
                    new ProdutoDAO(getApplicationContext()).updateFlagProduto(-1, 0);
                }

                int firebaseFlag = Integer.parseInt(new CommFirebase().getItem(dataSnapshot,constants.getPathDespensa()+constants.getPathFlgDsp()));

                if (upFlag != firebaseFlag) {
                    produtosLista = new CommFirebase().getListaCompras(dataSnapshot, constants.getPathDespensa(), constants.getFlgDsp());
                    new ProdutoDAO(getApplicationContext()).deleteProduto(-1);
                    new ProdutoDAO(getApplicationContext()).saveList(produtosLista);
                    new ProdutoDAO(getApplicationContext()).updateFlagProduto(constants.getFlgDsp(), firebaseFlag);
                    new ProdutoDAO(getApplicationContext()).updateFlagProduto(constants.getFlgMlst(), firebaseFlag);
                }

                upFlag = new ProdutoDAO(getApplicationContext()).getFlagProduto(constants.getFlgMlst());
                firebaseFlag = Integer.parseInt(new CommFirebase().getItem(dataSnapshot,constants.getPathMinhaLista()+constants.getPathFlgMlst()));

                if (upFlag != firebaseFlag) {
                    ArrayList<DBProduto> produtosMyLista = new CommFirebase().getListaCompras(dataSnapshot, constants.getPathMinhaLista(), constants.getFlgMlst());
                    new ProdutoDAO(getApplicationContext()).updateProduto(null, 3);         //deixa todos os produtos disponíveis na despensa / fora da lista de compras

                    for (DBProduto temp : produtosMyLista) {
                        new ProdutoDAO(getApplicationContext()).updateProduto(temp, 2);              //atualiza o status, quantidade e unidade dos produtos da lista
                    }

                    new ProdutoDAO(getApplicationContext()).updateFlagProduto(constants.getFlgMlst(), firebaseFlag);

                    if (page == PAGER_0){
                        if (callbacks != null)
                            callbacks.updateMyList();
                        /*else
                            refreshMylist = true;*/
                    } else
                        flgUpdateTab0 = true;

                    if (page == PAGER_1 && flgUpdateTab1)
                        callbacksProductCategoria.updateProducts();

                    flgUpdateTab1 = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        try {
            if (fragment.toString().indexOf("FragMyList") == 0) {
                callbacks = (Callbacks) fragment;

                /*
                if (refreshMylist){
                    callbacks.updateMyList();
                }
                 */
            }

            if (fragment.toString().indexOf("FragCategoria") == 0)
                callbacksProductCategoria = (Callbacks) fragment;
        } catch (ClassCastException e) {
            throw new ClassCastException(fragment.toString() + " must implement OnArticleSelectedListener");
        }
    }

    @Override
    public void onBackPressed() {

        FragmentManager fragmentManager = getSupportFragmentManager();

        for (Fragment temp : fragmentManager.getFragments()){

            if (temp.getTag() != null){
                String frag = temp.getTag().replace("frag", "");

                try{
                    int fragNumber = Integer.parseInt(frag);
                    if ((fragNumber == page) || (page == PAGER_2 && fragNumber % 2 == 0)) {
                        callFragmentDefault(fragNumber);
                        Log.println(Log.VERBOSE, "Teste: ", "Page: " + fragNumber);
                        return;
                    }
                }catch (Exception e){
                    Log.println(Log.VERBOSE, "Teste: ", "Page: " + "invalida");
                }
            }
        }

        super.onBackPressed();
    }

    //@RequiresApi(api = Build.VERSION_CODES.M)
    //@SuppressLint("NewApi")
    private void setupTabIcons(int pageMode) {
        ivTab0.setImageResource(tabIcons[0]);
        ivTab1.setImageResource(tabIcons[2]);
        ivTab2.setImageResource(tabIcons[4]);

        switch (pageMode) {
            case 0:
                //tabLayout.setTabTextColors(Color.parseColor("#727272"), getBaseContext().getColor(R.color.colorTab0));
                tabLayout.setTabTextColors(Color.parseColor("#727272"), getResources().getColor(R.color.colorTab0));
                ivTab0.setImageResource(tabIcons[1]);
                break;

            case 1:
                tabLayout.setTabTextColors(Color.parseColor("#727272"), getResources().getColor(R.color.colorTab1));
                ivTab1.setImageResource(tabIcons[3]);
                break;

            case 2:
                tabLayout.setTabTextColors(Color.parseColor("#727272"), getResources().getColor(R.color.colorTab2));
                ivTab2.setImageResource(tabIcons[5]);
                break;
        }
    }

    /*
    //Click Dashboard OpcoesLCP - viewpager - 2
    public void ivClickedOpLCP(View item){
        if ((item instanceof ImageView) || (item instanceof TextView)) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            switch ((int) item.getTag()){
                case 0:
                    fragmentTransaction.replace(R.id.frmLClearOther, new FragSaveProduct(), "frag" + PAGER_2).commit(); //todos do primeiro nível devem ser camados com PAGER_2
                    break;

                case 1:
                    shareWhatsApp(getString(R.string.msgCompartilharLst), formatSharedMensage());
                    break;
            }
        }
    }
     */

    public String formatSharedMensage(){
        String productList = "*" + getString(R.string.msgLstCompras) + "*\n\n";
        String purchasedProductList = "";

        List<DBProduto> produtos = new ProdutoDAO(this).getListProduct(3, -1);

        if (produtos.size() == 0){
            productList += getString(R.string.listClear);
            return productList;
        }

        int     category = -1,
                position = 0;

        for (DBProduto temp : produtos){
            if (temp.getCategoria() != category){
                category = temp.getCategoria();

                for (int i = position; (i < produtos.size() && category == produtos.get(i).getCategoria()); i++){
                    if (produtos.get(i).getStatus() == constants.getStatusWait()){
                        productList += "*-" + constants.getNameCategoryItem(temp.getCategoria()) + "*\n";
                        break;
                    }
                }
            }

            if (temp.getStatus() != constants.getStatusOff())
                productList += temp.getNome()+ " - " + temp.getQuantidade()  + " " + constants.getNameUnidade()[temp.getUnidade()] + "\n";
            else {
                if (purchasedProductList.equals(""))
                    purchasedProductList = "\n*" + getString(R.string.cestaOk) + "*\n\n";

                purchasedProductList += "~" + temp.getNome()+ " - " + temp.getQuantidade()  + " " + constants.getNameUnidade()[temp.getUnidade()] + "~\n";
            }
            position++;
        }

        if (!purchasedProductList.equals("")) {
            productList += purchasedProductList;
        }

        return productList;
    }

    public void shareWhatsApp(String title, String data){
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);

        share.putExtra(Intent.EXTRA_TEXT, data);
        startActivity(Intent.createChooser(share, title));
    }

    public void callFragmentDefault(int fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //todos da viewpager 1 terão número impar e todos da viewpager 2 terão número par.
        switch (fragment){
            case 1:
                fragmentTransaction.replace(R.id.frmLClear, new DashBoardCategoria()).commit();
                break;

            case 2:
                fragmentTransaction.replace(R.id.frmLClearOther, new DashBoardOpcoesLCP()).commit();
                break;

            case 4:
                sProduto.setCategoria(-1);
                fragmentTransaction.replace(R.id.frmLClearOther, new FragSaveProduct(), "frag" + PAGER_2).commit();
                break;
        }
    }

    public void buttonClicked(View view){

        if ((view instanceof ImageView)) {
            switch (view.getTag().toString()){
                case "back":
                    finish();
                    break;

                case "refresh":
                    new CommFirebase().sendDataInt(dbOutStatus, constants.getPathDespensa()+constants.getPathFlgDsp(), new Random().nextInt(constants.rangeRandom));
                    new CommFirebase().sendDataInt(dbOutStatus, constants.getPathMinhaLista()+constants.getPathFlgMlst(), new Random().nextInt(constants.rangeRandom));
                    break;
            }
        }
    }

    /*
    //click no dashboardCategoria
    public void ivClickedCpDB(View item){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if ((item instanceof ImageView) || (item instanceof TextView)) {
            switch (page){
                case 1:
                    fragmentTransaction.replace(R.id.frmLClear, new FragCategoria((int) item.getTag()), "frag" + page).commit();
                    break;

                case 2:
                    new CommFirebase().sendDataInt(dbOutStatus, constants.getPathDespensa()+"/"+item.getTag()+"/"+sProduto.getNome(), sProduto.getUnidade());
                    new CommFirebase().sendDataInt(dbOutStatus, constants.getPathDespensa()+constants.getPathFlgDsp(), new Random().nextInt(constants.rangeRandom));

                    sProduto.setCategoria(-1);
                    Snackbar.make(item, getString(R.string.saveOk), Snackbar.LENGTH_LONG).setAction("Action", null).show();

                    fragmentTransaction.replace(R.id.frmLClearOther, new FragSaveProduct(), "frag" + PAGER_2).commit();
                    break;
            }
        }
    }
     */

    public void clickIvTabs (View view){
        if ((view instanceof ImageView)){
            switch (view.getId()){
                case R.id.ivTab0:
                    //tabLayout.selectTab(tabLayout.getTabAt(0));
                    //tabLayout.Tab.selectTab(tabLayout.getTabAt(0));
                    tabLayout.getTabAt(constants.getPAGER_0()).select();
                    break;

                case R.id.ivTab1:
                    //tabLayout.selectTab(tabLayout.getTabAt(1));
                    tabLayout.getTabAt(constants.getPAGER_1()).select();
                    break;

                case R.id.ivTab2:
                    //tabLayout.selectTab(tabLayout.getTabAt(2));
                    tabLayout.getTabAt(constants.getPAGER_2()).select();
                    break;
            }
        }
    }

    public void hideSoftKeyboard(View view) {
        view.clearFocus();
        InputMethodManager imm =  (InputMethodManager) getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public DBProduto getsProduto() {
        return sProduto;
    }

    public void setsProduto(DBProduto sProduto) {
        this.sProduto = sProduto;
    }

    public int getPage() {
        return page;
    }

    public DatabaseReference getDbOutStatus() {
        return dbOutStatus;
    }

    public boolean isFlgUpdateTab1() {
        return flgUpdateTab1;
    }

    public void setFlgUpdateTab1(boolean flgUpdateTab1) {
        this.flgUpdateTab1 = flgUpdateTab1;
    }
}