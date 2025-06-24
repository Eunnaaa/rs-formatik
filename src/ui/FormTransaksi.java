package ui;

import model.Dokter;
import model.Kamar;
import model.Pasien;

import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.*;
import java.awt.Color;

public class FormTransaksi extends JFrame {
    private JTextField tfNoPasien, tfNamaPasien, tfTotal;
    private JComboBox<String> cbSpesialis;
    private JComboBox<Dokter> cbDokter;
    private JComboBox<Kamar> cbKamar;
    private JComboBox<String> cbTipeRawat;
    private JCheckBox cbAdministrasi, cbKonsultasi, cbObat;
    private JLabel lblJamPraktek, lblKamar;

    private List<Pasien> dataPasien = new ArrayList<>();
    private Map<String, List<Dokter>> dataDokter = new HashMap<>();
    private List<Kamar> dataKamar = new ArrayList<>();
    private int hariInap = 0;
    private int totalBayar = 0;

    public FormTransaksi(List<Pasien> pasienList) {
        this.dataPasien = pasienList;
        initUI();
    }

    public JCheckBox getCbAdministrasi() { return cbAdministrasi; }
    public JCheckBox getCbKonsultasi() { return cbKonsultasi; }
    public JCheckBox getCbObat() { return cbObat; }
    public JComboBox<String> getCbTipeRawat() { return cbTipeRawat; }
    public JComboBox<Kamar> getCbKamar() { return cbKamar; }
    public int getHariInap() { return hariInap; }

    private void initUI() {
        setTitle("Halaman Utama - RS Formatik");
        setSize(700, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initData();

        BackgroundPanel panel = new BackgroundPanel("assets/background.png");
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        ImageIcon icon = new ImageIcon("assets/logo.png");
        Image scaled = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaled));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        centerPanel.add(logoLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        tfNoPasien = new JTextField();
        tfNamaPasien = new JTextField(); tfNamaPasien.setEditable(false);
        cbSpesialis = new JComboBox<>();
        cbDokter = new JComboBox<>();
        lblJamPraktek = new JLabel("-");
        cbTipeRawat = new JComboBox<>(new String[]{"Rawat Jalan", "Rawat Inap"});
        lblKamar = new JLabel("Kamar Inap");
        cbKamar = new JComboBox<>(dataKamar.toArray(new Kamar[0]));
        cbAdministrasi = new JCheckBox("Administrasi");
        cbKonsultasi = new JCheckBox("Konsultasi");
        cbObat = new JCheckBox("Obat");
        tfTotal = new JTextField(); tfTotal.setEditable(false);

        for (String spesialis : dataDokter.keySet()) {
            cbSpesialis.addItem(spesialis);
        }

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("No Pasien"), gbc);
        gbc.gridx = 1; formPanel.add(tfNoPasien, gbc); row++;

        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Nama Pasien"), gbc);
        gbc.gridx = 1; formPanel.add(tfNamaPasien, gbc); row++;

        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Spesialis"), gbc);
        gbc.gridx = 1; formPanel.add(cbSpesialis, gbc); row++;

        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Dokter"), gbc);
        gbc.gridx = 1; formPanel.add(cbDokter, gbc); row++;

        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Jam Praktek"), gbc);
        gbc.gridx = 1; formPanel.add(lblJamPraktek, gbc); row++;

        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Tipe Perawatan"), gbc);
        gbc.gridx = 1; formPanel.add(cbTipeRawat, gbc); row++;

        gbc.gridx = 0; gbc.gridy = row; formPanel.add(lblKamar, gbc);
        gbc.gridx = 1; formPanel.add(cbKamar, gbc); row++;

        JPanel checkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        checkPanel.setOpaque(false);
        checkPanel.add(cbAdministrasi);
        checkPanel.add(cbKonsultasi);
        checkPanel.add(cbObat);

        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Layanan Tambahan"), gbc);
        gbc.gridx = 1; formPanel.add(checkPanel, gbc); row++;

        gbc.gridx = 0; gbc.gridy = row; formPanel.add(new JLabel("Total Bayar"), gbc);
        gbc.gridx = 1; formPanel.add(tfTotal, gbc); row++;

        centerPanel.add(formPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);

        RoundedButton btnHitung = new RoundedButton("Hitung", new Color(0x4c9e8e));
        RoundedButton btnBayar = new RoundedButton("Bayar", new Color(0x16366e));
        RoundedButton btnKeluar = new RoundedButton("Kembali", new Color(0xCB1F1F));

        buttonPanel.add(btnHitung);
        buttonPanel.add(btnBayar);
        buttonPanel.add(btnKeluar);

        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        setContentPane(panel);

        tfNoPasien.addActionListener(e -> {
            String no = tfNoPasien.getText().trim();
            tfNamaPasien.setText("");
            for (Pasien p : dataPasien) {
                if (p.getNoPasien().equalsIgnoreCase(no)) {
                    tfNamaPasien.setText(p.getNama());
                    break;
                }
            }
            if (tfNamaPasien.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Pasien tidak ditemukan!", "Info", JOptionPane.WARNING_MESSAGE);
            }
        });

