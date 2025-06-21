package ui;

import model.Pasien;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;

public class FormPendaftaran extends JFrame {
    private JTextField tfNama;
    private JTextArea taKeluhan;
    private JTextField tfNoPasien;

    public static List<Pasien> dataPasien = new ArrayList<>();

    public FormPendaftaran() {
        setTitle("Halaman Pendaftaran Pasien - RS Formatik");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        BackgroundPanel backgroundPanel = new BackgroundPanel("assets/background.png");
        backgroundPanel.setLayout(new BorderLayout());

        ImageIcon logoIcon = new ImageIcon("assets/logo.png");
        Image logoImage = logoIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        JLabel lblLogo = new JLabel(new ImageIcon(logoImage));
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblLogo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblNama = new JLabel("Nama Pasien:");
        tfNama = new JTextField(20);

        JLabel lblKeluhan = new JLabel("Keluhan:");
        taKeluhan = new JTextArea(3, 20);
        taKeluhan.setLineWrap(true);
        taKeluhan.setWrapStyleWord(true);
        JScrollPane scrollKeluhan = new JScrollPane(taKeluhan);

        JLabel lblNoPasien = new JLabel("Nomor Pasien:");
        tfNoPasien = new JTextField(generateNoPasien());
        tfNoPasien.setEditable(false);

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(lblNama, gbc);
        gbc.gridx = 1;
        formPanel.add(tfNama, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(lblKeluhan, gbc);
        gbc.gridx = 1;
        formPanel.add(scrollKeluhan, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(lblNoPasien, gbc);
        gbc.gridx = 1;
        formPanel.add(tfNoPasien, gbc);

        JPanel tombolPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        tombolPanel.setOpaque(false);

        RoundedButton btnDaftar = new RoundedButton("Daftar", new Color(0x4c9e8e));

        RoundedButton btnMasuk = new RoundedButton("Masuk", new Color(0x16366e));

        RoundedButton btnKeluar = new RoundedButton("Keluar", new Color(0xCB1F1F));

        tombolPanel.add(btnDaftar);
        tombolPanel.add(btnMasuk);
        tombolPanel.add(btnKeluar);

        btnDaftar.addActionListener(e -> {
            String nama = tfNama.getText().trim();
            String keluhan = taKeluhan.getText().trim();
            String noPasien = tfNoPasien.getText();

            if (nama.isEmpty() || keluhan.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Harap isi semua data terlebih dahulu.");
                return;
            }

            dataPasien.add(new Pasien(noPasien, nama));
            JOptionPane.showMessageDialog(this, "Pendaftaran berhasil!\nNomor Pasien: " + noPasien);

            tfNoPasien.setText(generateNoPasien());
            tfNama.setText("");
            taKeluhan.setText("");
        });

        btnMasuk.addActionListener(e -> {
            new FormTransaksi(dataPasien).setVisible(true);
            dispose();
        });

        btnKeluar.addActionListener(e -> System.exit(0));

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        centerPanel.add(lblLogo);
        centerPanel.add(formPanel);

        backgroundPanel.add(centerPanel, BorderLayout.CENTER);
        backgroundPanel.add(tombolPanel, BorderLayout.SOUTH);

        setContentPane(backgroundPanel);
    }

    public FormPendaftaran(List<Pasien> pasienList) {
        this();
        dataPasien = pasienList;
    }

    private String generateNoPasien() {
        int random = (int) (Math.random() * 10000);
        return "P" + String.format("%04d", random);
    }
}
