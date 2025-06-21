package ui;

import model.Pasien;
import model.Kamar;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Locale;
import java.text.NumberFormat;
import java.awt.Color;

public class FormBayar extends JFrame {
    private JTextField tfTotal, tfBayar, tfKembalian;
    private JTextArea taRincian;
    private int totalBayar;
    private List<Pasien> dataPasien;
    private FormTransaksi formTransaksi;

    public FormBayar(int totalBayar, List<Pasien> dataPasien, FormTransaksi formTransaksi) {
        this.totalBayar = totalBayar;
        this.dataPasien = dataPasien;
        this.formTransaksi = formTransaksi;

        setTitle("Halaman Pembayaran -  RS Formatik");
        setSize(600, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {
        NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

        BackgroundPanel backgroundPanel = new BackgroundPanel("assets/background.png");
        backgroundPanel.setLayout(new GridBagLayout());

        JLabel lblTotal = new JLabel("Total Tagihan:");
        JLabel lblBayar = new JLabel("Uang Bayar:");
        JLabel lblKembalian = new JLabel("Kembalian:");
        JLabel lblRincian = new JLabel("Rincian Transaksi:");

        tfTotal = new JTextField(rupiahFormat.format(totalBayar));
        tfTotal.setEditable(false);

        tfBayar = new JTextField();
        tfKembalian = new JTextField();
        tfKembalian.setEditable(false);

        taRincian = new JTextArea(getRincianTransaksi(), 5, 20);
        taRincian.setEditable(false);
        taRincian.setLineWrap(true);
        taRincian.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(taRincian);
        scrollPane.setPreferredSize(new Dimension(200, 100));

        RoundedButton btnBayar = new RoundedButton("Bayar", new Color(0x16366e));

        RoundedButton btnKeluar = new RoundedButton("Keluar", new Color(0xCB1F1F));

        btnBayar.addActionListener(e -> prosesBayar());
        btnKeluar.addActionListener(e -> {
            dispose();
            formTransaksi.setVisible(true);
        });

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(lblTotal, gbc);
        gbc.gridx = 1;
        panel.add(tfTotal, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(lblBayar, gbc);
        gbc.gridx = 1;
        panel.add(tfBayar, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(lblKembalian, gbc);
        gbc.gridx = 1;
        panel.add(tfKembalian, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(lblRincian, gbc);
        gbc.gridx = 1;
        panel.add(scrollPane, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(btnBayar, gbc);
        gbc.gridx = 1;
        panel.add(btnKeluar, gbc);

        backgroundPanel.add(panel, new GridBagConstraints());

        setContentPane(backgroundPanel);
    }

    private void prosesBayar() {
        try {
            int bayar = Integer.parseInt(tfBayar.getText().trim());
            if (bayar < totalBayar) {
                JOptionPane.showMessageDialog(this, "Uang tidak mencukupi!", "Gagal", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int kembali = bayar - totalBayar;
            tfKembalian.setText(formatRupiah(kembali));

            JOptionPane.showMessageDialog(this, "Pembayaran berhasil!\nTerima kasih telah berkunjung.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Masukkan nominal uang bayar dengan benar!", "Input Salah", JOptionPane.WARNING_MESSAGE);
        }
    }

    private String formatRupiah(int number) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return nf.format(number);
    }

    private String getRincianTransaksi() {
        StringBuilder rincian = new StringBuilder();
        NumberFormat rupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

        if (formTransaksi.getCbAdministrasi().isSelected()) {
            rincian.append("Administrasi: ").append(rupiah.format(10000)).append("\n");
        }
        if (formTransaksi.getCbKonsultasi().isSelected()) {
            rincian.append("Konsultasi: ").append(rupiah.format(50000)).append("\n");
        }
        if (formTransaksi.getCbObat().isSelected()) {
            rincian.append("Obat: ").append(rupiah.format(80000)).append("\n");
        }

        String tipe = (String) formTransaksi.getCbTipeRawat().getSelectedItem();
        rincian.append("Tipe Perawatan: ").append(tipe).append("\n");

        if ("Rawat Inap".equals(tipe)) {
            Kamar kamarDipilih = (Kamar) formTransaksi.getCbKamar().getSelectedItem();
            if (kamarDipilih != null) {
                int hari = formTransaksi.getHariInap();
                double hargaTotal = kamarDipilih.getHarga() * hari;
                double diskon = hargaTotal * kamarDipilih.getDiskon();
                double total = hargaTotal - diskon;

                rincian.append("Kamar: ").append(kamarDipilih.getNama()).append("\n");
                rincian.append("Durasi: ").append(hari).append(" hari\n");
                rincian.append("Biaya Kamar (setelah diskon): ").append(rupiah.format((int) total)).append("\n");
            }
        }

        return rincian.toString();
    }
}
