package com.example.plexa;

public class FilterModel {
    public int resid;
    public String filtername;
    public FilterModel(int resid, String filtername) {
        this.resid = resid;
        this.filtername = filtername;
    }

    public int getResid() {
        return resid;
    }

    public void setResid(int resid) {
        this.resid = resid;
    }

    public String getFiltername() {
        return filtername;
    }

    public void setFiltername(String filtername) {
        this.filtername = filtername;
    }





}
