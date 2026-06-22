
package menuutama;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import koneksi.Koneksi;

/**
 *
 * @authordeff
 */
public class PengaturanSubKriteria extends javax.swing.JPanel {
    private Connection conn = new Koneksi().connect();
    private DefaultTableModel tabmode;
    /**
     * Creates new form Pengaturan
     */
    public PengaturanSubKriteria() {
        initComponents();
        updateDataTabel();
    }
    
protected void kosong(){
    cbSubLayananTambahan1.setSelectedIndex(0);
    cbSubLayananTambahan2.setSelectedIndex(0);
    cbSubLayananTambahan3.setSelectedIndex(0);
    cbSubLayananTambahan4.setSelectedIndex(0);

    cbSubBandwidth1.setSelectedIndex(0);
    cbSubBandwidth2.setSelectedIndex(0);
    cbSubBandwidth3.setSelectedIndex(0);

    cbSubHarga1.setSelectedIndex(0);
    cbSubHarga2.setSelectedIndex(0);
    cbSubHarga3.setSelectedIndex(0);

    cbSubFUP1.setSelectedIndex(0);
    cbSubFUP2.setSelectedIndex(0);

    cbSubFiber1.setSelectedIndex(0);
    cbSubFiber2.setSelectedIndex(0);
}

    
    protected void updateDataTabel(){
        Object[] Baris = {
            "Kode Kriteria",
            "Nama Kriteria",
            "Nama SubKriteria",
            "Kepentingan SubKriteria"
        };
        tabmode = new DefaultTableModel(null, Baris);
        tabelSubKriteria.setModel(tabmode);
        String sql = "SELECT * FROM sub_kriteria ORDER BY kd_kriteria, no_sub";
        try{
            java.sql.Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            while(hasil.next()){
                String a = hasil.getString("kd_kriteria");
                String b = hasil.getString("nama_kriteria");
                String c = hasil.getString("nama_sub_kriteria");
                String d = hasil.getString("prioritas_kepentingan");
                
                String[] data={a, b, c, d};
                tabmode.addRow(data);
            }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,e);
        }
    }
    
protected void getDataTabel() {
    String sql = "SELECT nama_sub_kriteria FROM sub_kriteria ORDER BY kd_kriteria, no_sub";
    int n = 1;

    try {
        java.sql.Statement stat = conn.createStatement();
        ResultSet hasil = stat.executeQuery(sql);

        while (hasil.next()) {
            String a = hasil.getString("nama_sub_kriteria");

            // 1-4 : Layanan Tambahan
            if (n == 1) {
                cbSubLayananTambahan1.setSelectedItem(a);
            } else if (n == 2) {
                cbSubLayananTambahan2.setSelectedItem(a);
            } else if (n == 3) {
                cbSubLayananTambahan3.setSelectedItem(a);
            } else if (n == 4) {
                cbSubLayananTambahan4.setSelectedItem(a);

            // 5-7 : Harga
            } else if (n == 5) {
                cbSubHarga1.setSelectedItem(a);
            } else if (n == 6) {
                cbSubHarga2.setSelectedItem(a);
            } else if (n == 7) {
                cbSubHarga3.setSelectedItem(a);

            // 8-10 : Bandwidth
            } else if (n == 8) {
                cbSubBandwidth1.setSelectedItem(a);
            } else if (n == 9) {
                cbSubBandwidth2.setSelectedItem(a);
            } else if (n == 10) {
                cbSubBandwidth3.setSelectedItem(a);

            // 11-12 : FUP
            } else if (n == 11) {
                cbSubFUP1.setSelectedItem(a);
            } else if (n == 12) {
                cbSubFUP2.setSelectedItem(a);

            // 13-14 : Status Fiber
            } else if (n == 13) {
                cbSubFiber1.setSelectedItem(a);
            } else if (n == 14) {
                cbSubFiber2.setSelectedItem(a);
            }

            n++;
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e);
    }
}

