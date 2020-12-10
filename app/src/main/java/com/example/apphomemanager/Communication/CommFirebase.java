package com.example.apphomemanager.Communication;

import android.util.Log;

import androidx.core.util.LogWriter;

import com.example.apphomemanager.GeneralUse.ComponentStatus;
import com.example.apphomemanager.GeneralUse.WaterTankData;
import com.example.apphomemanager.listacompras.ConstantsApp;
import com.example.apphomemanager.listacompras.DBProduto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
//import java.util.Arrays;

public class CommFirebase {

    private ConstantsApp constant = new ConstantsApp();

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
                    if (mode == constant.getFlgDsp())
                        list.add(new DBProduto(-1, Integer.parseInt(temp.getKey()), temp1.getKey(), (float) 1.0, Integer.parseInt(temp1.getValue().toString()), constant.getStatusOn()));
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
