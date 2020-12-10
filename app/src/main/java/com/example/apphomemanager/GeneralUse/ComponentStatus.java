package com.example.apphomemanager.GeneralUse;

import java.util.ArrayList;

public class ComponentStatus {

    private byte btOnOff;
    private ArrayList<Byte> lout;
    private ArrayList<Byte> pout;

    public ComponentStatus(byte btOnOff, ArrayList<Byte> lout, ArrayList<Byte> pout) {
        this.btOnOff = btOnOff;
        this.lout = lout;
        this.pout = pout;
    }

    public ComponentStatus() {
        this.btOnOff = 0;
        this.lout = new ArrayList<>();
        this.pout = new ArrayList<>();
    }

    public byte getBtOnOff() {
        return btOnOff;
    }

    public void setBtOnOff(byte btOnOff) {
        this.btOnOff = btOnOff;
    }

    public byte getLoutUn(int value) {
        return lout.get(value);
    }

    public void setLout(ArrayList<Byte> lout) {
        this.lout = lout;
    }

    public void setLoutUn(byte lout) {
        this.lout.add(lout);
    }

    public ArrayList<Byte> getPout() {
        return pout;
    }

    public byte getPoutUn(int value) {
        return pout.get(value);
    }

    public void setPout(ArrayList<Byte> pout) {
        this.pout = pout;
    }

    public void setPoutUn(byte pout) {
        this.pout.add(pout);
    }
}
