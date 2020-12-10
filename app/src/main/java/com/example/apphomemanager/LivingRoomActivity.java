package com.example.apphomemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.apphomemanager.Communication.CommFirebase;
import com.example.apphomemanager.GeneralUse.ComponentStatus;
import com.example.apphomemanager.GeneralUse.ConstantsApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LivingRoomActivity extends AppCompatActivity {

    private ViewGroup vgPrincipal;
    private ProgressBar pgBar;
    private ImageView ivLightOnOff;

    private ImageView ivBack;

    private ImageView ivLight1;
    private ImageView ivLight2;
    private ImageView ivLight3;
    private ImageView ivLight4;
    private ImageView ivLight5;
    private ImageView ivLight6;

    private ImageView ivPower1;
    private ImageView ivPower2;
    private ImageView ivPower3;
    private ImageView ivPower4;
    private ImageView ivPower5;
    private ImageView ivPower6;

    private ArrayList<ImageView> lightDevice = new ArrayList<>();
    private ArrayList<ImageView> powerDevice = new ArrayList<>();

    private String PATH_DEVICE;
    private int TYPE_DEVICE;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference = database.getReference();

    final DatabaseReference dbOutStatus = reference;
    final ConstantsApp constants = new ConstantsApp();

    private ComponentStatus statusComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_living_room);

        //zona de adaptação para outros ambientes
        vgPrincipal = (ViewGroup) findViewById(R.id.ctnlPrincipalLVR);

        pgBar = (ProgressBar) findViewById(R.id.pgBarCommLVR);

        ivLightOnOff = (ImageView) findViewById(R.id.ivOnOffLVR);

        ivBack = (ImageView) findViewById(R.id.ivBackLVR);

        ivLight1 = (ImageView) findViewById(R.id.ivLight1LVR);
        ivLight2 = (ImageView) findViewById(R.id.ivLight2LVR);
        ivLight3 = (ImageView) findViewById(R.id.ivLight3LVR);
        ivLight4 = (ImageView) findViewById(R.id.ivLight4LVR);
        ivLight5 = (ImageView) findViewById(R.id.ivLight5LVR);
        ivLight6 = (ImageView) findViewById(R.id.ivLight6LVR);

        ivPower1 = (ImageView) findViewById(R.id.ivPower1LVR);
        ivPower2 = (ImageView) findViewById(R.id.ivPower2LVR);
        ivPower3 = (ImageView) findViewById(R.id.ivPower3LVR);
        ivPower4 = (ImageView) findViewById(R.id.ivPower4LVR);
        ivPower5 = (ImageView) findViewById(R.id.ivPower5LVR);
        ivPower6 = (ImageView) findViewById(R.id.ivPower6LVR);

        lightDevice.add(ivLight1);
        lightDevice.add(ivLight2);
        lightDevice.add(ivLight3);
        lightDevice.add(ivLight4);
        lightDevice.add(ivLight5);
        lightDevice.add(ivLight6);

        powerDevice.add(ivPower1);
        powerDevice.add(ivPower2);
        powerDevice.add(ivPower3);
        powerDevice.add(ivPower4);
        powerDevice.add(ivPower5);
        powerDevice.add(ivPower6);

        TYPE_DEVICE = constants.getLIVING();
        //realizar as modificações até aqui

        pgBar.setVisibility(View.VISIBLE);

        ivLightOnOff.setImageResource(R.drawable.btoff);

        PATH_DEVICE = constants.getPathComodo()[TYPE_DEVICE];

        controlComponent(false, 0);
        controlComponent(false, 1);

        Log.w("Firebase", database.toString());
        Log.w("DataFirebase", reference.toString());

        dbOutStatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                statusComponent = new CommFirebase().getOutPut(dataSnapshot, PATH_DEVICE);

                for (int i = 0; i < vgPrincipal.getChildCount(); i++) {
                    View comp = vgPrincipal.getChildAt(i);

                    if (comp instanceof ImageView) {
                        switch (comp.getId()) {
                            case R.id.ivOnOffLVR:
                                boolean action = statusComponent.getBtOnOff() == 1 ? false : true;
                                ivLightOnOff.setImageResource(action ? R.drawable.btoff : R.drawable.bton);
                                controlComponent(true, 0);
                                controlComponent(action ? false : true, 1);
                                pgBar.setVisibility(View.INVISIBLE);
                                updateStatusComponent();
                                break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void controlComponent(boolean status, int mode){
        switch(mode) {
            case 0:
                ivLightOnOff.setEnabled(status);
                break;

            case 1:
                for (ImageView temp : lightDevice)
                    temp.setEnabled(status);

                for (ImageView temp : powerDevice)
                    temp.setEnabled(status);
                break;
        }
    }

    private int[] getTypeDevice(View item){
        int device[] = {0, 0};

        if (item instanceof ImageView) {
            if (item == ivBack)
                return device;

            device[0] = 1;
            if (item == ivLightOnOff)
                return device;

            int i = 0;
            device[0] = 2;
            for (ImageView temp : lightDevice) {
                if (temp == item) {
                    device[1] = i;
                    return device;
                }
                i++;
            }

            i = 0;
            device[0] = 3;
            for (ImageView temp : powerDevice) {
                if (temp == item) {
                    device[1] = i;
                    return device;
                }
                i++;
            }
        }
        device[0] = -1;
        return device;
    }

    public void buttonClickedLVR(View item){
        int device[] = getTypeDevice(item);

        switch (device[0]){
            case 0:
                finish();
                break;

            case 1:
                updateStatusDevice(statusComponent.getBtOnOff(), (ImageView) item, constants.getPathImageDeviceButton(), PATH_DEVICE+"/lonoff");
                controlComponent(statusComponent.getBtOnOff() == 1 ? false : true, 1);
                break;

            case 2:
                updateStatusDevice(statusComponent.getLoutUn(device[1]), (ImageView) item, constants.getPathImageDeviceLight(),
                        PATH_DEVICE+constants.getPathComodoOutType()[constants.getLIGHT()]+constants.getPathComodoOut()[device[1]]);
                break;

            case 3:
                updateStatusDevice(statusComponent.getPoutUn(device[1]), (ImageView) item, constants.getPathImageDevicePower(),
                        PATH_DEVICE+constants.getPathComodoOutType()[constants.getPOWER()]+constants.getPathComodoOut()[device[1]]);
                break;
        }
    }

    private void updateStatusDevice(byte statusComponente, ImageView iv, String[] pathImage, String pathFirebase){
        CommFirebase gate = new CommFirebase();
        try{
            boolean action = statusComponente == 1 ? false : true;
            this.getResources().getIdentifier(pathImage[action ? 0 : 1], "drawable", this.getPackageName());
            gate.sendDataInt(dbOutStatus, pathFirebase, action ? 1 : 0);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Não foi possível obter os dados", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateStatusComponent(){
        if (statusComponent.getBtOnOff() != 0) {
            int i = 0;
            for (ImageView temp : lightDevice)
                temp.setImageResource(statusComponent.getLoutUn(i++) == 0 ? R.drawable.lampadaoff : R.drawable.lampadaon);

            i = 0;
            for (ImageView temp : powerDevice)
                temp.setImageResource(statusComponent.getPoutUn(i++) == 0 ? R.drawable.tomadaoff : R.drawable.tomadaon);
        }else{
            for (ImageView temp : lightDevice)
                temp.setImageResource(R.drawable.lampadaoff);

            for (ImageView temp : powerDevice)
                temp.setImageResource(R.drawable.tomadaoff);
        }
    }
}