package com.omegaauto.shurik.mobilesklad.settings;

public class MobileSkladFontSize {

    String id;
    String descr;

    int sizeName;
    int sizeValue;

    public MobileSkladFontSize() {
    }

    public MobileSkladFontSize(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public int getSizeName() {
        return sizeName;
    }

    public void setSizeName(int sizeName) {
        this.sizeName = sizeName;
    }

    public int getSizeValue() {
        return sizeValue;
    }

    public void setSizeValue(int sizeValue) {
        this.sizeValue = sizeValue;
    }
}