        cbSpesialis.addActionListener(e -> {
            String spesialis = (String) cbSpesialis.getSelectedItem();
            cbDokter.removeAllItems();
            List<Dokter> list = dataDokter.getOrDefault(spesialis, new ArrayList<>());
            for (Dokter d : list) cbDokter.addItem(d);
        });

        cbDokter.addActionListener(e -> {
            Dokter d = (Dokter) cbDokter.getSelectedItem();
            if (d != null) lblJamPraktek.setText(d.getJadwal());
        });

        cbTipeRawat.addActionListener(e -> {
            String tipe = (String) cbTipeRawat.getSelectedItem();
            boolean inap = tipe.equals("Rawat Inap");
            lblKamar.setVisible(inap);
            cbKamar.setVisible(inap);

            if (inap) {
                try {
                    String input = JOptionPane.showInputDialog(this, "Jumlah hari rawat inap (maks. 5 hari):");
                    hariInap = Integer.parseInt(input);
                    if (hariInap < 1) hariInap = 1;
                    if (hariInap > 5) {
                        JOptionPane.showMessageDialog(this, "Maksimal 5 hari rawat inap. Ditetapkan menjadi 5 hari.");
                        hariInap = 5;
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Input tidak valid. Default 1 hari.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                    hariInap = 1;
                }
            } else {
                hariInap = 0;
            }
        });

        btnHitung.addActionListener(e -> hitungTotal());

        btnBayar.addActionListener(e -> {
            if (totalBayar == 0) {
                JOptionPane.showMessageDialog(this, "Hitung total dulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            } else {
                this.setVisible(false);
                new FormBayar(totalBayar, dataPasien, this).setVisible(true);
            }
        });

        btnKeluar.addActionListener(e -> {
            dispose();
            new FormPendaftaran(dataPasien).setVisible(true);
        });

        cbSpesialis.setSelectedIndex(0);
        lblKamar.setVisible(false);
        cbKamar.setVisible(false);

        setVisible(true);
    }

    private void initData() {
        dataDokter.put("Umum", List.of(
                new Dokter("Dr. Andi", "Umum", "08.00", "15.00"),
                new Dokter("Dr. Budi", "Umum", "16.00", "23.00")));
        dataDokter.put("S.Gigi", List.of(
                new Dokter("Drg. Citra", "Gigi", "08.00", "15.00"),
                new Dokter("Drg. Dini", "Gigi", "16.00", "23.00")));
        dataDokter.put("S.Saraf", List.of(
                new Dokter("Dr. Eko", "Saraf", "08.00", "15.00"),
                new Dokter("Dr. Farid", "Saraf", "16.00", "23.00")));
        dataDokter.put("S.Anak", List.of(
                new Dokter("Dr. Mulyono", "S.Anak", "08.00", "15.00"),
                new Dokter("Dr. Sutopo", "S.Anak", "16.00", "23.00")));
        dataDokter.put("S.Penyakit Dalam", List.of(
                new Dokter("Dr. Jo", "S.Penyakit Dalam", "08.00", "15.00"),
                new Dokter("Dr. Al", "S.Penyakit Dalam", "16.00", "23.00")));
        dataDokter.put("S.Bedah Umum", List.of(
                new Dokter("Dr. Yeni", "S.Bedah Umum", "08.00", "15.00"),
                new Dokter("Dr. Sinta", "S.Bedah Umum", "16.00", "23.00")));
        dataDokter.put("S.Obgyn", List.of(
                new Dokter("Dr. Yuli", "S.Obgyn", "08.00", "15.00"),
                new Dokter("Dr. Cantika", "S.Obgyn", "16.00", "23.00")));
        dataDokter.put("S.Radiologi", List.of(
                new Dokter("Dr. Yogi", "S.Radiologi", "08.00", "15.00"),
                new Dokter("Dr. Richard", "S.Radiologi", "16.00", "23.00")));

        dataKamar = List.of(
                new Kamar("Kelas 1", 300000, 0.3),
                new Kamar("Kelas 2", 500000, 0.2),
                new Kamar("Kelas 3", 700000, 0.1),
                new Kamar("VIP", 1000000, 0.05));
    }

    private void hitungTotal() {
        totalBayar = 0;
        if (cbAdministrasi.isSelected()) totalBayar += 10000;
        if (cbKonsultasi.isSelected()) totalBayar += 50000;
        if (cbObat.isSelected()) totalBayar += 80000;
        if (hariInap > 0) {
            Kamar kamar = (Kamar) cbKamar.getSelectedItem();
            if (kamar != null) {
                double harga = kamar.getHarga() * hariInap;
                double diskon = harga * kamar.getDiskon();
                totalBayar += (int) (harga - diskon);
            }
        }
        tfTotal.setText(formatRupiah(totalBayar));
    }

    private String formatRupiah(int number) {
        return NumberFormat.getCurrencyInstance(new Locale("id", "ID")).format(number);
    }
}
