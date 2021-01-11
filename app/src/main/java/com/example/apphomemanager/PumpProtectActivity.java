package com.example.apphomemanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
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

public class PumpProtectActivity extends AppCompatActivity {

    private TextView tvBoxStatusPPT;
    private TextView tvSensorTensaoBoiaPPT;
    private TextView tvBoxVazaoPPT;
    private TextView tvErroPPT;
    private TextView tvTimeRearmPPT;
    private TextView tvLabelRearmPPT;

    private ImageView ivSetupPPT;
    private ImageView ivBackPPT;
    private ImageView ivOnOffPPT;

    private SeekBar skbModePPT;

    private AlertDialog alerta;

    private PumpProtection datasPumpProtection = new PumpProtection();

    final ConstantsApp constants = new ConstantsApp();

    private String  PATH_ROOT,
                    FULL_PATH;

    private int valueSeebarOld = 0,
                targetPercentFlowWater = 0;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();

    final DatabaseReference dbOutStatus = reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pump_protect);

        tvBoxStatusPPT = (TextView) findViewById(R.id.tvBoxStatusPPT);
        tvSensorTensaoBoiaPPT = (TextView) findViewById(R.id.tvSensorTensaoBoiaPPT);
        tvBoxVazaoPPT = (TextView) findViewById(R.id.tvBoxVazaoPPT);
        tvErroPPT = (TextView) findViewById(R.id.tvErroPPT);
        tvTimeRearmPPT = (TextView) findViewById(R.id.tvTimeRearmPPT);
        tvLabelRearmPPT = (TextView) findViewById(R.id.tvLabelRearmPPT);

        ivBackPPT = (ImageView) findViewById(R.id.ivBackPPT );
        ivSetupPPT = (ImageView) findViewById(R.id.ivSetupPPT );
        ivOnOffPPT = (ImageView) findViewById(R.id.ivOnOffPPT);

        skbModePPT = (SeekBar) findViewById(R.id.skbModePPT );

        PATH_ROOT = constants.getPathRoot();
        FULL_PATH = PATH_ROOT+constants.getPathPumpProtect();

        ivOnOffPPT.setImageResource(R.drawable.btoff);

        new CommFirebase().sendDataInt(dbOutStatus, FULL_PATH+constants.getPathFlgPPT(), 1);

        //Toast.makeText(getApplicationContext(), "Inicio", Toast.LENGTH_SHORT).show();

        skbModePPT .setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

