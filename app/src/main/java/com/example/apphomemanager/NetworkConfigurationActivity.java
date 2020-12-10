package com.example.apphomemanager;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apphomemanager.Communication.HttpService;
import com.example.apphomemanager.Communication.OnEventListener;
import com.example.apphomemanager.GeneralUse.ConstantsApp;
import com.example.apphomemanager.Model.dataNetWork;

import java.util.Timer;
import java.util.TimerTask;

public class NetworkConfigurationActivity extends AppCompatActivity {

    final int EDIT_TEXT_CONTROLE    = 0;
    final int PROGRESS_BAR_CONTROLE    = 1;
    final int SEND_CONTROLE    = 2;
    final int ALL_CONTROLE    = 3;

    final int LENGTH_SSID    = 35;
    final int LENGTH_PASSWORD    = 35;

    final long DELAY_START = 0;
    final long DELAY_PERIOD = 1000;

    private EditText eTSSID;
    private EditText eTPassword;

    private TextView tvSSID;

    private ProgressBar pBar;

    private ImageView ivSendNCG;

    //private Button btEnviar;

    private Context context;

    private Timer timer;

    private String URL_BOARD;

    //private static String URL_BOARD = "http://172.42.1.42/";

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
        setContentView(R.layout.activity_networkconfiguration);

        context = this;

        URL_BOARD = new ConstantsApp().getURL_BOARD();

        eTSSID = (EditText) findViewById(R.id.eTSSID);
        eTPassword = (EditText) findViewById(R.id.eTPassword);

        pBar = (ProgressBar) findViewById(R.id.pBar);
        //btEnviar = (Button) findViewById(R.id.btEnviar);

        tvSSID = (TextView) findViewById(R.id.tvSSID);

        ivSendNCG = (ImageView) findViewById(R.id.ivSendNCG);

        componentControl(true, ALL_CONTROLE);

        if (timer == null){
            timer = new Timer();
        }

        Timer();

        /*
        btEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
         */
    }

    private void registerUser(){

        if (TextUtils.isEmpty(eTSSID.getText().toString())) {
            Toast.makeText(getApplicationContext(), R.string.fvssid, Toast.LENGTH_LONG).show();
            return;
        }

        if (eTSSID.getText().toString().length() > LENGTH_SSID){
            Toast.makeText(getApplicationContext(), R.string.ssidErrorLength, Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(eTPassword.getText().toString())) {
            Toast.makeText(getApplicationContext(), R.string.fvpassnetword, Toast.LENGTH_LONG).show();
            return;
        }

        if (eTPassword.getText().toString().length() > LENGTH_PASSWORD){
            Toast.makeText(getApplicationContext(), R.string.passwordErrorLength, Toast.LENGTH_LONG).show();
            return;
        }

        componentControl(false, ALL_CONTROLE);
        HttpService httpService = new HttpService(new dataNetWork(URL_BOARD, "configWifi?ssid=", eTSSID.getText().toString(),
                "&password=", eTPassword.getText().toString(), ""), new OnEventListener<dataNetWork>() {

            @Override
            public void onSuccess(dataNetWork object) {
                try {
                    dataNetWork data = filterDate(object.getMessage());
                    if (eTSSID.getText().toString().equals(data.getParamm2()) && eTPassword.getText().toString().equals(data.getParamm4())) {
                        clearComponent();
                        Toast.makeText(getApplicationContext(), R.string.networkOk, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.networkError, Toast.LENGTH_LONG).show();
                    }
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
            case R.id.ivBackNCG:
                //Toast.makeText(getApplicationContext(), "btBoxBack On", Toast.LENGTH_SHORT).show();
                finish();
                break;

            case R.id.ivSendNCG:
                //Toast.makeText(getApplicationContext(), "btBoxBack On", Toast.LENGTH_SHORT).show();
                //finish();
                hideSoftKeyboard();
                registerUser();
                break;
        }
    }

    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    void clearComponent(){
        eTSSID.setText("");
        eTPassword.setText("");
        eTSSID.requestFocus();
    }

    private void componentControl (boolean status, int mode){

        switch(mode){
            case EDIT_TEXT_CONTROLE:
                eTSSID.setEnabled(status);
                eTPassword.setEnabled(status);
                break;

            case PROGRESS_BAR_CONTROLE:
                pBar.setVisibility(status ? View.VISIBLE : View.INVISIBLE);
                break;

            case SEND_CONTROLE:
                //btEnviar.setEnabled(status);
                ivSendNCG.setVisibility(status ? View.VISIBLE : View.INVISIBLE);
                break;

            case ALL_CONTROLE:
                componentControl(status, EDIT_TEXT_CONTROLE);
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

    public dataNetWork filterDate(String data){
        final String FILTER_SSID = "SSID:";
        final String FILTER_PASSWORD = "Password:";

        if (!data.equals("")) {
            return new dataNetWork("", "", data.substring(data.indexOf(FILTER_SSID) + FILTER_SSID.length(), data.indexOf(FILTER_PASSWORD)),
                    "", data.substring(data.indexOf(FILTER_PASSWORD) + FILTER_PASSWORD.length()), "");
        }
        return new dataNetWork("","","","", "", "");
    }

    public void Timer(){
        Task task = new Task();

        timer.schedule(task, DELAY_START, DELAY_PERIOD);
    }

    class Task extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String network = getConnection();

                    if (!network.equals(tvSSID.getText().toString().replace(getString(R.string.ssid), "").trim())) {
                        if (network.equals(getString(R.string.networkName)))
                            tvSSID.setTextColor(Color.BLUE);
                        else
                            tvSSID.setTextColor(Color.RED);

                        //tvSSID.setText(getString(R.string.ssid) + " " + getConnection());
                        tvSSID.setText(getString(R.string.ssid) + " " + network);

                        if (!network.equals(getString(R.string.networkName))) {
                            componentControl(false, SEND_CONTROLE);
                            //new AlertDialog.Builder(context).setTitle("Conexão").setMessage("Favor conectar na rede " + getString(R.string.networkName) + " para configurar o dispositivo").show();
                            new AlertDialog.Builder(context).setTitle(getString(R.string.conexao)).setMessage(getString(R.string.msg1) + " " + getString(R.string.networkName) + " " + getString(R.string.msg2)).show();
                        }else
                            componentControl(true, SEND_CONTROLE);
                    }
                }
            });
        }
    }
}