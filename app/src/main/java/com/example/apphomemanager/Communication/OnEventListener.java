package com.example.apphomemanager.Communication;

import com.example.apphomemanager.Model.dataNetWork;

public interface OnEventListener<object> {
    public void onSuccess(dataNetWork object);
    public void onFailure(Exception e);
}
