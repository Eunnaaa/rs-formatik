package model;

public class Dokter {
    private String nama;
    private String spesialis;
    private String jamMulai;
    private String jamSelesai;

    public Dokter(String nama, String spesialis, String jamMulai, String jamSelesai) {
        this.nama = nama;
        this.spesialis = spesialis;
        this.jamMulai = jamMulai;
        this.jamSelesai = jamSelesai;
    }

    public String getNama() {
        return nama;
    }

    public String getSpesialis() {
        return spesialis;
    }

    public String getJadwal() {
        return jamMulai + " - " + jamSelesai;
    }

    @Override
    public String toString() {
        return nama;
    }
}