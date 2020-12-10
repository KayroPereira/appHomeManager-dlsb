package com.example.apphomemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apphomemanager.Communication.HttpService;
import com.example.apphomemanager.Communication.OnEventListener;
import com.example.apphomemanager.GeneralUse.ConstantsApp;
import com.example.apphomemanager.Model.dataNetWork;

import java.util.Timer;
import java.util.TimerTask;

public class doorLockActivity extends AppCompatActivity {

    final int PROGRESS_BAR_CONTROLE    = 1;
    final int SEND_CONTROLE    = 2;
    final int ALL_CONTROLE    = 3;

    final long DELAY_START = 0;
    final long DELAY_PERIOD = 1000;

    private TextView tvSSIDDoor;

    private ProgressBar pBarDoor;

    //private Button btEnviarDoor;

    private ImageView ivSend;

    private Context context;

    private Timer timer;

    private String URL_BOARD;

    @Override
    protected void onResume() {
        super.onResume();

        if (timer == null){
            timer = new Timer();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (timer != null){
            timer.cancel();
            timer = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_door_lock);

        context = this;

        URL_BOARD = new ConstantsApp().getURL_BOARD();

        pBarDoor = (ProgressBar) findViewById(R.id.pBarDoor);
        //btEnviarDoor = (Button) findViewById(R.id.btEnviarDoor);

        tvSSIDDoor = (TextView) findViewById(R.id.tvSSIDDoor);

        ivSend = (ImageView) findViewById(R.id.ivSendDLK);

        componentControl(true, ALL_CONTROLE);

        if (timer == null){
            timer = new Timer();
        }

        Timer();

        /*
        btEnviarDoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardRecord();
            }
        });
        */
    }

    private void cardRecord(){
        componentControl(false, ALL_CONTROLE);
        HttpService httpService = new HttpService(new dataNetWork(URL_BOARD,"cardRecord", "",
                "", "", ""), new OnEventListener<dataNetWork>() {
            @Override
            public void onSuccess(dataNetWork object) {
                try {
                    Toast.makeText(getApplicationContext(), R.string.networkOk, Toast.LENGTH_LONG).show();

                    //Toast.makeText(getApplicationContext(), R.string.networkError, Toast.LENGTH_LONG).show();
                }catch (Exception e){

                }
                componentControl(true, ALL_CONTROLE);
            }

            @Override
            public void onFailure(Exception e) {
                componentControl(true, ALL_CONTROLE);
                Toast.makeText(getApplicationContext(), R.string.networkError, Toast.LENGTH_LONG).show();
            }
        });
        httpService.execute();
    }

    public void buttonClicked(View item){
        switch (item.getId()){
            case R.id.ivBackDLK:
                //Toast.makeText(getApplicationContext(), "btBoxBack On", Toast.LENGTH_SHORT).show();
                finish();
                break;

            case R.id.ivSendDLK:
                //Toast.makeText(getApplicationContext(), "btBoxBack On", Toast.LENGTH_SHORT).show();
                //finish();
                cardRecord();
                break;
        }
    }


    private void componentControl (boolean status, int mode){

        switch(mode){
            case PROGRESS_BAR_CONTROLE:
                pBarDoor.setVisibility(status ? View.VISIBLE : View.INVISIBLE);
                break;

            case SEND_CONTROLE:
                //btEnviarDoor.setEnabled(status);
                ivSend.setVisibility(status ? View.VISIBLE : View.INVISIBLE);
                break;

            case ALL_CONTROLE:
                componentControl(!status, PROGRESS_BAR_CONTROLE);
                componentControl(status, SEND_CONTROLE);
                break;
        }
    }

    private String getConnection(){
        String connection = getString(R.string.unidentified);

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo;

        wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
            connection = wifiInfo.getSSID();
        }

        if (!connection.equals("<unknown ssid>"))
            return connection.replaceAll("\"", "");
        else
            return getString(R.string.unidentified);
    }

    public void Timer(){
        doorLockActivity.Task task = new doorLockActivity.Task();

        timer.schedule(task, DELAY_START, DELAY_PERIOD);
    }

    class Task extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String network = getConnection();

                    if (!network.equals(tvSSIDDoor.getText().toString().replace(getString(R.string.ssid), "").trim())) {
                        if (network.equals(getString(R.string.networkName)))
                            tvSSIDDoor.setTextColor(Color.BLUE);
                        else
                            tvSSIDDoor.setTextColor(Color.RED);

                        //tvSSIDDoor.setText(getString(R.string.ssid) + " " + getConnection());
                        tvSSIDDoor.setText(getString(R.string.ssid) + " " + network);

                        if (!network.equals(getString(R.string.networkName))) {
                            componentControl(false, SEND_CONTROLE);
                            new AlertDialog.Builder(context).setTitle(getString(R.string.conexao)).setMessage(getString(R.string.msg1) + " " + getString(R.string.networkName) + " " + getString(R.string.msg2)).show();
                        }else
                            componentControl(true, SEND_CONTROLE);
                    }
                }
            });
        }
    }
}