package com.example.apphomemanager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.apphomemanager.Communication.CommFirebase;
import com.example.apphomemanager.GeneralUse.ConstantsApp;
import com.example.apphomemanager.GeneralUse.WaterTankData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WaterTankActivity extends AppCompatActivity {

    private TextView tvBoxReservoirWT;
    private TextView tvBoxStatusWT;
    private TextView tvBoxErrWT;
    private TextView tvLevelHWT;
    private TextView tvLevelWT;
    private TextView tvLevelLWT;
    private TextView tvDeviceHWT;

    private ImageView ivSetupWT;
    private ImageView ivBackWT;

    private SeekBar skbModeWT;

    private ImageView ivReservoir;

    private int mode;
    private WaterTankData datasReservoir = new WaterTankData();

    final ConstantsApp constants = new ConstantsApp();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();

    final DatabaseReference dbOutStatus = reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_tank);

        tvBoxReservoirWT = (TextView) findViewById(R.id.tvBoxReservoirWT);
        tvBoxStatusWT = (TextView) findViewById(R.id.tvBoxStatusWT);
        tvBoxErrWT = (TextView) findViewById(R.id.tvBoxErrWT);
        tvLevelHWT = (TextView) findViewById(R.id.tvLevelHWT);
        tvLevelWT = (TextView) findViewById(R.id.tvLevelWT);
        tvLevelLWT = (TextView) findViewById(R.id.tvLevelLWT);
        tvDeviceHWT = (TextView) findViewById(R.id.tvDeviceHWT);

        ivSetupWT = (ImageView) findViewById(R.id.ivSetupWT);
        ivBackWT = (ImageView) findViewById(R.id.ivBackWT);

        skbModeWT = (SeekBar) findViewById(R.id.skbModeWT);

        ivReservoir = (ImageView) findViewById(R.id.ivReservoir);

        mode = getIntent().getExtras().getInt("mode");
        new CommFirebase().sendDataInt(dbOutStatus, constants.getPathReservoir()[mode]+constants.getPathFcp(), 1);

        //Toast.makeText(getApplicationContext(), "Inicio", Toast.LENGTH_SHORT).show();

        skbModeWT.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                new CommFirebase().sendDataInt(dbOutStatus, constants.getPathReservoir()[mode]+constants.getPathDeviceSet()[mode],
                                                i == 2 ? constants.getHIGH() : i == 1 ? constants.getAUTO() : constants.getLOW());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        dbOutStatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                WaterTankData tempData = new CommFirebase().getDataWaterTank(dataSnapshot, constants.getPathReservoir()[mode]);

                if (isUpdateData(tempData))
                    startComponents(mode);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean isUpdateData(WaterTankData data){

        if ((data.getFcp() != datasReservoir.getFcp()) || (data.getErr() != datasReservoir.getErr()) ||
            (data.getLevel() != datasReservoir.getLevel()) || (data.getSx1() != datasReservoir.getSx1()) ||
            (data.getX1s() != datasReservoir.getX1s())) {
            datasReservoir = data;
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void buttonClickedWT(View item){

        switch (item.getId()){
            case R.id.ivBackWT:
                //Toast.makeText(getApplicationContext(), "btBoxBack On", Toast.LENGTH_SHORT).show();
                finish();
                break;

            case R.id.ivSetupWT:
                //Toast.makeText(getApplicationContext(), "btBoxSend On", Toast.LENGTH_SHORT).show();
                Intent it = new Intent(this, WaterTankSetupActivity.class);
                //it.putExtra("mode", new ConstantsApp().getCISTERN());
                //it.putExtra("mode", new ConstantsApp().getWATER_TANK());
                it.putExtra("mode", mode);
                startActivity(it);
                break;
        }
    }

    void startComponents(int mode){

        tvBoxReservoirWT.setText(mode == 0 ? getString(R.string.cisterna) : getString(R.string.caixa));
        tvBoxStatusWT.setText(datasReservoir.getFcp() == 0 ? getString(R.string.onLine) : getString(R.string.offLine));
        tvLevelHWT.setText(getString(R.string.nivel) + " " + getString(R.string.sup) + ": " + datasReservoir.getLh());
        tvLevelLWT.setText(getString(R.string.nivel) + " " + getString(R.string.inf) + ": " + datasReservoir.getLl());
        tvLevelWT.setText(getString(R.string.nivel) + " " + getString(R.string.atual) + ": " + datasReservoir.getLevel());
        tvDeviceHWT.setText(constants.getDeviceType()[mode] + " " +(datasReservoir.getSx1() == 0 ? getString(R.string.desligada) : getString(R.string.ligada)));

        tvDeviceHWT.setTextColor(datasReservoir.getSx1() == 0 ? Color.RED : Color.GREEN);

        skbModeWT.setProgress(datasReservoir.getX1s() == 2 ? constants.getHIGH() : datasReservoir.getX1s() == 1 ? constants.getAUTO() : constants.getLOW());

        ivReservoir.setImageResource(this.getResources().getIdentifier(constants.getImagePath()[mode] + getImageLevel(datasReservoir), "drawable", this.getPackageName()));

        if (datasReservoir.getErr() == 0) {
            tvBoxErrWT.setVisibility(View.INVISIBLE);
        }else{
            tvBoxErrWT.setVisibility(View.VISIBLE);
            tvBoxErrWT.setText(constants.getErros()[datasReservoir.getErr()]);
        }
    }

    int getImageLevel(WaterTankData data){
        int range = (data.getLl() - data.getLh()) / constants.getImageLength();
        int value = ((data.getLl() - data.getLevel() - range) / range);

        if (value < 0)
            return 0;
        else
            if (value >= constants.getImageLength())
                return constants.getImageLength()-1;
            else
                return value;
    }
}