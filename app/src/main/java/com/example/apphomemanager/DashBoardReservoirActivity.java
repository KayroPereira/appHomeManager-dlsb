package com.example.apphomemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.apphomemanager.GeneralUse.ConstantsApp;

public class DashBoardReservoirActivity extends AppCompatActivity {

    private ImageView ivBackDBR;
    private ImageView ivCisternDBR;
    private ImageView ivWaterTankDBR;
    private ImageView ivPumpProtectionDBR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_dash_board_reservoir);

        ivBackDBR = (ImageView) findViewById(R.id.ivBackDBR);
        ivCisternDBR = (ImageView) findViewById(R.id.ivCisternDBR);
        ivWaterTankDBR = (ImageView) findViewById(R.id.ivWaterTankDBR);
        ivPumpProtectionDBR = (ImageView) findViewById(R.id.ivPumpProtectionDBR);

        ivCisternDBR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(DashBoardReservoirActivity.this, WaterTankActivity.class);
                it.putExtra("mode", new ConstantsApp().getCISTERN());
                //it.putExtra("mode", new ConstantsApp().getWATER_TANK());
                startActivity(it);
            }
        });

        ivWaterTankDBR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(DashBoardReservoirActivity.this, WaterTankActivity.class);
                //it.putExtra("mode", new ConstantsApp().getCISTERN());
                it.putExtra("mode", new ConstantsApp().getWATER_TANK());
                startActivity(it);
            }
        });

        ivPumpProtectionDBR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(DashBoardReservoirActivity.this, PumpProtectActivity.class);
                //it.putExtra("mode", new ConstantsApp().getCISTERN());
                it.putExtra("mode", new ConstantsApp().getWATER_TANK());
                startActivity(it);
            }
        });
    }

    public void buttonClickedDBR(View item){

        switch (item.getId()){
            case R.id.ivBackDBR:
                //Toast.makeText(getApplicationContext(), "btBoxBack On", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
}
