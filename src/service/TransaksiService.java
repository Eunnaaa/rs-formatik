package service;

import model.Kamar;

public class TransaksiService {
    private static final double BIAYA_ADMINISTRASI = 10000;
    private static final double BIAYA_KONSULTASI = 50000;
    private static final double BIAYA_OBAT = 30000;

    public static double hitungTotal(boolean bayarAdministrasi, boolean bayarKonsultasi, boolean bayarObat,
                                     int lamaInap, Kamar kamar) {
        double total = 0;

        if (bayarAdministrasi) {
            total += BIAYA_ADMINISTRASI;
        }

        if (bayarKonsultasi) {
            total += BIAYA_KONSULTASI;
        }

        if (bayarObat) {
            total += BIAYA_OBAT;
        }

        if (lamaInap > 0 && kamar != null) {
            double subtotalInap = kamar.getHarga() * lamaInap;
            double diskon = subtotalInap * kamar.getDiskon();
            total += subtotalInap - diskon;
        }

        return total;
    }

    public static double getBiayaAdministrasi() {
        return BIAYA_ADMINISTRASI;
    }

    public static double getBiayaKonsultasi() {
        return BIAYA_KONSULTASI;
    }

    public static double getBiayaObat() {
        return BIAYA_OBAT;
    }
}