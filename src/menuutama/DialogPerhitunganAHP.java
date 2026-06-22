
package menuutama;
import java.awt.Color;
import java.awt.Cursor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import javax.swing.JRootPane;
import javax.swing.table.DefaultTableModel;
import koneksi.Koneksi;

/**
 *
 * @author
 */
public class DialogPerhitunganAHP extends javax.swing.JDialog {
    private Connection conn = new Koneksi().connect();
    private DefaultTableModel tabmode;
    protected KriteriaAhp kriteria = new KriteriaAhp();
    protected SubKriteriaAhp SubK = new SubKriteriaAhp();
    DecimalFormat df = new DecimalFormat("#.##");
    ArrayList<String> K = new ArrayList<String>();
    ArrayList<Double> KS4x4 = new ArrayList<Double>();
    ArrayList<Double> KS3x3 = new ArrayList<Double>();
    String noIdAlternatif, namaAlternatif, noHpAlternatif, pendidikanAlternatif, waktuAlternatif, sikapAlternatif;
    int wawancaraAlternatif;
    double nilaiAlternatif, totalNilai;
    String dtksAlternatif;
String fiberAlternatif, hargaAlternatif, fupAlternatif, bandwidthAlternatif, layananTambahanAlternatif;


public DialogPerhitunganAHP(java.awt.Frame parent, boolean modal) {
    super(parent, modal);
    initComponents();

    // WAJIB: tempelkan PanelPerhitungan ke Pane (Pane adalah JPanel)
    Pane.removeAll();
    Pane.setLayout(new java.awt.BorderLayout());
    Pane.add(PanelPerhitungan, java.awt.BorderLayout.CENTER);

    Pane.revalidate();
    Pane.repaint();

    getRelasiId();

    pack();                      // hitung ukuran dari isi
    setLocationRelativeTo(parent);
}
    
