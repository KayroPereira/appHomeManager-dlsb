package com.example.apphomemanager.GeneralUse;

public class PumpProtection {

    private int en;
    private int err;
    private int flg;
    private int sb;
    private int st;
    private int stm;
    private int tdw;
    private int tra;
    private int trm;
    private int vz;

    public PumpProtection(){

    }

    public PumpProtection(int en, int err, int flg, int sb, int st, int stm, int tdw, int tra, int trm, int vz) {
        this.en = en;
        this.err = err;
        this.flg = flg;
        this.sb = sb;
        this.st = st;
        this.stm = stm;
        this.tdw = tdw;
        this.tra = tra;
        this.trm = trm;
        this.vz = vz;
    }

    public int getEn() {
        return en;
    }

    public void setEn(int en) {
        this.en = en;
    }

    public int getErr() {
        return err;
    }

    public void setErr(int err) {
        this.err = err;
    }

    public int getFlg() {
        return flg;
    }

    public void setFlg(int flg) {
        this.flg = flg;
    }

    public int getSb() {
        return sb;
    }

    public void setSb(int sb) {
        this.sb = sb;
    }

    public int getSt() {
        return st;
    }

    public void setSt(int st) {
        this.st = st;
    }

    public int getStm() {
        return stm;
    }

    public void setStm(int stm) {
        this.stm = stm;
    }

    public int getTdw() {
        return tdw;
    }

    public void setTdw(int tdw) {
        this.tdw = tdw;
    }

    public int getTra() {
        return tra;
    }

    public void setTra(int tra) {
        this.tra = tra;
    }

    public int getTrm() {
        return trm;
    }

    public void setTrm(int trm) {
        this.trm = trm;
    }

    public int getVz() {
        return vz;
    }

    public void setVz(int vz) {
        this.vz = vz;
    }

    @Override
    public String toString() {
        return "PumpProtection{" +
                "en=" + en +
                ", err=" + err +
                ", flg=" + flg +
                ", sb=" + sb +
                ", st=" + st +
                ", stm=" + stm +
                ", tdw=" + tdw +
                ", tra=" + tra +
                ", trm=" + trm +
                ", vz=" + vz +
                '}';
    }
}