//                Log.d("teste", "Changed: " + "i: " + i + " b: " + b);

                if (b) {
                    if (i > 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PumpProtectActivity.this);
                        builder.setTitle(getString(R.string.msgTituloModoManualPPT));
                        builder.setMessage(getString(R.string.msgModoManualPPT));
                        builder.setPositiveButton(getString(R.string.sim), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {

                                valueSeebarOld = seekBar.getProgress();

                                if(updateFirebase(1)){
                                    Toast.makeText(PumpProtectActivity.this, getString(R.string.msgModoManualAtivadoPPT), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        builder.setNegativeButton(getString(R.string.nao), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {

                                if(updateFirebase(0)){
                                    Toast.makeText(PumpProtectActivity.this, getString(R.string.msgModoManualNaoAtivadoPPT), Toast.LENGTH_SHORT).show();
                                }

                                datasPumpProtection.setStm(0);
                                skbModePPT.setProgress(datasPumpProtection.getStm());

                                valueSeebarOld = seekBar.getProgress();

//                            Log.d("teste", "não: " + "i: " + i + " b: " + b);
//                            Log.d("teste", "Nao ------> value atual: " + seekBar.getProgress() + " value old: " + valueSeebarOld);
                            }
                        });

                        alerta = builder.create();
                        alerta.show();
                    }else {

//                        new CommFirebase().sendDataInt(dbOutStatus, FULL_PATH + constants.getPathStmPPT(), 0);
                        if(updateFirebase(0)) {
                            Toast.makeText(PumpProtectActivity.this, getString(R.string.msgModoManualDesativadoPPT), Toast.LENGTH_SHORT).show();
                        }

//                    Log.d("teste", "i <= 0: " + "i: " + i + " b: " + b);

                        valueSeebarOld = seekBar.getProgress();
//                    Log.d("teste", "i == 0 ------> value atual: " + seekBar.getProgress() + " value old: " + valueSeebarOld);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                Log.d("teste", "Start ------> value atual: " + seekBar.getProgress() + " value old: " + valueSeebarOld);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                Log.d("teste", "Stop ------> value atual: " + seekBar.getProgress() + " value old: " + valueSeebarOld);

                if(seekBar.getProgress() != valueSeebarOld){
                    skbModePPT.setProgress(valueSeebarOld);
                    valueSeebarOld = seekBar.getProgress();
                }
            }
        });

        dbOutStatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PumpProtection tempData = new CommFirebase().getDataPumpProtection(dataSnapshot);

//                Log.i("teste", tempData.toString());

                if (isUpdateData(tempData)) {
//                    Log.d("teste", "Vzm: " + datasPumpProtection.getVzm());
//                    Log.d("teste", "Pvz: " + datasPumpProtection.getPvz());
                    targetPercentFlowWater = percentFlowWater(datasPumpProtection.getVzm(), datasPumpProtection.getPvz());
                    startComponents();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean isUpdateData(PumpProtection data){
    //en, err, flg, sb, st, stm, tdw, tra, trm, vz
        if ((data.getErr() != datasPumpProtection.getErr()) || (data.getFlg() != datasPumpProtection.getFlg()) ||
            (data.getSb() != datasPumpProtection.getSb()) || (data.getSt() != datasPumpProtection.getSt()) ||
            (data.getStm() != datasPumpProtection.getStm()) || (data.getTdw() != datasPumpProtection.getTdw()) ||
            (data.getTra() != datasPumpProtection.getTra()) || (data.getTrm() != datasPumpProtection.getTrm()) ||
            (data.getVz() != datasPumpProtection.getVz()) || (data.getEn() != datasPumpProtection.getEn())) {
            datasPumpProtection = data;
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void buttonClickedPPT (View item){

        switch (item.getId()){
            case R.id.ivBackPPT:
//                Toast.makeText(getApplicationContext(), "btBoxBack On", Toast.LENGTH_SHORT).show();
                finish();
                break;

            case R.id.ivSetupPPT:
                //Toast.makeText(getApplicationContext(), "btBoxSend On", Toast.LENGTH_SHORT).show();
                Intent it = new Intent(this, PumpProtectSetupActivity.class);
                startActivity(it);
                break;

            case R.id.ivOnOffPPT:
                new CommFirebase().sendDataInt(dbOutStatus, FULL_PATH+constants.getPathEnPPT(), datasPumpProtection.getEn() == 0 ? 1 : 0);
                break;
        }
    }

    void startComponents() {

        //en, err, flg, sb, st, stm, tdw, tra, trm, vz, vzm, pvz

        if (datasPumpProtection.getEn() == 0) {
            ivOnOffPPT.setImageResource(R.drawable.btoff);
        } else {
            ivOnOffPPT.setImageResource(R.drawable.bton);
        }

        tvBoxStatusPPT.setText(getString(R.string.pump) + " " + (datasPumpProtection.getSb() == 0 ? getString(R.string.desligada) : getString(R.string.ligada)));
        tvSensorTensaoBoiaPPT.setText(getString(R.string.boia) + " " + (datasPumpProtection.getSt() == 0 ? getString(R.string.boiaDesacionada) : getString(R.string.boiaAcionada)));
        tvBoxVazaoPPT.setText(getString(R.string.fluxoDePPT) + " " + datasPumpProtection.getVz() + " " + (datasPumpProtection.getVz() == 1 ? getString(R.string.pulso) : getString(R.string.pulsos)));

        if (datasPumpProtection.getErr() >= constants.getMESAGENS_ERROS().length) {
            datasPumpProtection.setErr(constants.getMESAGENS_ERROS().length - 1);
        }

        tvErroPPT.setText(constants.getMESAGENS_ERROS()[datasPumpProtection.getErr()]);

        if (datasPumpProtection.getTra() != 0) {
            int[] timeConvertido = splitTime(datasPumpProtection.getTra());

            String time = "";

            if (timeConvertido[0] != 0) {
                time += timeConvertido[0] + getString(R.string.horaPPT) + ":";
            }

            time += timeConvertido[1] + getString(R.string.minutoPPT);

            tvTimeRearmPPT.setText(getString(R.string.msgRearmePPT) + " " + time);
            tvTimeRearmPPT.setVisibility(View.VISIBLE);
            tvLabelRearmPPT.setVisibility(View.VISIBLE);
        } else {
            tvTimeRearmPPT.setVisibility(View.GONE);
            tvLabelRearmPPT.setVisibility(View.GONE);
        }

        int greenPerson = getResources().getColor(R.color.colorTextGreen),
            orangePerson = getResources().getColor(R.color.colorTextOrange);

//        Log.d("teste", "targetPercentFlowWater: " + targetPercentFlowWater);
//        Log.d("teste", "datasPumpProtection: " + datasPumpProtection.getVz());

        tvBoxStatusPPT.setTextColor(datasPumpProtection.getSb() == 0 ? orangePerson : greenPerson);
        tvSensorTensaoBoiaPPT.setTextColor(datasPumpProtection.getSt() == 0 ? orangePerson : greenPerson);
//        tvBoxVazaoPPT.setTextColor(datasPumpProtection.getVz() > 0 ? greenPerson : Color.BLUE);
        tvBoxVazaoPPT.setTextColor(datasPumpProtection.getVz() >= targetPercentFlowWater ? greenPerson : datasPumpProtection.getVz() == 0 ? Color.BLUE : Color.RED);

        tvErroPPT.setTextColor(datasPumpProtection.getErr() == 0 ? greenPerson : Color.RED);
        tvTimeRearmPPT.setTextColor(datasPumpProtection.getTra() < 6 ? greenPerson : Color.RED);

        skbModePPT.setProgress(datasPumpProtection.getStm());
        valueSeebarOld = datasPumpProtection.getStm();
    }

    int[] splitTime(int time){
        int[] timeConvertido = new int[2];

        timeConvertido[0] = time / 60;
        timeConvertido[1] = time % 60;

        return timeConvertido;
    }

    private boolean updateFirebase(int value){
        try {
            new CommFirebase().sendDataInt(dbOutStatus, FULL_PATH + constants.getPathStmPPT(), value);
//            Toast.makeText(PumpProtectActivity.this, getString(R.string.msgModoManualAtivadoPPT), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), getString(R.string.msgErroAtualizacaoDadosPPT), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private int percentFlowWater(int pulse, int percent){
        return (int) (pulse * ((float) percent / 100));
    }
}