protected void insertDataSubKriteria() {
    try {
        int n = 1, nLayanan = 1, nHarga = 1, nBandwidth = 1, nFUP = 1, nFiber = 1, i = 1;

        do {
            String kepentingan = "";
            String sql = "INSERT INTO sub_kriteria VALUES (?,?,?,?,?)";
            PreparedStatement stat = conn.prepareStatement(sql);
            java.sql.Statement statCek = conn.createStatement();

            String sqlLayanan  = "SELECT kd_kriteria, nama_kriteria FROM kriteria WHERE nama_kriteria='Layanan Tambahan'";
            String sqlHarga    = "SELECT kd_kriteria, nama_kriteria FROM kriteria WHERE nama_kriteria='Harga'";
            String sqlBandwidth= "SELECT kd_kriteria, nama_kriteria FROM kriteria WHERE nama_kriteria='Bandwidth'";
            String sqlFUP      = "SELECT kd_kriteria, nama_kriteria FROM kriteria WHERE nama_kriteria='FUP ( Fair Play Usage)'";
            String sqlFiber    = "SELECT kd_kriteria, nama_kriteria FROM kriteria WHERE nama_kriteria='Fiber Optic'";

            ResultSet hasil;

            // n=1 Layanan Tambahan (4)
            if (n == 1) {
                hasil = statCek.executeQuery(sqlLayanan);

                stat.setString(1, Integer.toString(i)); i++;
                while (hasil.next()) {
                    stat.setString(2, hasil.getString("kd_kriteria"));
                    stat.setString(3, hasil.getString("nama_kriteria"));
                }

                if (nLayanan == 1) {
                    stat.setString(4, cbSubLayananTambahan1.getSelectedItem().toString());
                    kepentingan = "Sangat Penting ke-1";
                } else if (nLayanan == 2) {
                    stat.setString(4, cbSubLayananTambahan2.getSelectedItem().toString());
                    kepentingan = "Penting ke-2";
                } else if (nLayanan == 3) {
                    stat.setString(4, cbSubLayananTambahan3.getSelectedItem().toString());
                    kepentingan = "Cukup Penting ke-3";
                } else {
                    stat.setString(4, cbSubLayananTambahan4.getSelectedItem().toString());
                    kepentingan = "Biasa ke-4";
                    n++; // pindah ke Harga
                }

                stat.setString(5, kepentingan);
                stat.executeUpdate();
                nLayanan++;

            // n=2 Harga (3)
            } else if (n == 2) {
                hasil = statCek.executeQuery(sqlHarga);

                stat.setString(1, Integer.toString(i)); i++;
                while (hasil.next()) {
                    stat.setString(2, hasil.getString("kd_kriteria"));
                    stat.setString(3, hasil.getString("nama_kriteria"));
                }

                if (nHarga == 1) {
                    stat.setString(4, cbSubHarga1.getSelectedItem().toString());
                    kepentingan = "Sangat Penting ke-1";
                } else if (nHarga == 2) {
                    stat.setString(4, cbSubHarga2.getSelectedItem().toString());
                    kepentingan = "Cukup Penting ke-2";
                } else {
                    stat.setString(4, cbSubHarga3.getSelectedItem().toString());
                    kepentingan = "Biasa ke-3";
                    n++; // pindah ke Bandwidth
                }

                stat.setString(5, kepentingan);
                stat.executeUpdate();
                nHarga++;

            // n=3 Bandwidth (3)
            } else if (n == 3) {
                hasil = statCek.executeQuery(sqlBandwidth);

                stat.setString(1, Integer.toString(i)); i++;
                while (hasil.next()) {
                    stat.setString(2, hasil.getString("kd_kriteria"));
                    stat.setString(3, hasil.getString("nama_kriteria"));
                }

                if (nBandwidth == 1) {
                    stat.setString(4, cbSubBandwidth1.getSelectedItem().toString());
                    kepentingan = "Sangat Penting ke-1";
                } else if (nBandwidth == 2) {
                    stat.setString(4, cbSubBandwidth2.getSelectedItem().toString());
                    kepentingan = "Cukup Penting ke-2";
                } else {
                    stat.setString(4, cbSubBandwidth3.getSelectedItem().toString());
                    kepentingan = "Biasa ke-3";
                    n++; // pindah ke FUP
                }

                stat.setString(5, kepentingan);
                stat.executeUpdate();
                nBandwidth++;

            // n=4 FUP (2)
            } else if (n == 4) {
                hasil = statCek.executeQuery(sqlFUP);

                stat.setString(1, Integer.toString(i)); i++;
                while (hasil.next()) {
                    stat.setString(2, hasil.getString("kd_kriteria"));
                    stat.setString(3, hasil.getString("nama_kriteria"));
                }

                if (nFUP == 1) {
                    stat.setString(4, cbSubFUP1.getSelectedItem().toString());
                    kepentingan = "Sangat Penting ke-1";
                } else {
                    stat.setString(4, cbSubFUP2.getSelectedItem().toString());
                    kepentingan = "Penting ke-2";
                    n++; // pindah ke Fiber
                }

                stat.setString(5, kepentingan);
                stat.executeUpdate();
                nFUP++;

            // n=5 Status Fiber (2)
            } else { // n == 5
                hasil = statCek.executeQuery(sqlFiber);

                stat.setString(1, Integer.toString(i)); i++;
                while (hasil.next()) {
                    stat.setString(2, hasil.getString("kd_kriteria"));
                    stat.setString(3, hasil.getString("nama_kriteria"));
                }

                if (nFiber == 1) {
                    stat.setString(4, cbSubFiber1.getSelectedItem().toString());
                    kepentingan = "Sangat Penting ke-1";
                } else {
                    stat.setString(4, cbSubFiber2.getSelectedItem().toString());
                    kepentingan = "Penting ke-2";
                    n++; // selesai
                }

                stat.setString(5, kepentingan);
                stat.executeUpdate();
                nFiber++;
            }

        } while (n <= 5);

        JOptionPane.showMessageDialog(null, "DATA Berhasil Disimpan");
        kosong();
        updateDataTabel();

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Data Gagal Disimpan " + e);
    }
}
protected void hapusDataSubKriteria() {
    int ok = JOptionPane.showConfirmDialog(null, "hapus", "Konfirmasi Dialog", JOptionPane.YES_NO_OPTION);
    if (ok == 0) {
        String sql = "DELETE FROM sub_kriteria";
        try {
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Berhasil diHapus ");
            kosong();
            updateDataTabel();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Data Gagal diHapus " + e);
        }
    }
}
protected void editDataSubKriteria() {
    try {
        int n = 1, nLayanan = 1, nHarga = 1, nBandwidth = 1, nFUP = 1, nFiber = 1, i = 1;
        String kepentingan = "";

        do {
            String sql = "UPDATE sub_kriteria SET kd_kriteria=?, nama_kriteria=?, nama_sub_kriteria=?, prioritas_kepentingan=? WHERE no_sub=?";
            PreparedStatement stat = conn.prepareStatement(sql);
            java.sql.Statement statCek = conn.createStatement();

            String sqlLayanan  = "SELECT kd_kriteria, nama_kriteria FROM kriteria WHERE nama_kriteria='Layanan Tambahan'";
            String sqlHarga    = "SELECT kd_kriteria, nama_kriteria FROM kriteria WHERE nama_kriteria='Harga'";
            String sqlBandwidth= "SELECT kd_kriteria, nama_kriteria FROM kriteria WHERE nama_kriteria='Bandwidth'";
            String sqlFUP      = "SELECT kd_kriteria, nama_kriteria FROM kriteria WHERE nama_kriteria='FUP'";
            String sqlFiber    = "SELECT kd_kriteria, nama_kriteria FROM kriteria WHERE nama_kriteria='Status Fiber'";

            ResultSet hasil;

            // set WHERE no_sub
            stat.setString(5, Integer.toString(i)); i++;

            if (n == 1) {
                hasil = statCek.executeQuery(sqlLayanan);
                while (hasil.next()) {
                    stat.setString(1, hasil.getString("kd_kriteria"));
                    stat.setString(2, hasil.getString("nama_kriteria"));
                }

                if (nLayanan == 1) { stat.setString(3, cbSubLayananTambahan1.getSelectedItem().toString()); kepentingan = "Sangat Penting ke-1"; }
                else if (nLayanan == 2) { stat.setString(3, cbSubLayananTambahan2.getSelectedItem().toString()); kepentingan = "Penting ke-2"; }
                else if (nLayanan == 3) { stat.setString(3, cbSubLayananTambahan3.getSelectedItem().toString()); kepentingan = "Cukup Penting ke-3"; }
                else { stat.setString(3, cbSubLayananTambahan4.getSelectedItem().toString()); kepentingan = "Biasa ke-4"; n++; }

                stat.setString(4, kepentingan);
                stat.executeUpdate();
                nLayanan++;

            } else if (n == 2) {
                hasil = statCek.executeQuery(sqlHarga);
                while (hasil.next()) {
                    stat.setString(1, hasil.getString("kd_kriteria"));
                    stat.setString(2, hasil.getString("nama_kriteria"));
                }

                if (nHarga == 1) { stat.setString(3, cbSubHarga1.getSelectedItem().toString()); kepentingan = "Sangat Penting ke-1"; }
                else if (nHarga == 2) { stat.setString(3, cbSubHarga2.getSelectedItem().toString()); kepentingan = "Cukup Penting ke-2"; }
                else { stat.setString(3, cbSubHarga3.getSelectedItem().toString()); kepentingan = "Biasa ke-3"; n++; }

                stat.setString(4, kepentingan);
                stat.executeUpdate();
                nHarga++;

            } else if (n == 3) {
                hasil = statCek.executeQuery(sqlBandwidth);
                while (hasil.next()) {
                    stat.setString(1, hasil.getString("kd_kriteria"));
                    stat.setString(2, hasil.getString("nama_kriteria"));
                }

                if (nBandwidth == 1) { stat.setString(3, cbSubBandwidth1.getSelectedItem().toString()); kepentingan = "Sangat Penting ke-1"; }
                else if (nBandwidth == 2) { stat.setString(3, cbSubBandwidth2.getSelectedItem().toString()); kepentingan = "Cukup Penting ke-2"; }
                else { stat.setString(3, cbSubBandwidth3.getSelectedItem().toString()); kepentingan = "Biasa ke-3"; n++; }

                stat.setString(4, kepentingan);
                stat.executeUpdate();
                nBandwidth++;

            } else if (n == 4) {
                hasil = statCek.executeQuery(sqlFUP);
                while (hasil.next()) {
                    stat.setString(1, hasil.getString("kd_kriteria"));
                    stat.setString(2, hasil.getString("nama_kriteria"));
                }

                if (nFUP == 1) { stat.setString(3, cbSubFUP1.getSelectedItem().toString()); kepentingan = "Sangat Penting ke-1"; }
                else { stat.setString(3, cbSubFUP2.getSelectedItem().toString()); kepentingan = "Penting ke-2"; n++; }

                stat.setString(4, kepentingan);
                stat.executeUpdate();
                nFUP++;

            } else {
                hasil = statCek.executeQuery(sqlFiber);
                while (hasil.next()) {
                    stat.setString(1, hasil.getString("kd_kriteria"));
                    stat.setString(2, hasil.getString("nama_kriteria"));
                }

                if (nFiber == 1) { stat.setString(3, cbSubFiber1.getSelectedItem().toString()); kepentingan = "Sangat Penting ke-1"; }
                else { stat.setString(3, cbSubFiber2.getSelectedItem().toString()); kepentingan = "Penting ke-2"; n++; }

                stat.setString(4, kepentingan);
                stat.executeUpdate();
                nFiber++;
            }

        } while (n <= 5);

        JOptionPane.showMessageDialog(null, "DATA Berhasil DiUbah");
        kosong();
        updateDataTabel();

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Data Gagal DiUbah " + e);
    }
}


    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        judul = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelSubKriteria = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        tombolSimpan = new javax.swing.JButton();
        tombolEdit = new javax.swing.JButton();
        tombolHapus = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        cbSubLayananTambahan4 = new javax.swing.JComboBox<>();
        cbSubLayananTambahan3 = new javax.swing.JComboBox<>();
        cbSubLayananTambahan2 = new javax.swing.JComboBox<>();
        cbSubLayananTambahan1 = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        cbSubBandwidth3 = new javax.swing.JComboBox<>();
        cbSubBandwidth2 = new javax.swing.JComboBox<>();
        cbSubBandwidth1 = new javax.swing.JComboBox<>();
        jPanel5 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        cbSubFUP2 = new javax.swing.JComboBox<>();
        cbSubFUP1 = new javax.swing.JComboBox<>();
        jPanel6 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        cbSubHarga3 = new javax.swing.JComboBox<>();
        cbSubHarga2 = new javax.swing.JComboBox<>();
        cbSubHarga1 = new javax.swing.JComboBox<>();
        jPanel7 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        cbSubFiber2 = new javax.swing.JComboBox<>();
        cbSubFiber1 = new javax.swing.JComboBox<>();

        judul.setBackground(new java.awt.Color(255, 255, 255));
        judul.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        judul.setForeground(new java.awt.Color(51, 255, 51));
        judul.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        judul.setText("  Pengaturan Bobot Kepentingan SubKriteria");
        judul.setOpaque(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        tabelSubKriteria.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Kode Kriteria", "Nama Kriteria", "Nama SubKriteria", "Kepentingan SubKriteria"
            }
        ));
        tabelSubKriteria.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelSubKriteriaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabelSubKriteria);

        jLabel1.setText("Catatan : Edit data, klik data pada tabel terlebih dahulu");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 590, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addContainerGap(285, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        tombolSimpan.setBackground(new java.awt.Color(0, 51, 102));
        tombolSimpan.setForeground(new java.awt.Color(255, 255, 255));
        tombolSimpan.setText("Simpan");
        tombolSimpan.setBorder(null);
        tombolSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolSimpanActionPerformed(evt);
            }
        });

        tombolEdit.setBackground(new java.awt.Color(0, 51, 102));
        tombolEdit.setForeground(new java.awt.Color(255, 255, 255));
        tombolEdit.setText("Edit");
        tombolEdit.setBorder(null);
        tombolEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolEditActionPerformed(evt);
            }
        });

        tombolHapus.setBackground(new java.awt.Color(102, 0, 0));
        tombolHapus.setForeground(new java.awt.Color(255, 255, 255));
        tombolHapus.setText("Hapus");
        tombolHapus.setBorder(null);
        tombolHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolHapusActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Kepentingan Layanan Tambahan"));
        jPanel3.setToolTipText("");

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel17.setText("Layanan Yang Sangat Penting ke-1");

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel18.setText("Layanan Penting ke-2");

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel19.setText("Layanan Cukup Penting ke-3");

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel20.setText("Layanan biasa ke-4");

        cbSubLayananTambahan4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih SubKriteria Layanan Tambahan  -", "Internet saja", "Internet + TV BOX", "Internet + TV BOX + Cloud Storage", "Internet + TV BOX + Cloud Storage + Roter Mesh (Extender)" }));

        cbSubLayananTambahan3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih SubKriteria Layanan Tambahan  -", "Internet saja", "Internet + TV BOX", "Internet + TV BOX + Cloud Storage", "Internet + TV BOX + Cloud Storage + Roter Mesh (Extender)" }));

        cbSubLayananTambahan2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih SubKriteria Layanan Tambahan  -", "Internet saja", "Internet + TV BOX", "Internet + TV BOX + Cloud Storage", "Internet + TV BOX + Cloud Storage + Roter Mesh (Extender)" }));

        cbSubLayananTambahan1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih SubKriteria Layanan Tambahan  -", "Internet saja", "Internet + TV BOX", "Internet + TV BOX + Cloud Storage", "Internet + TV BOX + Cloud Storage + Roter Mesh (Extender)" }));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19)
                            .addComponent(jLabel20))
                        .addGap(81, 81, 81)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbSubLayananTambahan3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbSubLayananTambahan4, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addComponent(jLabel18))
                        .addGap(42, 42, 42)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbSubLayananTambahan2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbSubLayananTambahan1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(cbSubLayananTambahan1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(cbSubLayananTambahan2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(cbSubLayananTambahan3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(cbSubLayananTambahan4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Kepentingan Bandwidth"));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel9.setText("Bandwidth Yang Sangat Penting ke-1");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel15.setText("Bandwidth Cukup Penting ke-2");

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel16.setText("Bandwidth biasa ke-3");

        cbSubBandwidth3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SubKriteria Nilai Bandwidth -", "30 Mbps", "< 100 Mbps", "> 100 Mbps" }));

        cbSubBandwidth2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SubKriteria Nilai Bandwidth -", "30 Mbps", "< 100 Mbps", "> 100 Mbps" }));

        cbSubBandwidth1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SubKriteria Nilai Bandwidth -", "30 Mbps", "< 100 Mbps", "> 100 Mbps" }));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbSubBandwidth1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbSubBandwidth2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbSubBandwidth3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(cbSubBandwidth1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(cbSubBandwidth2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(cbSubBandwidth3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Kepentingan FUP"));

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel21.setText("FUP Yang Sangat Penting ke-1");

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel22.setText("FUP Penting ke-2");

        cbSubFUP2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Sub Kriteria FUP -", "Tidak Ada", "Ada" }));

        cbSubFUP1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Sub Kriteria FUP -", "Tidak Ada", "Ada" }));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21)
                    .addComponent(jLabel22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cbSubFUP1, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbSubFUP2, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(cbSubFUP1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(cbSubFUP2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Kepentingan Harga"));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel10.setText("Harga Sangat Penting ke-1");

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel25.setText("Harga Cukup Penting ke-2");

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel26.setText("Harga biasa ke-3");

        cbSubHarga3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Subkriteria Harga -", "< Rp.250.000", "Rp 250.000 -  Rp.500.000", "> Rp.500.000" }));

        cbSubHarga2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Subkriteria Harga -", "< Rp.250.000", "Rp 250.000 -  Rp.500.000", "> Rp.500.000" }));

        cbSubHarga1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Subkriteria Harga -", "< Rp.250.000", "Rp 250.000 -  Rp.500.000", "> Rp.500.000" }));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jLabel25)
                    .addComponent(jLabel26))
                .addGap(58, 58, 58)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbSubHarga3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbSubHarga2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbSubHarga1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(cbSubHarga1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbSubHarga2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25))
                .addGap(24, 24, 24)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbSubHarga3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Kepentingan FIBER"));

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel23.setText("FIBER Yang Sangat Penting ke-1");

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel24.setText("FIBER Penting ke-2");

        cbSubFiber2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Sub Kriteria Status Fiber  -", "Sudah Fiber", "Belum Fiber" }));

        cbSubFiber1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Sub Kriteria Status Fiber  -", "Sudah Fiber", "Belum Fiber" }));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel23)
                    .addComponent(jLabel24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cbSubFiber1, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbSubFiber2, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(cbSubFiber1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(cbSubFiber2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(tombolSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(tombolEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(tombolHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tombolSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tombolEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tombolHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(115, Short.MAX_VALUE))
        );

        jPanel3.getAccessibleContext().setAccessibleName("");
        jPanel4.getAccessibleContext().setAccessibleName("Kepentingan Bobot Jumlah Tanggungan");
        jPanel7.getAccessibleContext().setAccessibleDescription("");

        jScrollPane2.setViewportView(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 495, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(judul, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(judul, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tombolSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolSimpanActionPerformed
        // TODO add your handling code here:
        insertDataSubKriteria();
    }//GEN-LAST:event_tombolSimpanActionPerformed

    private void tombolEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolEditActionPerformed
        // TODO add your handling code here:
        editDataSubKriteria();
    }//GEN-LAST:event_tombolEditActionPerformed

    private void tombolHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolHapusActionPerformed
        // TODO add your handling code here:
        hapusDataSubKriteria();
    }//GEN-LAST:event_tombolHapusActionPerformed

    private void tabelSubKriteriaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelSubKriteriaMouseClicked
        // TODO add your handling code here:
        getDataTabel();
    }//GEN-LAST:event_tabelSubKriteriaMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cbSubBandwidth1;
    private javax.swing.JComboBox<String> cbSubBandwidth2;
    private javax.swing.JComboBox<String> cbSubBandwidth3;
    private javax.swing.JComboBox<String> cbSubFUP1;
    private javax.swing.JComboBox<String> cbSubFUP2;
    private javax.swing.JComboBox<String> cbSubFiber1;
    private javax.swing.JComboBox<String> cbSubFiber2;
    private javax.swing.JComboBox<String> cbSubHarga1;
    private javax.swing.JComboBox<String> cbSubHarga2;
    private javax.swing.JComboBox<String> cbSubHarga3;
    private javax.swing.JComboBox<String> cbSubLayananTambahan1;
    private javax.swing.JComboBox<String> cbSubLayananTambahan2;
    private javax.swing.JComboBox<String> cbSubLayananTambahan3;
    private javax.swing.JComboBox<String> cbSubLayananTambahan4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel judul;
    private javax.swing.JTable tabelSubKriteria;
    private javax.swing.JButton tombolEdit;
    private javax.swing.JButton tombolHapus;
    private javax.swing.JButton tombolSimpan;
    // End of variables declaration//GEN-END:variables
}
