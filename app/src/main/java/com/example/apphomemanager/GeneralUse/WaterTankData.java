package com.example.apphomemanager.GeneralUse;

public class WaterTankData {

    private String address[] = new String[3];
    private int err;
    private int fcp;
    private int level;
    private int x1s;
    private int sx1;
    private int ll;
    private int lh;

    public WaterTankData(){}

    public WaterTankData(String[] address, int err, int fcp, int level, int x1s, int sx1, int ll, int lh) {
        this.address = address;
        this.err = err;
        this.fcp = fcp;
        this.level = level;
        this.x1s = x1s;
        this.sx1 = sx1;
        this.ll = ll;
        this.lh = lh;
    }

    public String[] getAddress() {
        return address;
    }

    public void setAddress(String[] address) {
        this.address = address;
    }

    public int getErr() {
        return err;
    }

    public void setErr(int err) {
        this.err = err;
    }

    public int getFcp() {
        return fcp;
    }

    public void setFcp(int fcp) {
        this.fcp = fcp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getX1s() {
        return x1s;
    }

    public void setX1s(int x1s) {
        this.x1s = x1s;
    }

    public int getSx1() {
        return sx1;
    }

    public void setSx1(int sx1) {
        this.sx1 = sx1;
    }

    public int getLl() {
        return ll;
    }

    public void setLl(int ll) {
        this.ll = ll;
    }

    public int getLh() {
        return lh;
    }

    public void setLh(int lh) {
        this.lh = lh;
    }
}
