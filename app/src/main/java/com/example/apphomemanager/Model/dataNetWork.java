package com.example.apphomemanager.Model;

/**
 * Created by matheus on 13/11/17.
 */

public class dataNetWork {

    private String url;
    private String paramm1;
    private String paramm2;
    private String paramm3;
    private String paramm4;
    private String message;

    public dataNetWork(String url, String paramm1, String paramm2, String paramm3, String paramm4, String message) {
        this.url = url;
        this.paramm1 = paramm1;
        this.paramm2 = paramm2;
        this.paramm3 = paramm3;
        this.paramm4 = paramm4;
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParamm1() {
        return paramm1;
    }

    public void setParamm1(String paramm1) {
        this.paramm1 = paramm1;
    }

    public String getParamm2() {
        return paramm2;
    }

    public void setParamm2(String paramm2) {
        this.paramm2 = paramm2;
    }

    public String getParamm3() {
        return paramm3;
    }

    public void setParamm3(String paramm3) {
        this.paramm3 = paramm3;
    }

    public String getParamm4() {
        return paramm4;
    }

    public void setParamm4(String paramm4) {
        this.paramm4 = paramm4;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return  url + "&&"
                + paramm1 + "&&"
                + paramm2 + "&&"
                + paramm3 + "&&"
                + paramm4 + "&&"
                + message;
    }
}