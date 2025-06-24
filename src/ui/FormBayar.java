package ui;

import model.Pasien;
import model.Kamar;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Locale;
import java.text.NumberFormat;

public class FormBayar extends JFrame {
    private JTextField tfTotal, tfBayar, tfKembalian;
    private JTextArea taRincian;
    private JComboBox<String> cbMetodeBayar;
    private JComboBox<String> cbTenor;
    private int totalBayar;
    private List<Pasien> dataPasien;
    private FormTransaksi formTransaksi;

    public FormBayar(int totalBayar, List<Pasien> dataPasien, FormTransaksi formTransaksi) {
        this.totalBayar = totalBayar;
        this.dataPasien = dataPasien;
        this.formTransaksi = formTransaksi;

        setTitle("Halaman Pembayaran - RS Formatik");
        setSize(600, 550);
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
        JLabel lblMetode = new JLabel("Metode Pembayaran:");
        JLabel lblTenor = new JLabel("Tenor Angsuran:");

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

        cbMetodeBayar = new JComboBox<>(new String[]{"Tunai", "BPJS", "Credit/Angsuran"});
        cbTenor = new JComboBox<>(new String[]{"3 bulan", "6 bulan", "12 bulan"});
        cbTenor.setVisible(false);
        lblTenor.setVisible(false);

        RoundedButton btnBayar = new RoundedButton("Bayar", new Color(0x16366e));
        RoundedButton btnKeluar = new RoundedButton("Keluar", new Color(0xCB1F1F));

        btnBayar.addActionListener(e -> prosesBayar());
        btnKeluar.addActionListener(e -> {
            dispose();
            formTransaksi.setVisible(true);
        });

        cbMetodeBayar.addActionListener(e -> {
            String metode = (String) cbMetodeBayar.getSelectedItem();
            if (metode.equals("BPJS")) {
                tfTotal.setText(formatRupiah(0));
                tfKembalian.setText("");
                tfBayar.setText("");
                cbTenor.setVisible(false);
                lblTenor.setVisible(false);
            } else if (metode.equals("Credit/Angsuran")) {
                cbTenor.setVisible(true);
                lblTenor.setVisible(true);
                updateTotalAngsuran();
            } else {
                tfTotal.setText(formatRupiah(totalBayar));
                tfKembalian.setText("");
                tfBayar.setText("");
                cbTenor.setVisible(false);
                lblTenor.setVisible(false);
            }
        });

        cbTenor.addActionListener(e -> {
            if (cbMetodeBayar.getSelectedItem().equals("Credit/Angsuran")) {
                updateTotalAngsuran();
            }
        });

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        gbc.gridx = 0; gbc.gridy = row;
        panel.add(lblTotal, gbc);
        gbc.gridx = 1;
        panel.add(tfTotal, gbc); row++;

        gbc.gridx = 0; gbc.gridy = row;
        panel.add(lblBayar, gbc);
        gbc.gridx = 1;
        panel.add(tfBayar, gbc); row++;

        gbc.gridx = 0; gbc.gridy = row;
        panel.add(lblKembalian, gbc);
        gbc.gridx = 1;
        panel.add(tfKembalian, gbc); row++;

        gbc.gridx = 0; gbc.gridy = row;
        panel.add(lblMetode, gbc);
        gbc.gridx = 1;
        panel.add(cbMetodeBayar, gbc); row++;

        gbc.gridx = 0; gbc.gridy = row;
        panel.add(lblTenor, gbc);
        gbc.gridx = 1;
        panel.add(cbTenor, gbc); row++;

        gbc.gridx = 0; gbc.gridy = row;
        panel.add(lblRincian, gbc);
        gbc.gridx = 1;
        panel.add(scrollPane, gbc); row++;

        gbc.gridx = 0; gbc.gridy = row;
        panel.add(btnBayar, gbc);
        gbc.gridx = 1;
        panel.add(btnKeluar, gbc);

        backgroundPanel.add(panel, new GridBagConstraints());
        setContentPane(backgroundPanel);
    }

    private void prosesBayar() {
        try {
            String metode = (String) cbMetodeBayar.getSelectedItem();

            if (metode.equals("BPJS")) {
                tfKembalian.setText(formatRupiah(0));
                JOptionPane.showMessageDialog(this, "Pembayaran via BPJS berhasil!\nTotal dibayar: Rp 0", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (metode.equals("Credit/Angsuran")) {
                String tenorStr = (String) cbTenor.getSelectedItem();
                int bulan = tenorStr.contains("6") ? 6 : tenorStr.contains("12") ? 12 : 3;
                int adminTotal = 5000 * bulan;
                int totalAngsuran = totalBayar + adminTotal;
                int angsuranPerBulan = totalAngsuran / bulan;

                int bayar = Integer.parseInt(tfBayar.getText().trim());
                if (bayar < angsuranPerBulan) {
                    JOptionPane.showMessageDialog(this, "Minimal bayar cicilan bulan ini: " + formatRupiah(angsuranPerBulan), "Gagal", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int kembalian = bayar - angsuranPerBulan;
                tfKembalian.setText(formatRupiah(kembalian));

                JOptionPane.showMessageDialog(this,
                        "Pembayaran angsuran berhasil!\n" +
                                "Total angsuran: " + formatRupiah(totalAngsuran) + "\n" +
                                "Angsuran per bulan: " + formatRupiah(angsuranPerBulan) + "\n" +
                                "Sisa cicilan: " + (bulan - 1) + " bulan lagi (" + formatRupiah(angsuranPerBulan * (bulan - 1)) + ")",
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Jika tunai
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

    private void updateTotalAngsuran() {
        String tenorStr = (String) cbTenor.getSelectedItem();
        int bulan = 3;
        if (tenorStr != null) {
            if (tenorStr.contains("6")) bulan = 6;
            else if (tenorStr.contains("12")) bulan = 12;
        }
        int admin = 5000 * bulan;
        int totalAngsuran = totalBayar + admin;
        int angsuranPerBulan = totalAngsuran / bulan;
        tfTotal.setText(formatRupiah(angsuranPerBulan));
    }

    private String formatRupiah(int number) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return nf.format(number);
    }

    private String getRincianTransaksi() {
        StringBuilder rincian = new StringBuilder();
        NumberFormat rupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

        if (formTransaksi.getCbAdministrasi().isSelected()) {
            rincian.append("Administrasi: ").append(rupiah.format(20000)).append("\n");
        }
        if (formTransaksi.getCbKonsultasi().isSelected()) {
            rincian.append("Konsultasi: ").append(rupiah.format(100000)).append("\n");
        }
        if (formTransaksi.getCbObat().isSelected()) {
            rincian.append("Obat: ").append(rupiah.format(50000)).append("\n");
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
