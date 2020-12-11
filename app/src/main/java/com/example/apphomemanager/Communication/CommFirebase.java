package com.example.apphomemanager.Communication;

import android.util.Log;

import com.example.apphomemanager.GeneralUse.ComponentStatus;
import com.example.apphomemanager.GeneralUse.PumpProtection;
import com.example.apphomemanager.GeneralUse.WaterTankData;
import com.example.apphomemanager.listacompras.ConstantsApp;
import com.example.apphomemanager.listacompras.DBProduto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
//import java.util.Arrays;

public class CommFirebase {

    private ConstantsApp constantListaCompras = new ConstantsApp();
    private com.example.apphomemanager.GeneralUse.ConstantsApp constantGeneralUse = new com.example.apphomemanager.GeneralUse.ConstantsApp();

    public ComponentStatus getOutPut(DataSnapshot dataSnapshot, String room){
        ComponentStatus outPuts = new ComponentStatus();

        try {
            outPuts.setBtOnOff(Byte.parseByte(dataSnapshot.child(room).child("lonoff").getValue().toString()));

            for (int i = 0; i < dataSnapshot.child(room).child("l").getChildrenCount()-1; i++) {
                if (outPuts.getBtOnOff() !=0)
                    outPuts.setLoutUn(Byte.parseByte(dataSnapshot.child(room).child("l").child("o" + (i + 1)).getValue().toString()));
                else
                    outPuts.setLoutUn((byte) 0);
            }

            for (int i = 0; i < dataSnapshot.child(room).child("p").getChildrenCount()-1; i++) {
                if (outPuts.getBtOnOff() !=0)
                    outPuts.setPoutUn(Byte.parseByte(dataSnapshot.child(room).child("p").child("o" + (i + 1)).getValue().toString()));
                else
                    outPuts.setPoutUn((byte) 0);
            }
        }catch (Exception e){

        }

        return outPuts;
    }

    public int getComponentStatus(DataSnapshot dataSnapshot, String path, String component){
        int valueComponent = -1;

        try {
            valueComponent = Integer.parseInt(dataSnapshot.child(path).child(component).getValue().toString());
        }catch (Exception e){

        }
        return valueComponent;
    }

    public ArrayList<String> getOutPutOld(DataSnapshot dataSnapshot){
        ArrayList<String> outPuts = new ArrayList<>();

        for (int i = 0; i < dataSnapshot.getChildrenCount(); i++){
            outPuts.add(dataSnapshot.child("out"+(i+1)).child("Valor").getValue().toString());
        }

        return outPuts;
    }

    public WaterTankData getDataWaterTank(DataSnapshot dataSnapshot, String mode){
        WaterTankData data = new WaterTankData();

        //(abx1, abx2, abx30 ou (ci), err, fcp, level, p1s, set{LH, LL}, sp1

        DataSnapshot path = dataSnapshot.child(mode);

        try {
            data.setErr(Integer.parseInt(path.child("err").getValue().toString()));
            data.setFcp(Integer.parseInt(path.child("fcp").getValue().toString()));
            data.setLevel(Integer.parseInt(path.child("level").getValue().toString()));
            data.setLl(Integer.parseInt(path.child("set").child("LL").getValue().toString()));
            data.setLh(Integer.parseInt(path.child("set").child("LH").getValue().toString()));

            switch(mode){
                case "cix1":         //cisterna
                    data.setAddress(new String[]{path.child("abx1").getValue().toString(),
                            path.child("abx2").getValue().toString(),
                            path.child("abx3").getValue().toString()});
                    data.setSx1(Integer.parseInt(path.child("sp1").getValue().toString()));
                    data.setX1s(Integer.parseInt(path.child("p1s").getValue().toString()));
                    break;

                case "cx1":         //caixa
                    data.setAddress(new String[]{path.child("ci").getValue().toString(), "", ""});
                    data.setSx1(Integer.parseInt(path.child("sv1").getValue().toString()));
                    data.setX1s(Integer.parseInt(path.child("v1s").getValue().toString()));
                    break;
            }

        }catch(Exception ex){
            Log.w("Firebase", "Erro no download / conversão dos dados firebase");
        }

        return data;
    }

