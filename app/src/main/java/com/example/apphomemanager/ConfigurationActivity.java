package com.example.apphomemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ConfigurationActivity extends AppCompatActivity {

    private ImageView ivBackCFG;
    private ImageView ivCadastroCFG;
    private ImageView ivCardRecordCFG;
    private ImageView ivConfWifiCFG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        ivBackCFG = (ImageView) findViewById(R.id.ivBackCFG);

        ivCadastroCFG = (ImageView) findViewById(R.id.ivCadastroCFG);
        ivCardRecordCFG = (ImageView) findViewById(R.id.ivCardRecordCFG);
        ivConfWifiCFG = (ImageView) findViewById(R.id.ivConfWifiCFG);

        ivCadastroCFG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ConfigurationActivity.this, CadastroActivity.class);
                startActivity(it);
            }
        });

        ivCardRecordCFG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ConfigurationActivity.this, doorLockActivity.class);
                startActivity(it);
            }
        });

        ivConfWifiCFG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ConfigurationActivity.this, NetworkConfigurationActivity.class);
                startActivity(it);
            }
        });
    }

    public void buttonClicked(View item){

        switch (item.getId()){
            case R.id.ivBackCFG:
                //Toast.makeText(getApplicationContext(), "btBoxBack On", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
}
