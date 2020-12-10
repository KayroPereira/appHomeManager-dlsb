package com.example.apphomemanager.Communication;

import android.os.AsyncTask;

import com.example.apphomemanager.Model.dataNetWork;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class HttpService extends AsyncTask<Void, Void, dataNetWork>{
    private OnEventListener<dataNetWork> mCallBack;
    private dataNetWork data;

    public Exception mException;

    public HttpService(dataNetWork data, OnEventListener callback) {
        mCallBack = callback;
        this.data = data;
    }

    @Override
    protected dataNetWork doInBackground(Void... params) {
        StringBuilder resposta = new StringBuilder();

        try {
            URL url = new URL(data.getUrl() + data.getParamm1() + data.getParamm2()
                    + data.getParamm3() + data.getParamm4() + data.getMessage());

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-type", "text/plain");
            connection.setDoOutput(true);
            connection.setConnectTimeout(5000);
            connection.connect();

            Scanner scanner = new Scanner(url.openStream());
            while (scanner.hasNext()) {
                resposta.append(scanner.next());
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            mException = e;
        } catch (IOException e) {
            e.printStackTrace();
            mException = e;
        }

        if (!resposta.toString().equals(""))
            return new dataNetWork("", "", "", "", "", resposta.toString());
        else
            return null;
    }

    @Override
    protected void onPostExecute(dataNetWork result) {
        if (mCallBack != null) {
            if (mException == null) {
                mCallBack.onSuccess(result);
            } else {
                mCallBack.onFailure(mException);
            }
        }
    }
}
