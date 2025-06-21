package model;

public class Kamar {
    private String nama;
    private double harga;
    private double diskon;
    private String kode;

    public Kamar(String nama, double harga, double diskon) {
        this.nama = nama;
        this.harga = harga;
        this.diskon = diskon;
        this.kode = nama.toLowerCase().replace(" ", "_");
    }

    public String getNama() {
        return nama;
    }

    public String getKode() {
        return kode;
    }

    public double getHarga() {
        return harga;
    }

    public double getDiskon() {
        return diskon;
    }

    @Override
    public String toString() {
        return nama;
    }
}