    void kosong(){
        TotalNilai.setText("");
    }

// -------------------------------
// Helper: set kolom prioritas sub
// -------------------------------
private void setKolom(int kolom, String v1, String v2, String v3, String v4){
    if(kolom==1){ PriorS11.setText(v1); PriorS12.setText(v2); PriorS13.setText(v3); PriorS14.setText(v4); }
    else if(kolom==2){ PriorS21.setText(v1); PriorS22.setText(v2); PriorS23.setText(v3); PriorS24.setText(v4); }
    else if(kolom==3){ PriorS31.setText(v1); PriorS32.setText(v2); PriorS33.setText(v3); PriorS34.setText(v4); }
    else if(kolom==4){ PriorS41.setText(v1); PriorS42.setText(v2); PriorS43.setText(v3); PriorS44.setText(v4); }
    else if(kolom==5){ PriorS51.setText(v1); PriorS52.setText(v2); PriorS53.setText(v3); PriorS54.setText(v4); }
}

// -------------------------------
// Setter kolom per kriteria (Internet)
// -------------------------------
private void setKolomFiber2x2(int kolom){
    setKolom(kolom,
        df.format(SubKriteriaAhp.prioritasSubFiber2x2[0]),
        df.format(SubKriteriaAhp.prioritasSubFiber2x2[1]),
        "", ""
    );
}

private void setKolomHarga3x3(int kolom){
    setKolom(kolom,
        df.format(SubKriteriaAhp.prioritasSubHarga3x3[0]),
        df.format(SubKriteriaAhp.prioritasSubHarga3x3[1]),
        df.format(SubKriteriaAhp.prioritasSubHarga3x3[2]),
        ""
    );
}

private void setKolomFUP2x2(int kolom){
    setKolom(kolom,
        df.format(SubKriteriaAhp.prioritasSubFUP2x2[0]),
        df.format(SubKriteriaAhp.prioritasSubFUP2x2[1]),
        "", ""
    );
}

private void setKolomBandwidth3x3(int kolom){
    setKolom(kolom,
        df.format(SubKriteriaAhp.prioritasSubBandwidth3x3[0]),
        df.format(SubKriteriaAhp.prioritasSubBandwidth3x3[1]),
        df.format(SubKriteriaAhp.prioritasSubBandwidth3x3[2]),
        ""
    );
}

private void setKolomLayananTambahan4x4(int kolom){
    setKolom(kolom,
        df.format(SubKriteriaAhp.prioritasSubLayananTambahan4x4[0]),
        df.format(SubKriteriaAhp.prioritasSubLayananTambahan4x4[1]),
        df.format(SubKriteriaAhp.prioritasSubLayananTambahan4x4[2]),
        df.format(SubKriteriaAhp.prioritasSubLayananTambahan4x4[3])
    );
}

// -------------------------------
// Disable field yang kosong (biar rapi)
// -------------------------------
private void disableIfEmpty(javax.swing.JTextField tf){
    if(tf.getText() == null || tf.getText().trim().isEmpty()){
        tf.setEnabled(false);
        tf.setEditable(false);
        tf.setText("");
    }else{
        tf.setEnabled(true);
        tf.setEditable(false); // biar user ga ngubah
    }
}

private void rapihinFieldPrioritas(){
    javax.swing.JTextField[] all = {
        PriorS11,PriorS12,PriorS13,PriorS14,
        PriorS21,PriorS22,PriorS23,PriorS24,
        PriorS31,PriorS32,PriorS33,PriorS34,
        PriorS41,PriorS42,PriorS43,PriorS44,
        PriorS51,PriorS52,PriorS53,PriorS54
    };
    for(javax.swing.JTextField tf : all){
        disableIfEmpty(tf);
    }
}

// -------------------------------
// Ambil daftar kriteria dari DB
// (Pastikan tabel kriteria berisi: Status Fiber, Harga, FUP, Bandwidth, Layanan Tambahan)
// -------------------------------
public void getKriteria(){
    K.clear();
    String sql = "SELECT nama_kriteria FROM kriteria ORDER BY kd_kriteria";
    try{
        java.sql.Statement stat = conn.createStatement();
        ResultSet hasil = stat.executeQuery(sql);
        while(hasil.next()){
            K.add(hasil.getString("nama_kriteria").trim());
        }
    }catch(SQLException e){
        JOptionPane.showMessageDialog(null,e);
    }
    System.out.println(K);
}

// -------------------------------
// Matriks berpasangan kriteria
// -------------------------------
public void getMatriksK(){
    k1k1.setText(df.format(kriteria.matriksBerpasangan[0][0]));
    k1k2.setText(df.format(kriteria.matriksBerpasangan[0][1]));
    k1k3.setText(df.format(kriteria.matriksBerpasangan[0][2]));
    k1k4.setText(df.format(kriteria.matriksBerpasangan[0][3]));
    k1k5.setText(df.format(kriteria.matriksBerpasangan[0][4]));

    k2k1.setText(df.format(kriteria.matriksBerpasangan[1][0]));
    k2k2.setText(df.format(kriteria.matriksBerpasangan[1][1]));
    k2k3.setText(df.format(kriteria.matriksBerpasangan[1][2]));
    k2k4.setText(df.format(kriteria.matriksBerpasangan[1][3]));
    k2k5.setText(df.format(kriteria.matriksBerpasangan[1][4]));

    k3k1.setText(df.format(kriteria.matriksBerpasangan[2][0]));
    k3k2.setText(df.format(kriteria.matriksBerpasangan[2][1]));
    k3k3.setText(df.format(kriteria.matriksBerpasangan[2][2]));
    k3k4.setText(df.format(kriteria.matriksBerpasangan[2][3]));
    k3k5.setText(df.format(kriteria.matriksBerpasangan[2][4]));

    k4k1.setText(df.format(kriteria.matriksBerpasangan[3][0]));
    k4k2.setText(df.format(kriteria.matriksBerpasangan[3][1]));
    k4k3.setText(df.format(kriteria.matriksBerpasangan[3][2]));
    k4k4.setText(df.format(kriteria.matriksBerpasangan[3][3]));
    k4k5.setText(df.format(kriteria.matriksBerpasangan[3][4]));

    k5k1.setText(df.format(kriteria.matriksBerpasangan[4][0]));
    k5k2.setText(df.format(kriteria.matriksBerpasangan[4][1]));
    k5k3.setText(df.format(kriteria.matriksBerpasangan[4][2]));
    k5k4.setText(df.format(kriteria.matriksBerpasangan[4][3]));
    k5k5.setText(df.format(kriteria.matriksBerpasangan[4][4]));
}

// -------------------------------
// Matriks normalisasi + prioritas kriteria
// -------------------------------
public void getMatriksNorK(){
    k1k1N.setText(df.format(kriteria.matriksNormalisasi[0][0]));
    k1k2N.setText(df.format(kriteria.matriksNormalisasi[0][1]));
    k1k3N.setText(df.format(kriteria.matriksNormalisasi[0][2]));
    k1k4N.setText(df.format(kriteria.matriksNormalisasi[0][3]));
    k1k5N.setText(df.format(kriteria.matriksNormalisasi[0][4]));

    k2k1N.setText(df.format(kriteria.matriksNormalisasi[1][0]));
    k2k2N.setText(df.format(kriteria.matriksNormalisasi[1][1]));
    k2k3N.setText(df.format(kriteria.matriksNormalisasi[1][2]));
    k2k4N.setText(df.format(kriteria.matriksNormalisasi[1][3]));
    k2k5N.setText(df.format(kriteria.matriksNormalisasi[1][4]));

    k3k1N.setText(df.format(kriteria.matriksNormalisasi[2][0]));
    k3k2N.setText(df.format(kriteria.matriksNormalisasi[2][1]));
    k3k3N.setText(df.format(kriteria.matriksNormalisasi[2][2]));
    k3k4N.setText(df.format(kriteria.matriksNormalisasi[2][3]));
    k3k5N.setText(df.format(kriteria.matriksNormalisasi[2][4]));

    k4k1N.setText(df.format(kriteria.matriksNormalisasi[3][0]));
    k4k2N.setText(df.format(kriteria.matriksNormalisasi[3][1]));
    k4k3N.setText(df.format(kriteria.matriksNormalisasi[3][2]));
    k4k4N.setText(df.format(kriteria.matriksNormalisasi[3][3]));
    k4k5N.setText(df.format(kriteria.matriksNormalisasi[3][4]));

    k5k1N.setText(df.format(kriteria.matriksNormalisasi[4][0]));
    k5k2N.setText(df.format(kriteria.matriksNormalisasi[4][1]));
    k5k3N.setText(df.format(kriteria.matriksNormalisasi[4][2]));
    k5k4N.setText(df.format(kriteria.matriksNormalisasi[4][3]));
    k5k5N.setText(df.format(kriteria.matriksNormalisasi[4][4]));

    Prior1.setText(df.format(kriteria.prioritas[0]));
    Prior2.setText(df.format(kriteria.prioritas[1]));
    Prior3.setText(df.format(kriteria.prioritas[2]));
    Prior4.setText(df.format(kriteria.prioritas[3]));
    Prior5.setText(df.format(kriteria.prioritas[4]));
}

// -------------------------------
// Prioritas sub-kriteria mengikuti urutan kriteria di DB
// Jika urutan DB selalu tetap, kamu bisa panggil setter manual di bawah.
// -------------------------------
public void getPrioritasSub(){
    getKriteria(); // isi list K dari DB

    // Isi berdasarkan NAMA KRITERIA (aman walau urutan DB berubah)
    for(int idx = 0; idx < K.size(); idx++){
        String nama = K.get(idx).trim();
        int kolom = idx + 1;

        if(nama.equalsIgnoreCase("Status Fiber")){
            setKolomFiber2x2(kolom);

        } else if(nama.equalsIgnoreCase("Harga")){
            setKolomHarga3x3(kolom);

        } else if(nama.equalsIgnoreCase("FUP")){
            setKolomFUP2x2(kolom);

        } else if(nama.equalsIgnoreCase("Bandwidth") || nama.equalsIgnoreCase("Bandwith")){
            setKolomBandwidth3x3(kolom);

        } else if(nama.equalsIgnoreCase("Layanan Tambahan")){
            setKolomLayananTambahan4x4(kolom);
        }
    }

    rapihinFieldPrioritas();
}

// -------------------------------
// Ambil alternatif layanan internet dari tabel layanan_internet
// -------------------------------
public void getAlternatif(){
    String sql = "SELECT * FROM layanan_internet WHERE no_id='" + cbIdLayananInternet.getSelectedItem().toString() + "'";
    try{
        java.sql.Statement stat = conn.createStatement();
        ResultSet hasil = stat.executeQuery(sql);

        if(hasil.next()){
            noIdAlternatif = hasil.getString("no_id");
            namaAlternatif = hasil.getString("nama_layanan");

            fiberAlternatif = hasil.getString("fiber");                 // Sudah Fiber / Belum Fiber
            hargaAlternatif = hasil.getString("harga");                 // <250 / 250-500 / >500
            fupAlternatif = hasil.getString("fup");                     // Ada / Tidak Ada
            bandwidthAlternatif = hasil.getString("bandwidth");         // 30 / <100 / >100
            layananTambahanAlternatif = hasil.getString("layanan_tambahan"); // 4 opsi
        }
    }catch(SQLException e){
        JOptionPane.showMessageDialog(null,e);
    }
}

// -------------------------------
// Hitung total nilai AHP untuk alternatif terpilih
// -------------------------------
public void getPenilaian(){
    totalNilai = 0;

    // K1 Status Fiber (2x2)
    if(fiberAlternatif != null && fiberAlternatif.equalsIgnoreCase("Sudah Fiber")){
        totalNilai += SubKriteriaAhp.prioritasSubFiber2x2[1] * kriteria.prioritas[0];
    } else {
        totalNilai += SubKriteriaAhp.prioritasSubFiber2x2[0] * kriteria.prioritas[0];
    }

    // K2 Harga (3x3)
    if(hargaAlternatif != null && hargaAlternatif.equals("< Rp.250.000")){
        totalNilai += SubKriteriaAhp.prioritasSubHarga3x3[0] * kriteria.prioritas[1];
    } else if(hargaAlternatif != null && hargaAlternatif.equals("Rp 250.000 -  Rp.500.000")){
        totalNilai += SubKriteriaAhp.prioritasSubHarga3x3[1] * kriteria.prioritas[1];
    } else {
        totalNilai += SubKriteriaAhp.prioritasSubHarga3x3[2] * kriteria.prioritas[1];
    }

    // K3 FUP (2x2)
    if(fupAlternatif != null && fupAlternatif.equalsIgnoreCase("Ada")){
        totalNilai += SubKriteriaAhp.prioritasSubFUP2x2[1] * kriteria.prioritas[2];
    } else {
        totalNilai += SubKriteriaAhp.prioritasSubFUP2x2[0] * kriteria.prioritas[2];
    }

    // K4 Bandwidth (3x3)
    if(bandwidthAlternatif != null && bandwidthAlternatif.equals("30 Mbps")){
        totalNilai += SubKriteriaAhp.prioritasSubBandwidth3x3[0] * kriteria.prioritas[3];
    } else if(bandwidthAlternatif != null && bandwidthAlternatif.equals("< 100 Mbps")){
        totalNilai += SubKriteriaAhp.prioritasSubBandwidth3x3[1] * kriteria.prioritas[3];
    } else {
        totalNilai += SubKriteriaAhp.prioritasSubBandwidth3x3[2] * kriteria.prioritas[3];
    }

    // K5 Layanan Tambahan (4x4)
    if(layananTambahanAlternatif != null && layananTambahanAlternatif.equals("Internet saja")){
        totalNilai += SubKriteriaAhp.prioritasSubLayananTambahan4x4[0] * kriteria.prioritas[4];
    } else if(layananTambahanAlternatif != null && layananTambahanAlternatif.equals("Internet + TV BOX")){
        totalNilai += SubKriteriaAhp.prioritasSubLayananTambahan4x4[1] * kriteria.prioritas[4];
    } else if(layananTambahanAlternatif != null && layananTambahanAlternatif.equals("Internet + TV BOX + Cloud Storage")){
        totalNilai += SubKriteriaAhp.prioritasSubLayananTambahan4x4[2] * kriteria.prioritas[4];
    } else {
        totalNilai += SubKriteriaAhp.prioritasSubLayananTambahan4x4[3] * kriteria.prioritas[4];
    }

    TotalNilai.setText(df.format(totalNilai));
}

// -------------------------------
// Isi combobox id layanan internet
// -------------------------------
public void getRelasiId(){
    String sql = "SELECT DISTINCT no_id FROM layanan_internet ORDER BY no_id";
    try{
        java.sql.Statement stat = conn.createStatement();
        ResultSet hasil = stat.executeQuery(sql);
        while(hasil.next()){
            cbIdLayananInternet.addItem(hasil.getString("no_id"));
        }
    }catch(SQLException e){
        JOptionPane.showMessageDialog(null,e);
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

        btnG = new javax.swing.ButtonGroup();
        PanelPerhitungan = new javax.swing.JPanel();
        judul = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cbIdLayananInternet = new javax.swing.JComboBox<>();
        mulaiHitung = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        k1k1 = new javax.swing.JTextField();
        k1k2 = new javax.swing.JTextField();
        k1k3 = new javax.swing.JTextField();
        k1k4 = new javax.swing.JTextField();
        k2k1 = new javax.swing.JTextField();
        k2k2 = new javax.swing.JTextField();
        k2k3 = new javax.swing.JTextField();
        k2k4 = new javax.swing.JTextField();
        k3k1 = new javax.swing.JTextField();
        k3k2 = new javax.swing.JTextField();
        k3k3 = new javax.swing.JTextField();
        k3k4 = new javax.swing.JTextField();
        k4k1 = new javax.swing.JTextField();
        k4k2 = new javax.swing.JTextField();
        k4k3 = new javax.swing.JTextField();
        k4k4 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        k1k5 = new javax.swing.JTextField();
        k2k5 = new javax.swing.JTextField();
        k3k5 = new javax.swing.JTextField();
        k4k5 = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        k5k1 = new javax.swing.JTextField();
        k5k2 = new javax.swing.JTextField();
        k5k3 = new javax.swing.JTextField();
        k5k4 = new javax.swing.JTextField();
        k5k5 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        k1k1N = new javax.swing.JTextField();
        k1k2N = new javax.swing.JTextField();
        k1k3N = new javax.swing.JTextField();
        k1k4N = new javax.swing.JTextField();
        k2k1N = new javax.swing.JTextField();
        k2k2N = new javax.swing.JTextField();
        k2k3N = new javax.swing.JTextField();
        k2k4N = new javax.swing.JTextField();
        k3k1N = new javax.swing.JTextField();
        k3k2N = new javax.swing.JTextField();
        k3k3N = new javax.swing.JTextField();
        k3k4N = new javax.swing.JTextField();
        k4k1N = new javax.swing.JTextField();
        k4k2N = new javax.swing.JTextField();
        k4k3N = new javax.swing.JTextField();
        k4k4N = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        Prior1 = new javax.swing.JTextField();
        Prior2 = new javax.swing.JTextField();
        Prior3 = new javax.swing.JTextField();
        Prior4 = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        k1k5N = new javax.swing.JTextField();
        k2k5N = new javax.swing.JTextField();
        k3k5N = new javax.swing.JTextField();
        k4k5N = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        k5k1N = new javax.swing.JTextField();
        k5k2N = new javax.swing.JTextField();
        k5k3N = new javax.swing.JTextField();
        k5k4N = new javax.swing.JTextField();
        k5k5N = new javax.swing.JTextField();
        Prior5 = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        TotalNilai = new javax.swing.JTextField();
        Simpan = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        PriorS11 = new javax.swing.JTextField();
        PriorS12 = new javax.swing.JTextField();
        PriorS13 = new javax.swing.JTextField();
        PriorS14 = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        PriorS21 = new javax.swing.JTextField();
        PriorS22 = new javax.swing.JTextField();
        PriorS23 = new javax.swing.JTextField();
        PriorS24 = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        PriorS31 = new javax.swing.JTextField();
        PriorS32 = new javax.swing.JTextField();
        PriorS33 = new javax.swing.JTextField();
        PriorS34 = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        PriorS41 = new javax.swing.JTextField();
        PriorS42 = new javax.swing.JTextField();
        PriorS43 = new javax.swing.JTextField();
        PriorS44 = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        PriorS51 = new javax.swing.JTextField();
        PriorS52 = new javax.swing.JTextField();
        PriorS53 = new javax.swing.JTextField();
        PriorS54 = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        namaLayananInternet = new javax.swing.JTextField();
        Pane = new javax.swing.JPanel();

        judul.setBackground(new java.awt.Color(255, 255, 255));
        judul.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        judul.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        judul.setText("Perhitungan Hasil Penilaian Calon Penerima Menggunakan Metode AHP");
        judul.setOpaque(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(153, 255, 153));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("No. ID");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 41, 130, 28));

