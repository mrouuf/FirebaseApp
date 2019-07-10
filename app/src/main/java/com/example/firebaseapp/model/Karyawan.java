package com.example.firebaseapp.model;

import java.io.Serializable;

public class Karyawan implements Serializable {
    private String nama_karyawan;
    private String divisi_karyawan;
    private int gaji_karyawan;
    private String key;

    public Karyawan(){}

    public Karyawan(String nama_karyawan, String divisi_karyawan, int gaji_karyawan) {
        this.nama_karyawan = nama_karyawan;
        this.divisi_karyawan = divisi_karyawan;
        this.gaji_karyawan = gaji_karyawan;
        this.key = key;
    }

    @Override
    public String toString() {
        return  " " + nama_karyawan + "\n"+
                " " + divisi_karyawan + "\n" +
                " " + gaji_karyawan;
    }

    public String getNama_karyawan() {
        return nama_karyawan;
    }

    public void setNama_karyawan(String nama_karyawan) {
        this.nama_karyawan = nama_karyawan;
    }

    public String getDivisi_karyawan() {
        return divisi_karyawan;
    }

    public void setDivisi_karyawan(String divisi_karyawan) {
        this.divisi_karyawan = divisi_karyawan;
    }

    public int getGaji_karyawan() {
        return gaji_karyawan;
    }

    public void setGaji_karyawan(int gaji_karyawan) {
        this.gaji_karyawan = gaji_karyawan;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
