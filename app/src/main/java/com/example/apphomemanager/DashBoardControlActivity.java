package com.example.apphomemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class DashBoardControlActivity extends AppCompatActivity {

/*
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();

    final DatabaseReference dbOutStatus = reference;

    private AlertDialog alerta;

    private ImageView ivBackDBC;
*/

    private ImageView ivLivingRoom;
    private ImageView ivKitchen;
    private ImageView ivBedRoom1;
    private ImageView ivBedRoom2;
    private ImageView ivBedRoom3;
    private ImageView ivBedRoom4;
    private ImageView ivBathRoom1;
    private ImageView ivBathRoom2;
    private ImageView ivHall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board_control);

        ivLivingRoom = (ImageView) findViewById(R.id.ivSalaDBC);
        ivKitchen = (ImageView) findViewById(R.id.ivCozinhaDBC);
        //ivBackDBC = (ImageView) findViewById(R.id.ivBackDBC);

        ivBedRoom1 = (ImageView) findViewById(R.id.ivBedRoom1DBC);
        ivBedRoom2 = (ImageView) findViewById(R.id.ivBedRoom2DBC);
        ivBedRoom3 = (ImageView) findViewById(R.id.ivBedRoom3DBC);
        ivBedRoom4 = (ImageView) findViewById(R.id.ivBedRoom4DBC);

        ivBathRoom1 = (ImageView) findViewById(R.id.ivBanheiro1DBC);
        ivBathRoom2 = (ImageView) findViewById(R.id.ivBanheiro2DBC);

        ivHall = (ImageView) findViewById(R.id.ivHallDBC);

        ivLivingRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(DashBoardControlActivity.this, LivingRoomActivity.class);
                startActivity(it);
            }
        });

        ivKitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(DashBoardControlActivity.this, KitchenRoomActivity.class);
                startActivity(it);
            }
        });

        ivBedRoom1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(DashBoardControlActivity.this, BedRoom1Activity.class);
                startActivity(it);
            }
        });

        ivBedRoom2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(DashBoardControlActivity.this, BedRoom2Activity.class);
                startActivity(it);
            }
        });

        ivBedRoom3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(DashBoardControlActivity.this, BedRoom3Activity.class);
                startActivity(it);
            }
        });

        ivBedRoom4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(DashBoardControlActivity.this, BedRoom4Activity.class);
                startActivity(it);
            }
        });

        ivBathRoom1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(DashBoardControlActivity.this, BathRoom1Activity.class);
                startActivity(it);
            }
        });

        ivBathRoom2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(DashBoardControlActivity.this, BathRoom2Activity.class);
                startActivity(it);
            }
        });

        ivHall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(DashBoardControlActivity.this, HallActivity.class);
                startActivity(it);
            }
        });
    }

    public void buttonClicked(View item){

        switch (item.getId()){
            case R.id.ivBackDBC:
                //Toast.makeText(getApplicationContext(), "btBoxBack On", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
}
