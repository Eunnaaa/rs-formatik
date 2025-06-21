package model;

public class Pasien {
    private String noPasien;
    private String nama;

    public Pasien(String noPasien, String nama) {
        this.noPasien = noPasien;
        this.nama = nama;
    }

    public String getNoPasien() {
        return noPasien;
    }

    public String getNama() {
        return nama;
    }
}
