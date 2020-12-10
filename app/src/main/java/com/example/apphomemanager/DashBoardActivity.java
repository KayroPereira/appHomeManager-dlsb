package com.example.apphomemanager;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import com.example.apphomemanager.Communication.CommFirebase;
import com.example.apphomemanager.GeneralUse.ConstantsApp;
import com.example.apphomemanager.Localizacao.Clima;
import com.example.apphomemanager.Localizacao.DownloadJsonAsyncTask;
import com.example.apphomemanager.Localizacao.Localizacao;
import com.example.apphomemanager.listacompras.BuyListActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DashBoardActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();

    final DatabaseReference dbOutStatus = reference;

    private AlertDialog alerta;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private ConstraintLayout constraintLayout10;

    private ImageView ivDoorLockDB;
    private ImageView ivControlDB;
    private ImageView ivReservoirDB;
    private ImageView ivBuyListDB;
    private ImageView ivSetupDB;
    private ImageView ivRefreshDB;
    private ImageView ivClima;
    private ImageView ivTemp;
    private ImageView ivClima1;
    private ImageView ivClima2;
    private ImageView ivClima3;
    private ImageView ivBuyListInfDB;

    private TextView tvCidade;
    private TextView tvTempUmid;
    private TextView tvTempH_L;
    private TextView tvDay1;
    private TextView tvTempH_L1;
    private TextView tvDay2;
    private TextView tvTempH_L2;
    private TextView tvDay3;
    private TextView tvTempH_L3;
    private TextView tvBuyListInfDB;

    private boolean requestingLocationUpdates = false;
    private Localizacao localizacao = new Localizacao();
    private Clima clima = new Clima();

    final int REQUEST_CHECK_SETTINGS = 10;

    private static boolean startAplication = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        constraintLayout10 = (ConstraintLayout) findViewById(R.id.constraintLayout10);

        ivDoorLockDB = (ImageView) findViewById(R.id.ivDoorLockDB);
        ivControlDB = (ImageView) findViewById(R.id.ivControlDB);
        ivReservoirDB = (ImageView) findViewById(R.id.ivReservoirDB);
        ivSetupDB = (ImageView) findViewById(R.id.ivSetupDB);
        ivRefreshDB = (ImageView) findViewById(R.id.ivRefreshDB);
        ivClima = (ImageView) findViewById(R.id.ivClima);
        ivTemp = (ImageView) findViewById(R.id.ivTemp);
        ivClima1 = (ImageView) findViewById(R.id.ivClima1);
        ivClima2 = (ImageView) findViewById(R.id.ivClima2);
        ivClima3 = (ImageView) findViewById(R.id.ivClima3);
        ivBuyListDB = (ImageView) findViewById(R.id.ivBuyListDB);
        ivBuyListInfDB = (ImageView) findViewById(R.id.ivBuyListInfDB);

        tvCidade = (TextView) findViewById(R.id.tvCidade);
        tvTempUmid = (TextView) findViewById(R.id.tvTempUmid);
        tvTempH_L = (TextView) findViewById(R.id.tvTempH_L);
        tvDay1 = (TextView) findViewById(R.id.tvDay1);
        tvTempH_L1 = (TextView) findViewById(R.id.tvTempH_L1);
        tvDay2 = (TextView) findViewById(R.id.tvDay2);
        tvTempH_L2 = (TextView) findViewById(R.id.tvTempH_L2);
        tvDay3 = (TextView) findViewById(R.id.tvDay3);
        tvTempH_L3 = (TextView) findViewById(R.id.tvTempH_L3);
        tvBuyListInfDB = (TextView) findViewById(R.id.tvBuyListInfDB);

        hideBuyList(false);

        setStatusComponentes(false);

        iniciarLocalizacao();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        locationCallback = new LocationCallback() {
            @Override
            //chamado quando a conexão está ativada e se tem um resultado
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Toast.makeText(getApplicationContext(), "Erro: Localização nula", Toast.LENGTH_LONG).show();
                    return;
                }

                for (Location location : locationResult.getLocations()) {
                    updateData(location);
                }
            }

            //Chamado quando há uma alteração na disponibilidade dos dados do local.
            @Override
            public void onLocationAvailability(LocationAvailability avail) {
                super.onLocationAvailability(avail);

                //testa se a localização esta habilitada
                if (!avail.isLocationAvailable()) {
                    Toast.makeText(getApplicationContext(), "Habilite a localização para utilizar todas as funcionalidades", Toast.LENGTH_LONG).show();
                }
            }
        };

        ivControlDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(DashBoardActivity.this, DashBoardControlActivity.class);
                startActivity(it);
            }
        });

        ivSetupDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(DashBoardActivity.this, ConfigurationActivity.class);
                startActivity(it);
            }
        });

        ivReservoirDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(DashBoardActivity.this, DashBoardReservoirActivity.class);
                startActivity(it);
            }
        });

        ivBuyListDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(DashBoardActivity.this, BuyListActivity.class);
                startActivity(it);
            }
        });

        ivDoorLockDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DashBoardActivity.this);
                builder.setTitle("Abrir porta de entrada");
                builder.setMessage("Deseja abrir a porta de entrada?");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        try {
                            //ivDoorLockDB.setImageResource(R.drawable.btlight_on1);
                            dbOutStatus.child("door").child("d1").setValue(1);
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Não foi possível obter os dados", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(DashBoardActivity.this, "Solicitação cancelada", Toast.LENGTH_SHORT).show();
                    }
                });
                alerta = builder.create();
                alerta.show();
            }
        });

        dbOutStatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //get status component ivDoorLok in firebase
                ivDoorLockDB.setImageResource(new CommFirebase().getComponentStatus(dataSnapshot, "door", "d1") == 0 ? R.drawable.door : R.drawable.dooron1);

                if (new CommFirebase().isPathEmpt(dataSnapshot, new com.example.apphomemanager.listacompras.ConstantsApp().getPathMinhaLista()) > 0)
                    hideBuyList(true);
                else
                    hideBuyList(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void hideBuyList(boolean value) {
        if (value) {
            ivBuyListInfDB.setVisibility(View.VISIBLE);
            tvBuyListInfDB.setVisibility(View.VISIBLE);
        } else {
            ivBuyListInfDB.setVisibility(View.INVISIBLE);
            tvBuyListInfDB.setVisibility(View.INVISIBLE);
        }

    }

    void iniciarLocalizacao() {
        pedirPermissoes();
        createLocationRequest();
    }

    public void buttonClickedDB(View item) {
        if (requestingLocationUpdates) {
            onPause();
        } else {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    protected void onResume() {
        //Toast.makeText(getApplicationContext(), "teste5", Toast.LENGTH_LONG).show();
        super.onResume();

        if (!startAplication) {
            startAplication = true;
            startLocationUpdates();
        }

        //------------- implementar a chamada para localização
        /*
        if (!requestingLocationUpdates) {
            startLocationUpdates();
        }

         */
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        requestingLocationUpdates = true;
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
        requestingLocationUpdates = false;
    }

    protected void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000);
        //locationRequest.setFastestInterval(15000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); //verificar a possibilidade de conexão por outras redes
        //locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);        //funcionado
    }

    private void updateData(Location location){
        if (location == null) {
            Toast.makeText(getApplicationContext(), "Erro: Localização nula", Toast.LENGTH_LONG).show();
            return;
        }

        localizacao.setLatitude(location.getLatitude());
        localizacao.setLongitude(location.getLongitude());
        localizacao.setProvider(location.getProvider());
        localizacao.setTime(location.getTime());
        localizacao.setAccuracy(location.getAccuracy());
        localizacao.setAltitude(location.getAltitude());
        localizacao.setSpeed(location.getSpeed());

        stopLocationUpdates();
        updateClima(localizacao);
    }

    private void updateClima(Localizacao localizacao){
        //realiza a busca das informações do clima na cidade de Jaboatão dos Guararapes - PE
        DownloadJsonAsyncTask downloadJson = new DownloadJsonAsyncTask(new DownloadJsonAsyncTask.AsyncResponseJson() {
            ProgressDialog dialog;
            @Override
            public void processFinish(Clima result) {
                if (result != null ) {
                    String temp = "";

                    setStatusComponentes(true);

                    clima.setCityName(result.getCityName());
                    clima.setDate(result.getDate());
                    clima.setTemp(result.getTemp());
                    clima.setHumidity(result.getHumidity());
                    clima.setConditionSlug(result.getConditionSlug());
                    clima.setClimaDia(result.getClimaDia());

                    tvCidade.setText(clima.getCityName());
                    tvTempUmid.setText(clima.getTemp() + "°C / " + clima.getHumidity() + "%");
                    tvTempH_L.setText(clima.getClimaDia().get(0).getMin() + "°C / " + clima.getClimaDia().get(0).getMax() + "°C");

                    tvDay1.setText(clima.getClimaDia().get(1).getWeekday());
                    tvDay2.setText(clima.getClimaDia().get(2).getWeekday());
                    tvDay3.setText(clima.getClimaDia().get(3).getWeekday());

                    tvTempH_L1.setText(clima.getClimaDia().get(1).getMin() + "°C / " + clima.getClimaDia().get(1).getMax() + "°C");
                    tvTempH_L2.setText(clima.getClimaDia().get(2).getMin() + "°C / " + clima.getClimaDia().get(2).getMax() + "°C");
                    tvTempH_L3.setText(clima.getClimaDia().get(3).getMin() + "°C / " + clima.getClimaDia().get(3).getMax() + "°C");

                    String imageTemp[] = new ConstantsApp().getClimaImages();

                    ivClima.setImageResource(DashBoardActivity.this.getResources().getIdentifier(imageTemp[getClimaStatusValue(clima.getConditionSlug())], "drawable", DashBoardActivity.this.getPackageName()));
                    ivClima1.setImageResource(DashBoardActivity.this.getResources().getIdentifier(imageTemp[getClimaStatusValue(clima.getClimaDia().get(1).getCondition())], "drawable", DashBoardActivity.this.getPackageName()));
                    ivClima2.setImageResource(DashBoardActivity.this.getResources().getIdentifier(imageTemp[getClimaStatusValue(clima.getClimaDia().get(2).getCondition())], "drawable", DashBoardActivity.this.getPackageName()));
                    ivClima3.setImageResource(DashBoardActivity.this.getResources().getIdentifier(imageTemp[getClimaStatusValue(clima.getClimaDia().get(3).getCondition())], "drawable", DashBoardActivity.this.getPackageName()));

                    int temperature = 0;
                    try {
                        temperature = Integer.parseInt(clima.getTemp());
                    }catch (Exception e){

                    }

                    ivTemp.setImageResource(DashBoardActivity.this.getResources()
                            .getIdentifier(new ConstantsApp().getTemperaturaImages()[temperature < 15 ? 0 : temperature > 25 ? 2 : 1], "drawable", DashBoardActivity.this.getPackageName()));
                }else{
                    Toast.makeText(DashBoardActivity.this, "Erro ao obter os dados do clima", Toast.LENGTH_SHORT).show();
                }
                //dialog.dismiss();
                //interrompe as atualizações
                stopLocationUpdates();
            }

            @Override
            public void processStart() {
                //dialog = ProgressDialog.show(MainActivity.this, "Aguarde", "Fazendo download do JSON");
            }
        });
        downloadJson.execute("https://api.hgbrasil.com/weather?array_limit=4&fields=only_results,humidity,temp,city_name,forecast,condition_slug,condition,weekday,max,min,date&lat=" +
                localizacao.getLatitude() + "&log=" + localizacao.getLongitude() + "&user_ip=remote&key=bdda2060");
    }

    private void setStatusComponentes(boolean status){
        if (status)
            constraintLayout10.setVisibility(View.VISIBLE);
        else
            constraintLayout10.setVisibility(View.INVISIBLE);
    }

    private int getClimaStatusValue(String climaStatus){
        int imageValue = 0;
        String climaStatusOptions[] = new ConstantsApp().getClimaStatus();


        for (String temp : climaStatusOptions){
            if (temp.equals(climaStatus))
                return imageValue;

            imageValue++;
        }
        return climaStatusOptions.length-2;
    }

    private void pedirPermissoes() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CHECK_SETTINGS);
            }
        }
    }
}
