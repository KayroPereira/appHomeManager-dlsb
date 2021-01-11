package com.example.apphomemanager;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.apphomemanager.Communication.CommFirebase;
import com.example.apphomemanager.GeneralUse.ConstantsApp;
import com.example.apphomemanager.GeneralUse.PumpProtection;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PumpProtectSetupActivity extends AppCompatActivity {

    private EditText etBoxDelayPPTS;
    private EditText etBoxRearmPPTS;
    private EditText etBoxFlowMaxPPTS;
    private EditText etBoxFlowPercentPPTS;

    private ImageView ivSendPPTS;
    private ImageView ivBackPPTS;

    private PumpProtection datasPumpProtection = new PumpProtection();

    private String  PATH_ROOT,
                    FULL_PATH;

    final ConstantsApp constants = new ConstantsApp();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();

    final DatabaseReference dbOutStatus = reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pump_protect_setup);

        etBoxDelayPPTS = (EditText) findViewById(R.id.etBoxDelayPPTS);
        etBoxRearmPPTS = (EditText) findViewById(R.id.etBoxRearmPPTS);
        etBoxFlowMaxPPTS = (EditText) findViewById(R.id.etBoxFlowMaxPPTS);
        etBoxFlowPercentPPTS = (EditText) findViewById(R.id.etBoxFlowPercentPPTS);

        ivSendPPTS = (ImageView) findViewById(R.id.ivSendPPTS);
        ivBackPPTS = (ImageView) findViewById(R.id.ivBackPPTS);

        PATH_ROOT = constants.getPathRoot();
        FULL_PATH = PATH_ROOT+constants.getPathPumpProtect();

        new CommFirebase().sendDataInt(dbOutStatus, FULL_PATH+constants.getPathFlgPPT(), 1);

        dbOutStatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PumpProtection tempData = new CommFirebase().getDataPumpProtection(dataSnapshot);

                if (isUpdateData(tempData))
                    startComponents();

                //Toast.makeText(getApplicationContext(), "btBoxSend On", Toast.LENGTH_SHORT).show();

                //Log.w("Firebase", database.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean isUpdateData(PumpProtection data){
        //en, err, flg, sb, st, stm, tdw, tra, trm, vz, vzm, pvz
        if ((data.getTdw() != datasPumpProtection.getTdw()) || (data.getTrm() != datasPumpProtection.getTrm())
            || (data.getVzm() != datasPumpProtection.getVzm()) || (data.getPvz() != datasPumpProtection.getPvz())) {
            datasPumpProtection = data;
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void buttonClickedPPTS(View item){

        switch (item.getId()){
            case R.id.ivBackPPTS:
                //Toast.makeText(getApplicationContext(), "btBoxBack On", Toast.LENGTH_SHORT).show();
                finish();
                break;

            case R.id.ivSendPPTS:
                //Toast.makeText(getApplicationContext(), "btBoxSend On", Toast.LENGTH_SHORT).show();
                hideSoftKeyboard();
                btBoxSendClick();
                break;
        }
    }

    private void btBoxSendClick() {
        CommFirebase gate = new CommFirebase();

        gate.sendDataInt(dbOutStatus, FULL_PATH+constants.getPathTdwPPT(), Integer.parseInt(etBoxDelayPPTS.getText().toString()));
        gate.sendDataInt(dbOutStatus, FULL_PATH+constants.getPathTrmPPT(), Integer.parseInt(etBoxRearmPPTS.getText().toString()));
        gate.sendDataInt(dbOutStatus, FULL_PATH+constants.getPathVzmPPT(), Integer.parseInt(etBoxFlowMaxPPTS.getText().toString()));
        gate.sendDataInt(dbOutStatus, FULL_PATH+constants.getPathPvzPPT(), Integer.parseInt(etBoxFlowPercentPPTS.getText().toString()));

        gate.sendDataInt(dbOutStatus, FULL_PATH+constants.getPathFlgPPT(), 1);
        Toast.makeText(getApplicationContext(), getString(R.string.msg3), Toast.LENGTH_SHORT).show();
    }

    void startComponents(){

        etBoxDelayPPTS.setText(datasPumpProtection.getTdw()+"");
        etBoxRearmPPTS.setText(datasPumpProtection.getTrm()+"");
        etBoxFlowMaxPPTS.setText(datasPumpProtection.getVzm()+"");
        etBoxFlowPercentPPTS.setText(datasPumpProtection.getPvz()+"");
    }
}