    public PumpProtection getDataPumpProtection(DataSnapshot dataSnapshot){
        PumpProtection data = new PumpProtection();

        //en, err, flg, sb, st, stm, tdw, tra, trm, vz

//        Log.i("teste", "path: " + constantGeneralUse.getPathRoot() + constantGeneralUse.getPathPumpProtect());

        DataSnapshot path = dataSnapshot.child(constantGeneralUse.getPathRoot() + constantGeneralUse.getPathPumpProtect());

        try {
//            Log.i("teste", "en: " + Integer.parseInt(path.child("en").getValue().toString()));
//            Log.i("teste", "err: " + Integer.parseInt(path.child("err").getValue().toString()));
//            Log.i("teste", "flg: " + Integer.parseInt(path.child("flg").getValue().toString()));
//            Log.i("teste", "sb: " + Integer.parseInt(path.child("sb").getValue().toString()));
//            Log.i("teste", "st: " + Integer.parseInt(path.child("st").getValue().toString()));
//            Log.i("teste", "stm: " + Integer.parseInt(path.child("stm").getValue().toString()));
//            Log.i("teste", "tdw: " + Integer.parseInt(path.child("tdw").getValue().toString()));
//            Log.i("teste", "tra: " + Integer.parseInt(path.child("tra").getValue().toString()));
//            Log.i("teste", "trm: " + Integer.parseInt(path.child("trm").getValue().toString()));
//            Log.i("teste", "vz: " + Integer.parseInt(path.child("vz").getValue().toString()));

            data.setEn(Integer.parseInt(path.child("en").getValue().toString()));
            data.setErr(Integer.parseInt(path.child("err").getValue().toString()));
            data.setFlg(Integer.parseInt(path.child("flg").getValue().toString()));
            data.setSb(Integer.parseInt(path.child("sb").getValue().toString()));
            data.setSt(Integer.parseInt(path.child("st").getValue().toString()));
            data.setStm(Integer.parseInt(path.child("stm").getValue().toString()));
            data.setTdw(Integer.parseInt(path.child("tdw").getValue().toString()));
            data.setTra(Integer.parseInt(path.child("tra").getValue().toString()));
            data.setTrm(Integer.parseInt(path.child("trm").getValue().toString()));
            data.setVz(Integer.parseInt(path.child("vz").getValue().toString()));

        }catch(Exception ex){
            Log.w("Firebase", "Erro no download / conversão dos dados firebase");
//            Log.i("teste", "Erro no download / conversão dos dados firebase");
        }
        return data;
    }

    public String getItem(DataSnapshot dataSnapshot, String mode) {

        String item;
        try {
            DataSnapshot path = dataSnapshot.child(mode);

            item = path.getValue().toString();
        }catch (Exception e){
            return "";
        }
        return item;
    }

    public ArrayList<DBProduto> getListaCompras(DataSnapshot dataSnapshot, String path, int mode) {

        ArrayList<DBProduto> list = new ArrayList<>();

        try {
            DataSnapshot dataList = dataSnapshot.child(path);
            for (DataSnapshot temp : dataList.getChildren()) {
                for (DataSnapshot temp1 : temp.getChildren()) {
                    if (mode == constantListaCompras.getFlgDsp())
                        list.add(new DBProduto(-1, Integer.parseInt(temp.getKey()), temp1.getKey(), (float) 1.0, Integer.parseInt(temp1.getValue().toString()), constantListaCompras.getStatusOn()));
                    else{
                        String value[] = temp1.getValue().toString().split("#");
                        list.add(new DBProduto(-1, Integer.parseInt(temp.getKey()), temp1.getKey(), Float.parseFloat(value[0]), Integer.parseInt(value[1]), Integer.parseInt(value[2])));
                    }
                }
            }
        }catch (Exception e){
            return null;
        }
        return list;
    }

    public int isPathEmpt(DataSnapshot dataSnapshot, String path) {

        int pathEmpt = -1;

        try {
            DataSnapshot dataPath = dataSnapshot.child(path);
            pathEmpt = (int) dataPath.getChildrenCount();

            if (pathEmpt > 1)
                return pathEmpt;

            return 0;
        }catch (Exception e){
            return -1;
        }
    }

    public void sendDataInt(DatabaseReference reference, String path, int value){
        reference.child(path).setValue(value);
    }

    public void sendDataString(DatabaseReference reference, String path, String value){
        reference.child(path).setValue(value);
    }

    public void deleteItem(DatabaseReference reference, String path){
        reference.child(path).removeValue();
    }
}
