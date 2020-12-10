package com.example.apphomemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apphomemanager.Communication.CommFirebase;
import com.example.apphomemanager.GeneralUse.ConstantsApp;
import com.example.apphomemanager.GeneralUse.WaterTankData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WaterTankSetupActivity extends AppCompatActivity {

    private TextView tvReservoirPath1WTS;
    private TextView tvReservoirPath2WTS;
    private TextView tvReservoirPath3WTS;

    private EditText etBox1;
    private EditText etBox2;
    private EditText etBox3;
    private EditText etBoxLHWTS;
    private EditText etBoxLLWTS;

    private ImageView ivSendWTS;
    private ImageView ivBackWTS;

    private int mode;
    private WaterTankData datasReservoir = new WaterTankData();

    final ConstantsApp constants = new ConstantsApp();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();

    final DatabaseReference dbOutStatus = reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_tank_setup);

        tvReservoirPath1WTS = (TextView) findViewById(R.id.tvReservoirPath1WTS);
        tvReservoirPath2WTS = (TextView) findViewById(R.id.tvReservoirPath2WTS);
        tvReservoirPath3WTS = (TextView) findViewById(R.id.tvReservoirPath3WTS);

        etBox1 = (EditText) findViewById(R.id.etBox1WTS);
        etBox2 = (EditText) findViewById(R.id.etBox2WTS);
        etBox3 = (EditText) findViewById(R.id.etBox3WTS);
        etBoxLHWTS = (EditText) findViewById(R.id.etBoxLHWTS);
        etBoxLLWTS = (EditText) findViewById(R.id.etBoxLLWTS);

        ivSendWTS = (ImageView) findViewById(R.id.ivSendWTS);
        ivBackWTS = (ImageView) findViewById(R.id.ivBackWTS);

        mode = getIntent().getExtras().getInt("mode");
        startComponents(mode);

        dbOutStatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                WaterTankData tempData = new CommFirebase().getDataWaterTank(dataSnapshot, constants.getPathReservoir()[mode]);

                if (isUpdateData(tempData))
                    updateData(tempData, mode);

                //Toast.makeText(getApplicationContext(), "btBoxSend On", Toast.LENGTH_SHORT).show();

                //Log.w("Firebase", database.toString());
                /*
                statusComponent = new CommFirebase().getOutPut(dataSnapshot, "cix1");

                ViewGroup layout = (ViewGroup) findViewById(R.id.layoutKitchen);
                for (int i = 0; i < layout.getChildCount(); i++) {

                    View comp = layout.getChildAt(i);

                    if (comp instanceof ImageView) {

                        switch (comp.getId()){
                            case R.id.ivKitchenOnOff:
                                boolean action = statusComponent.getBtOnOff() == 1 ? false : true;
                                ivLightOnOff.setImageResource(action ? R.drawable.btoff1 : R.drawable.bton1);
                                controlComponent(true, 0);
                                controlComponent(action ? false : true, 1);
                                pgBar.setVisibility(View.INVISIBLE);
                                break;

                            case R.id.ivCadastro:
                                ivLight1.setImageResource(statusComponent.getLoutUn(0) == 0 ? R.drawable.btlight1 : R.drawable.btlight_on1);
                                break;

                            case R.id.ivConfiguracaoConf:
                                ivLight2.setImageResource(statusComponent.getLoutUn(1) == 0 ? R.drawable.btlight1 : R.drawable.btlight_on1);
                                break;

                            case R.id.ivKitchenLight3:
                                ivLight3.setImageResource(statusComponent.getLoutUn(2) == 0 ? R.drawable.btlight1 : R.drawable.btlight_on1);
                                break;

                            case R.id.ivKitchenPower1:
                                ivPower1.setImageResource(statusComponent.getPoutUn(0) == 0 ? R.drawable.cooker1 : R.drawable.cooker_on1);
                                break;

                            case R.id.ivKitchenPower2:
                                ivPower2.setImageResource(statusComponent.getPoutUn(1) == 0 ? R.drawable.microwave1 : R.drawable.microwave_on1);
                                break;

                            case R.id.ivKitchenPower3:
                                ivPower3.setImageResource(statusComponent.getPoutUn(2) == 0 ? R.drawable.refrigerator1 : R.drawable.refrigerator_on1);
                                break;

                            case R.id.ivKitchenPower4:
                                ivPower4.setImageResource(statusComponent.getPoutUn(3) == 0 ? R.drawable.washingmachine1 : R.drawable.washingmachine_on1);
                                break;

                            case R.id.ivKitchenPower5:
                                ivPower5.setImageResource(statusComponent.getPoutUn(4) == 0 ? R.drawable.coffeemachine1 : R.drawable.coffeemachine_on1);
                                break;

                            case R.id.ivKitchenPower6:
                                ivPower6.setImageResource(statusComponent.getPoutUn(5) == 0 ? R.drawable.liquefier1 : R.drawable.liquefier_on1);
                                break;

                            case R.id.ivKitchenPower7:
                                ivPower7.setImageResource(statusComponent.getPoutUn(6) == 0 ? R.drawable.power1 : R.drawable.power_on1);
                                break;

                            case R.id.ivKitchenPower8:
                                ivPower8.setImageResource(statusComponent.getPoutUn(7) == 0 ? R.drawable.power1 : R.drawable.power_on1);
                                break;

                            case R.id.ivKitchenPower9:
                                ivPower9.setImageResource(statusComponent.getPoutUn(8) == 0 ? R.drawable.power1 : R.drawable.power_on1);
                                break;
                        }
                    }
                }
                */
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean isUpdateData(WaterTankData data){

        if (data.getLh() != datasReservoir.getLh())
            return true;

        if (data.getLl() != datasReservoir.getLl())
            return true;

        for (int i = 0; i < data.getAddress().length; i++){
            if (!data.getAddress()[i].equals(datasReservoir.getAddress()[i]))
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

    public void buttonClicked(View item){

        switch (item.getId()){
            case R.id.ivBackWTS:
                //Toast.makeText(getApplicationContext(), "btBoxBack On", Toast.LENGTH_SHORT).show();
                finish();
                break;

            case R.id.ivSendWTS:
                //Toast.makeText(getApplicationContext(), "btBoxSend On", Toast.LENGTH_SHORT).show();
                hideSoftKeyboard();
                btBoxSendClick();
                break;
        }
    }

    private void btBoxSendClick() {
        CommFirebase gate = new CommFirebase();

        gate.sendDataInt(dbOutStatus, constants.getPathReservoir()[mode]+"/set/LH", Integer.parseInt(etBoxLHWTS.getText().toString()));
        gate.sendDataInt(dbOutStatus, constants.getPathReservoir()[mode]+"/set/LL", Integer.parseInt(etBoxLLWTS.getText().toString()));
        switch(mode){
            case 0:
                //gate.sendDataInt(dbOutStatus, "kitchen/l/o1", 6);
                gate.sendDataString(dbOutStatus, constants.getPathReservoir()[mode]+"/abx1", etBox1.getText().toString());
                gate.sendDataString(dbOutStatus, constants.getPathReservoir()[mode]+"/abx2", etBox2.getText().toString());
                gate.sendDataString(dbOutStatus, constants.getPathReservoir()[mode]+"/abx3", etBox3.getText().toString());
                break;

            case 1:
                gate.sendDataString(dbOutStatus, constants.getPathReservoir()[mode]+"/ci", etBox1.getText().toString());
                break;
        }

        gate.sendDataInt(dbOutStatus, constants.getPathReservoir()[mode]+"/fcp", 1);
        Toast.makeText(getApplicationContext(), getString(R.string.msg3), Toast.LENGTH_SHORT).show();
    }

    void updateData (WaterTankData data, int mode){
        String tempS[] = new String[data.getAddress().length];

        etBox1.setText(data.getAddress()[0]);
        etBoxLHWTS.setText(data.getLh()+"");
        etBoxLLWTS.setText(data.getLl()+"");

        switch(mode){
            case 0:
                etBox2.setText(data.getAddress()[1]);
                etBox3.setText(data.getAddress()[2]);
                break;

            case 1:
                break;
        }

        datasReservoir.setLh(data.getLh());
        datasReservoir.setLl(data.getLl());

        for (int i = 0; i < datasReservoir.getAddress().length; i++){
            tempS[i] = data.getAddress()[i];
        }
        datasReservoir.setAddress(tempS);
    }

    void startComponents(int mode){

        etBox1.setEnabled(true);

        switch(mode){
            case 0:
                tvReservoirPath1WTS.setText(getString(R.string.caixa) + " 1");
                tvReservoirPath2WTS.setText(getString(R.string.caixa) + " 2");
                tvReservoirPath3WTS.setText(getString(R.string.caixa) + " 3");

                tvReservoirPath2WTS.setVisibility(View.VISIBLE);
                tvReservoirPath3WTS.setVisibility(View.VISIBLE);
                etBox2.setVisibility(View.VISIBLE);
                etBox3.setVisibility(View.VISIBLE);
                break;

            case 1:
                tvReservoirPath1WTS.setText(getString(R.string.cisterna));

                etBox2.setVisibility(View.GONE);
                etBox3.setVisibility(View.GONE);
                tvReservoirPath2WTS.setVisibility(View.GONE);
                tvReservoirPath3WTS.setVisibility(View.GONE);
                break;
        }
    }
}