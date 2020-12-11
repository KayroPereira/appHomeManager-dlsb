package com.example.apphomemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.apphomemanager.listacompras.ConstantsApp;
import com.google.android.gms.auth.api.accounttransfer.zzu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

public class CadastroActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private ConstantsApp constants = new ConstantsApp();

    private EditText eTLogin;
    private EditText eTPassword;
    private EditText geteTPasswordConfirm;

    private ProgressBar pBar;

    private ImageView ivSend;

    //private Button btCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        mAuth = FirebaseAuth.getInstance();

        eTLogin = (EditText) findViewById(R.id.eTSSIDCDT);
        eTPassword = (EditText) findViewById(R.id.eTPasswordCDT);
        geteTPasswordConfirm = (EditText) findViewById(R.id.eTPasswordConfirmCDT);

        pBar = (ProgressBar) findViewById(R.id.pBarCDT);

        ivSend = (ImageView) findViewById(R.id.ivSendCDT);

        //btCadastrar = (Button) findViewById(R.id.btEnviarDoor);

        /*
        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

         */
        componentControl(true);
    }

    private void componentControl(boolean status){
        pBar.setVisibility(status ? View.INVISIBLE : View.VISIBLE);
        //btCadastrar.setEnabled(status);
        ivSend.setVisibility(status ? View.VISIBLE : View.INVISIBLE);
    }

    private void registerUser(){
        hideSoftKeyboard();
        componentControl(false);

        if (TextUtils.isEmpty(eTLogin.getText().toString())) {
            Toast.makeText(getApplicationContext(), R.string.fvEmail, Toast.LENGTH_LONG).show();
            componentControl(true);
            return;
        }

//        Toast.makeText(getApplicationContext(), "Len pass: " + eTPassword.getText().toString().length() + " len def: " +  constants.getLEN_PASSWORD_EMAIL()
//                + " codicao: " + (eTPassword.getText().toString().length() < constants.getLEN_PASSWORD_EMAIL()), Toast.LENGTH_LONG).show();

        if (TextUtils.isEmpty(eTPassword.getText().toString())) {
            Toast.makeText(getApplicationContext(), R.string.fvSenha, Toast.LENGTH_LONG).show();
            componentControl(true);
            return;
        }else if(eTPassword.getText().toString().length() < constants.getLEN_PASSWORD_EMAIL()){
            Toast.makeText(getApplicationContext(), R.string.msgLenPassword, Toast.LENGTH_SHORT).show();
            componentControl(true);
            return;
        }

        if (TextUtils.isEmpty(geteTPasswordConfirm.getText().toString())) {
            Toast.makeText(getApplicationContext(), R.string.fvcSenha, Toast.LENGTH_LONG).show();
            componentControl(true);
            return;
        }

        if (!eTPassword.getText().toString().equals(geteTPasswordConfirm.getText().toString())){
            Toast.makeText(getApplicationContext(), R.string.senhaError, Toast.LENGTH_SHORT).show();

            componentControl(true);
            return;
        }

        //Toast.makeText(getApplicationContext(), "User: " + eTLogin.getText().toString() + " Senha: " +  eTPassword.getText().toString(), Toast.LENGTH_LONG).show();

        mAuth.createUserWithEmailAndPassword(eTLogin.getText().toString(), eTPassword.getText().toString())
        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                //((FirebaseAuthInvalidCredentialsException) ((zzu) task).zzab).getErrorCode()

//                Toast.makeText(getApplicationContext(), "Erro: " + task.getException(), Toast.LENGTH_LONG).show();



                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), R.string.registroOk, Toast.LENGTH_LONG).show();
                    //progressBar.setVisibility(View.GONE);

                    Intent intent = new Intent(CadastroActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), R.string.registroError, Toast.LENGTH_LONG).show();
                    //progressBar.setVisibility(View.GONE);
                }
            }
        });

        componentControl(true);
        Toast.makeText(getApplicationContext(), R.string.tryRegister, Toast.LENGTH_LONG).show();
    }

    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void buttonClicked(View item){
        switch (item.getId()){
            case R.id.ivBackCDT:
                //Toast.makeText(getApplicationContext(), "btBoxBack On", Toast.LENGTH_SHORT).show();
                finish();
                break;

            case R.id.ivSendCDT:
                //Toast.makeText(getApplicationContext(), "btBoxBack On", Toast.LENGTH_SHORT).show();
                //finish();
                registerUser();
                break;
        }
    }
}