        cbIdLayananInternet.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbIdLayananInternetItemStateChanged(evt);
            }
        });
        jPanel1.add(cbIdLayananInternet, new org.netbeans.lib.awtextra.AbsoluteConstraints(222, 44, 156, -1));

        mulaiHitung.setBackground(new java.awt.Color(0, 255, 0));
        mulaiHitung.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        mulaiHitung.setForeground(new java.awt.Color(255, 255, 255));
        mulaiHitung.setText("Mulai Hitung");
        mulaiHitung.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mulaiHitungActionPerformed(evt);
            }
        });
        jPanel1.add(mulaiHitung, new org.netbeans.lib.awtextra.AbsoluteConstraints(225, 155, 153, 35));
        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 202, 875, 10));

        jLabel2.setText("Matriks Perbandingan Kriteria");

        jLabel3.setText("K1");

        jLabel4.setText("K2");

        jLabel5.setText("K3");

        jLabel6.setText("K4");

        k1k1.setEditable(false);
        k1k1.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k1k2.setEditable(false);
        k1k2.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k1k3.setEditable(false);
        k1k3.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k1k4.setEditable(false);
        k1k4.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k2k1.setEditable(false);
        k2k1.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k2k2.setEditable(false);
        k2k2.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k2k3.setEditable(false);
        k2k3.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k2k4.setEditable(false);
        k2k4.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k3k1.setEditable(false);
        k3k1.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k3k2.setEditable(false);
        k3k2.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k3k3.setEditable(false);
        k3k3.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k3k4.setEditable(false);
        k3k4.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k4k1.setEditable(false);
        k4k1.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k4k2.setEditable(false);
        k4k2.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k4k3.setEditable(false);
        k4k3.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k4k4.setEditable(false);
        k4k4.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel7.setText("K1");

        jLabel8.setText("K2");

        jLabel9.setText("K3");

        jLabel10.setText("K4");

        jLabel32.setText("K5");

        k1k5.setEditable(false);
        k1k5.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k2k5.setEditable(false);
        k2k5.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k3k5.setEditable(false);
        k3k5.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k4k5.setEditable(false);
        k4k5.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel33.setText("K5");

        k5k1.setEditable(false);
        k5k1.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k5k2.setEditable(false);
        k5k2.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k5k3.setEditable(false);
        k5k3.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k5k4.setEditable(false);
        k5k4.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        k5k5.setEditable(false);
        k5k5.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel4)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(k2k1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(k2k2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel5)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(k3k1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(k3k2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel6)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(k4k1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(k4k2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel3)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(k1k1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(k1k2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel33)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(k5k1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(k5k2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(10, 10, 10)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(k1k3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(k2k3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(k3k3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(k4k3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(k5k3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(k1k4, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(k2k4, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(k3k4, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(k4k4, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(k5k4, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGap(29, 29, 29)
                            .addComponent(jLabel7)
                            .addGap(40, 40, 40)
                            .addComponent(jLabel8)
                            .addGap(38, 38, 38)
                            .addComponent(jLabel9)
                            .addGap(42, 42, 42)
                            .addComponent(jLabel10)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(102, 102, 102)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(k1k5, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(k2k5, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(k3k5, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(k4k5, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel32))
                    .addComponent(k5k5, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(12, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel32)
                        .addGap(5, 5, 5)
                        .addComponent(k1k5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(k2k5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(k3k5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(k4k5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(k5k5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10))
                        .addGap(5, 5, 5)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(k1k1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(k2k1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(k3k1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(k4k1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(k5k1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel33)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(k1k3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(k1k2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(k2k3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(k2k2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(k3k3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(k3k2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(k4k3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(k4k2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(k5k3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(k5k2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(k1k4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(k2k4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(k3k4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(k4k4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(k5k4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 220, -1, -1));

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setText("Matriks Normalisasi");
        jPanel3.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(37, 12, -1, -1));

        jLabel12.setText("K1");
        jPanel3.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(37, 70, -1, -1));

        jLabel13.setText("K2");
        jPanel3.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(37, 104, -1, -1));

        jLabel14.setText("K3");
        jPanel3.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(37, 138, -1, -1));

        jLabel15.setText("K4");
        jPanel3.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(37, 172, -1, -1));

        k1k1N.setEditable(false);
        k1k1N.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel3.add(k1k1N, new org.netbeans.lib.awtextra.AbsoluteConstraints(56, 67, 41, -1));

        k1k2N.setEditable(false);
        k1k2N.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel3.add(k1k2N, new org.netbeans.lib.awtextra.AbsoluteConstraints(109, 67, 41, -1));

        k1k3N.setEditable(false);
        k1k3N.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel3.add(k1k3N, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 67, 41, -1));

        k1k4N.setEditable(false);
        k1k4N.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel3.add(k1k4N, new org.netbeans.lib.awtextra.AbsoluteConstraints(213, 67, 41, -1));

        k2k1N.setEditable(false);
        k2k1N.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel3.add(k2k1N, new org.netbeans.lib.awtextra.AbsoluteConstraints(56, 101, 41, -1));

        k2k2N.setEditable(false);
        k2k2N.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel3.add(k2k2N, new org.netbeans.lib.awtextra.AbsoluteConstraints(109, 101, 41, -1));

        k2k3N.setEditable(false);
        k2k3N.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel3.add(k2k3N, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 101, 41, -1));

        k2k4N.setEditable(false);
        k2k4N.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel3.add(k2k4N, new org.netbeans.lib.awtextra.AbsoluteConstraints(213, 101, 41, -1));

        k3k1N.setEditable(false);
        k3k1N.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel3.add(k3k1N, new org.netbeans.lib.awtextra.AbsoluteConstraints(56, 135, 41, -1));

        k3k2N.setEditable(false);
        k3k2N.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel3.add(k3k2N, new org.netbeans.lib.awtextra.AbsoluteConstraints(109, 135, 41, -1));

        k3k3N.setEditable(false);
        k3k3N.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel3.add(k3k3N, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 135, 41, -1));

        k3k4N.setEditable(false);
        k3k4N.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel3.add(k3k4N, new org.netbeans.lib.awtextra.AbsoluteConstraints(213, 135, 41, -1));

        k4k1N.setEditable(false);
        k4k1N.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel3.add(k4k1N, new org.netbeans.lib.awtextra.AbsoluteConstraints(56, 169, 41, -1));

        k4k2N.setEditable(false);
        k4k2N.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel3.add(k4k2N, new org.netbeans.lib.awtextra.AbsoluteConstraints(109, 169, 41, -1));

        k4k3N.setEditable(false);
        k4k3N.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel3.add(k4k3N, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 169, 41, -1));

        k4k4N.setEditable(false);
        k4k4N.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel3.add(k4k4N, new org.netbeans.lib.awtextra.AbsoluteConstraints(213, 169, 41, -1));

        jLabel16.setText("K1");
        jPanel3.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(66, 46, -1, -1));

        jLabel17.setText("K2");
        jPanel3.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(119, 46, -1, -1));

        jLabel18.setText("K3");
        jPanel3.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(168, 46, -1, -1));

        jLabel19.setText("K4");
        jPanel3.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(221, 46, -1, -1));

        jLabel20.setText("Prioritas");
        jPanel3.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(331, 46, -1, -1));

        Prior1.setEditable(false);
        Prior1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel3.add(Prior1, new org.netbeans.lib.awtextra.AbsoluteConstraints(331, 67, 41, -1));

        Prior2.setEditable(false);
        Prior2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel3.add(Prior2, new org.netbeans.lib.awtextra.AbsoluteConstraints(331, 101, 41, -1));

        Prior3.setEditable(false);
        Prior3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel3.add(Prior3, new org.netbeans.lib.awtextra.AbsoluteConstraints(331, 135, 41, -1));

        Prior4.setEditable(false);
        Prior4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel3.add(Prior4, new org.netbeans.lib.awtextra.AbsoluteConstraints(331, 169, 41, -1));

        jLabel34.setText("K5");
        jPanel3.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(274, 46, -1, -1));

        k1k5N.setEditable(false);
        k1k5N.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel3.add(k1k5N, new org.netbeans.lib.awtextra.AbsoluteConstraints(266, 67, 41, -1));

        k2k5N.setEditable(false);
        k2k5N.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel3.add(k2k5N, new org.netbeans.lib.awtextra.AbsoluteConstraints(266, 101, 41, -1));

        k3k5N.setEditable(false);
        k3k5N.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel3.add(k3k5N, new org.netbeans.lib.awtextra.AbsoluteConstraints(266, 135, 41, -1));

        k4k5N.setEditable(false);
        k4k5N.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel3.add(k4k5N, new org.netbeans.lib.awtextra.AbsoluteConstraints(266, 169, 41, -1));

        jLabel35.setText("K5");
        jPanel3.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(37, 206, -1, -1));

        k5k1N.setEditable(false);
        k5k1N.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel3.add(k5k1N, new org.netbeans.lib.awtextra.AbsoluteConstraints(56, 203, 41, -1));

        k5k2N.setEditable(false);
        k5k2N.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel3.add(k5k2N, new org.netbeans.lib.awtextra.AbsoluteConstraints(109, 203, 41, -1));

        k5k3N.setEditable(false);
        k5k3N.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel3.add(k5k3N, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 203, 41, -1));

        k5k4N.setEditable(false);
        k5k4N.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel3.add(k5k4N, new org.netbeans.lib.awtextra.AbsoluteConstraints(213, 203, 41, -1));

        k5k5N.setEditable(false);
        k5k5N.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel3.add(k5k5N, new org.netbeans.lib.awtextra.AbsoluteConstraints(266, 203, 41, -1));

        Prior5.setEditable(false);
        Prior5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel3.add(Prior5, new org.netbeans.lib.awtextra.AbsoluteConstraints(331, 203, 41, -1));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(412, 218, 380, 240));

        jLabel22.setText("Total Penilaian Calon Penerima");
        jPanel1.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 115, -1, 28));

        TotalNilai.setEditable(false);
        jPanel1.add(TotalNilai, new org.netbeans.lib.awtextra.AbsoluteConstraints(222, 118, 156, -1));

        Simpan.setBackground(new java.awt.Color(255, 0, 0));
        Simpan.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        Simpan.setForeground(new java.awt.Color(255, 255, 255));
        Simpan.setText("Simpan Data");
        Simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SimpanActionPerformed(evt);
            }
        });
        jPanel1.add(Simpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(477, 155, 153, 35));

        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel23.setText("Prioritas SubKriteria Sesuai Kriteria");
        jPanel4.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 6, -1, -1));

        jLabel28.setText("K1");
        jPanel4.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(68, 64, -1, -1));

        jLabel29.setText("K2");
        jPanel4.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(186, 63, -1, -1));

        jLabel30.setText("K3");
        jPanel4.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(313, 63, -1, -1));

        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel31.setText("K4");
        jPanel4.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(399, 64, 76, -1));

        jLabel21.setText("Prioritas Sub-Kriteria");
        jPanel4.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 42, -1, -1));

        PriorS11.setEditable(false);
        PriorS11.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel4.add(PriorS11, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 92, 102, -1));

        PriorS12.setEditable(false);
        PriorS12.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel4.add(PriorS12, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 126, 102, -1));

        PriorS13.setEditable(false);
        PriorS13.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel4.add(PriorS13, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 160, 102, -1));

        PriorS14.setEditable(false);
        PriorS14.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel4.add(PriorS14, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 194, 102, -1));

        jLabel24.setText("Prioritas Sub-Kriteria");
        jPanel4.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(145, 42, -1, -1));

        PriorS21.setEditable(false);
        PriorS21.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel4.add(PriorS21, new org.netbeans.lib.awtextra.AbsoluteConstraints(145, 91, 102, -1));

        PriorS22.setEditable(false);
        PriorS22.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel4.add(PriorS22, new org.netbeans.lib.awtextra.AbsoluteConstraints(145, 125, 102, -1));

        PriorS23.setEditable(false);
        PriorS23.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel4.add(PriorS23, new org.netbeans.lib.awtextra.AbsoluteConstraints(145, 159, 102, -1));

        PriorS24.setEditable(false);
        PriorS24.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel4.add(PriorS24, new org.netbeans.lib.awtextra.AbsoluteConstraints(145, 193, 102, -1));

        jLabel25.setText("Prioritas Sub-Kriteria");
        jPanel4.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(265, 42, -1, -1));

        PriorS31.setEditable(false);
        PriorS31.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel4.add(PriorS31, new org.netbeans.lib.awtextra.AbsoluteConstraints(265, 91, 102, -1));

        PriorS32.setEditable(false);
        PriorS32.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel4.add(PriorS32, new org.netbeans.lib.awtextra.AbsoluteConstraints(265, 125, 102, -1));

        PriorS33.setEditable(false);
        PriorS33.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel4.add(PriorS33, new org.netbeans.lib.awtextra.AbsoluteConstraints(265, 159, 102, -1));

        PriorS34.setEditable(false);
        PriorS34.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel4.add(PriorS34, new org.netbeans.lib.awtextra.AbsoluteConstraints(265, 193, 102, -1));

        jLabel26.setText("Prioritas Sub-Kriteria");
        jPanel4.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(385, 42, -1, -1));

        PriorS41.setEditable(false);
        PriorS41.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel4.add(PriorS41, new org.netbeans.lib.awtextra.AbsoluteConstraints(385, 92, 102, -1));

        PriorS42.setEditable(false);
        PriorS42.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel4.add(PriorS42, new org.netbeans.lib.awtextra.AbsoluteConstraints(385, 126, 102, -1));

        PriorS43.setEditable(false);
        PriorS43.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel4.add(PriorS43, new org.netbeans.lib.awtextra.AbsoluteConstraints(385, 160, 102, -1));

        PriorS44.setEditable(false);
        PriorS44.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel4.add(PriorS44, new org.netbeans.lib.awtextra.AbsoluteConstraints(385, 194, 102, -1));

        jLabel36.setText("Prioritas Sub-Kriteria");
        jPanel4.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(511, 44, -1, -1));

        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setText("K5");
        jPanel4.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(525, 66, 76, -1));

        PriorS51.setEditable(false);
        PriorS51.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel4.add(PriorS51, new org.netbeans.lib.awtextra.AbsoluteConstraints(511, 94, 108, 20));

        PriorS52.setEditable(false);
        PriorS52.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel4.add(PriorS52, new org.netbeans.lib.awtextra.AbsoluteConstraints(511, 126, 108, -1));

        PriorS53.setEditable(false);
        PriorS53.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel4.add(PriorS53, new org.netbeans.lib.awtextra.AbsoluteConstraints(511, 160, 108, -1));

        PriorS54.setEditable(false);
        PriorS54.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel4.add(PriorS54, new org.netbeans.lib.awtextra.AbsoluteConstraints(511, 194, 108, -1));

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 490, 650, 230));

        jLabel27.setText("Nama Layanan Internet");
        jPanel1.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 75, -1, 28));

        namaLayananInternet.setEditable(false);
        jPanel1.add(namaLayananInternet, new org.netbeans.lib.awtextra.AbsoluteConstraints(222, 78, 156, -1));

        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout PanelPerhitunganLayout = new javax.swing.GroupLayout(PanelPerhitungan);
        PanelPerhitungan.setLayout(PanelPerhitunganLayout);
        PanelPerhitunganLayout.setHorizontalGroup(
            PanelPerhitunganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(judul, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 867, Short.MAX_VALUE)
        );
        PanelPerhitunganLayout.setVerticalGroup(
            PanelPerhitunganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelPerhitunganLayout.createSequentialGroup()
                .addComponent(judul, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 457, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout PaneLayout = new javax.swing.GroupLayout(Pane);
        Pane.setLayout(PaneLayout);
        PaneLayout.setHorizontalGroup(
            PaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 872, Short.MAX_VALUE)
        );
        PaneLayout.setVerticalGroup(
            PaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 569, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Pane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Pane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(888, 577));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    //tombol mulai perhitungan
    private void mulaiHitungActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mulaiHitungActionPerformed
         totalNilai = 0;      // ✅ reset di awal
    K.clear();           // ✅ penting biar tidak dobel isi

    getMatriksK();
    getMatriksNorK();
    getPrioritasSub();
    getAlternatif();
    getPenilaian();
    }//GEN-LAST:event_mulaiHitungActionPerformed
    

    private void cbIdLayananInternetItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbIdLayananInternetItemStateChanged
 if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {

        String sql = "SELECT nama_layanan FROM layanan_internet WHERE no_id=?";

        try {
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setString(1, cbIdLayananInternet.getSelectedItem().toString());

            ResultSet hasil = stat.executeQuery();
            if (hasil.next()) {
                String nama = hasil.getString("nama_layanan");
                namaLayananInternet.setText(nama);
            } else {
                namaLayananInternet.setText("");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    }//GEN-LAST:event_cbIdLayananInternetItemStateChanged
    //simpan data
    private void SimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SimpanActionPerformed
 String sql = "INSERT INTO seleksi (no_id, nama, hasil_penilaian) VALUES (?,?,?)";

    try {
        PreparedStatement stat = conn.prepareStatement(sql);

        stat.setString(1, noIdAlternatif);        // id layanan
        stat.setString(2, namaAlternatif);        // nama layanan
        stat.setBigDecimal(3, new java.math.BigDecimal(TotalNilai.getText())); // decimal(4,2)

        stat.executeUpdate();

        JOptionPane.showMessageDialog(null, "DATA Berhasil Disimpan");
        kosong();

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Data Gagal Disimpan: " + e.getMessage());
    }
    }//GEN-LAST:event_SimpanActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DialogPerhitunganAHP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DialogPerhitunganAHP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DialogPerhitunganAHP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DialogPerhitunganAHP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DialogPerhitunganAHP dialog = new DialogPerhitunganAHP(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Pane;
    private javax.swing.JPanel PanelPerhitungan;
    private javax.swing.JTextField Prior1;
    private javax.swing.JTextField Prior2;
    private javax.swing.JTextField Prior3;
    private javax.swing.JTextField Prior4;
    private javax.swing.JTextField Prior5;
    private javax.swing.JTextField PriorS11;
    private javax.swing.JTextField PriorS12;
    private javax.swing.JTextField PriorS13;
    private javax.swing.JTextField PriorS14;
    private javax.swing.JTextField PriorS21;
    private javax.swing.JTextField PriorS22;
    private javax.swing.JTextField PriorS23;
    private javax.swing.JTextField PriorS24;
    private javax.swing.JTextField PriorS31;
    private javax.swing.JTextField PriorS32;
    private javax.swing.JTextField PriorS33;
    private javax.swing.JTextField PriorS34;
    private javax.swing.JTextField PriorS41;
    private javax.swing.JTextField PriorS42;
    private javax.swing.JTextField PriorS43;
    private javax.swing.JTextField PriorS44;
    private javax.swing.JTextField PriorS51;
    private javax.swing.JTextField PriorS52;
    private javax.swing.JTextField PriorS53;
    private javax.swing.JTextField PriorS54;
    private javax.swing.JButton Simpan;
    private javax.swing.JTextField TotalNilai;
    private javax.swing.ButtonGroup btnG;
    private javax.swing.JComboBox<String> cbIdLayananInternet;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel judul;
    private javax.swing.JTextField k1k1;
    private javax.swing.JTextField k1k1N;
    private javax.swing.JTextField k1k2;
    private javax.swing.JTextField k1k2N;
    private javax.swing.JTextField k1k3;
    private javax.swing.JTextField k1k3N;
    private javax.swing.JTextField k1k4;
    private javax.swing.JTextField k1k4N;
    private javax.swing.JTextField k1k5;
    private javax.swing.JTextField k1k5N;
    private javax.swing.JTextField k2k1;
    private javax.swing.JTextField k2k1N;
    private javax.swing.JTextField k2k2;
    private javax.swing.JTextField k2k2N;
    private javax.swing.JTextField k2k3;
    private javax.swing.JTextField k2k3N;
    private javax.swing.JTextField k2k4;
    private javax.swing.JTextField k2k4N;
    private javax.swing.JTextField k2k5;
    private javax.swing.JTextField k2k5N;
    private javax.swing.JTextField k3k1;
    private javax.swing.JTextField k3k1N;
    private javax.swing.JTextField k3k2;
    private javax.swing.JTextField k3k2N;
    private javax.swing.JTextField k3k3;
    private javax.swing.JTextField k3k3N;
    private javax.swing.JTextField k3k4;
    private javax.swing.JTextField k3k4N;
    private javax.swing.JTextField k3k5;
    private javax.swing.JTextField k3k5N;
    private javax.swing.JTextField k4k1;
    private javax.swing.JTextField k4k1N;
    private javax.swing.JTextField k4k2;
    private javax.swing.JTextField k4k2N;
    private javax.swing.JTextField k4k3;
    private javax.swing.JTextField k4k3N;
    private javax.swing.JTextField k4k4;
    private javax.swing.JTextField k4k4N;
    private javax.swing.JTextField k4k5;
    private javax.swing.JTextField k4k5N;
    private javax.swing.JTextField k5k1;
    private javax.swing.JTextField k5k1N;
    private javax.swing.JTextField k5k2;
    private javax.swing.JTextField k5k2N;
    private javax.swing.JTextField k5k3;
    private javax.swing.JTextField k5k3N;
    private javax.swing.JTextField k5k4;
    private javax.swing.JTextField k5k4N;
    private javax.swing.JTextField k5k5;
    private javax.swing.JTextField k5k5N;
    private javax.swing.JButton mulaiHitung;
    private javax.swing.JTextField namaLayananInternet;
    // End of variables declaration//GEN-END:variables

    void show(JRootPane rootPane) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
