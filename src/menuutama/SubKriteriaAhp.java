package menuutama;

import java.text.DecimalFormat;

public class SubKriteriaAhp {

    protected static int nBanyak4x4 = 4, nBanyak3x3 = 3, nBanyak2x2 = 2;

    protected static double RI[] = {0.0, 0.0, 0.58, 0.90, 1.12, 1.24, 1.32, 1.41, 1.45, 1.49, 1.51, 1.48, 1.56, 1.57, 1.59};

    static DecimalFormat df = new DecimalFormat("#.##");

    // ===== OUTPUT FINAL per-kriteria (dipakai UI & penilaian) =====
    // 2x2
    public static double[] prioritasSubFiber2x2 = new double[nBanyak2x2];   // [0]=Belum Fiber, [1]=Sudah Fiber
    public static double[] prioritasSubFUP2x2   = new double[nBanyak2x2];   // [0]=Tidak Ada, [1]=Ada (kalau kamu anggap "Tidak Ada" lebih baik)

    // 3x3
    public static double[] prioritasSubHarga3x3     = new double[nBanyak3x3]; // [0]=<250, [1]=250-500, [2]=>500
    public static double[] prioritasSubBandwidth3x3 = new double[nBanyak3x3]; // [0]=30, [1]=<100, [2]=>100

    // 4x4
    public static double[] prioritasSubLayananTambahan4x4 = new double[nBanyak4x4];
    // urutan:
    // [0]=Internet saja
    // [1]=Internet + TV BOX
    // [2]=Internet + TV BOX + Cloud Storage
    // [3]=Internet + TV BOX + Cloud Storage + Roter Mesh (Extender)

    // ===== UTIL: Random Index =====
    static double IR;
    static double getIR(int nBanyak) {
        if (nBanyak == 1 || nBanyak == 2) return RI[0];
        if (nBanyak == 3) return RI[2];
        if (nBanyak == 4) return RI[3];
        if (nBanyak == 5) return RI[4];
        if (nBanyak == 6) return RI[5];
        if (nBanyak == 7) return RI[6];
        if (nBanyak == 8) return RI[7];
        if (nBanyak == 9) return RI[8];
        return RI[9];
    }

    // ===== GENERIC AHP SOLVER (2x2 / 3x3 / 4x4) =====
    private static double[] solvePrioritasSub(double[][] A) {
        int n = A.length;

        double[] colSum = new double[n];
        double[][] norm = new double[n][n];
        double[] rowSum = new double[n];
        double[] w = new double[n];

        // sum kolom
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                colSum[c] += A[r][c];
            }
        }

        // normalisasi + prioritas (rata-rata baris)
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                norm[r][c] = A[r][c] / colSum[c];
                rowSum[r] += norm[r][c];
            }
            w[r] = rowSum[r] / n;
        }

        // normalisasi ke 0..1 (dibagi max) seperti gaya kamu
        double max = w[0];
        for (double v : w) if (v > max) max = v;

        double[] sub = new double[n];
        for (int i = 0; i < n; i++) sub[i] = w[i] / max;

        return sub;
    }

    // ==========================================================
    //  MATRKS PER-KRITERIA (Internet) - SESUAI URUTAN SUB DI UI
    // ==========================================================

    // 2x2 STATUS FIBER
    // [0]=Belum Fiber, [1]=Sudah Fiber (Sudah Fiber lebih baik)
    private static double[][] matriksFiber2x2() {
        return new double[][]{
            {1, 1.0/5.0},
            {5.0, 1}
        };
    }

    // 3x3 HARGA (lebih murah lebih baik)
    // [0]=<250 (terbaik), [1]=250-500, [2]=>500 (terendah)
    private static double[][] matriksHarga3x3() {
        return new double[][]{
            {1, 3.0, 7.0},
            {1.0/3.0, 1, 3.0},
            {1.0/7.0, 1.0/3.0, 1}
        };
    }

    // 2x2 FUP
    // Jika kamu anggap "Tidak Ada FUP" lebih baik (lebih bebas):
    // [0]=Tidak Ada (lebih baik), [1]=Ada (kurang)
    private static double[][] matriksFUP2x2() {
        return new double[][]{
            {1, 5.0},
            {1.0/5.0, 1}
        };
    }

    // 3x3 BANDWIDTH (lebih besar lebih baik)
    // [0]=30, [1]=<100, [2]=>100 (terbaik)
    private static double[][] matriksBandwidth3x3() {
        return new double[][]{
            {1, 1.0/3.0, 1.0/7.0},
            {3.0, 1, 1.0/3.0},
            {7.0, 3.0, 1}
        };
    }

    // 4x4 LAYANAN TAMBAHAN (semakin lengkap semakin baik)
    // [0]=Internet saja (terendah)
    // [1]=+ TV BOX
    // [2]=+ TV BOX + Cloud Storage
    // [3]=+ TV BOX + Cloud Storage + Roter Mesh (Extender) (terbaik)
    private static double[][] matriksLayananTambahan4x4() {
        return new double[][]{
            {1, 1.0/3.0, 1.0/5.0, 1.0/9.0},
            {3.0, 1, 1.0/3.0, 1.0/5.0},
            {5.0, 3.0, 1, 1.0/3.0},
            {9.0, 5.0, 3.0, 1}
        };
    }

    // ====== HITUNG SEMUA PRIORITAS SUB ======
    private static void hitungSemua() {
        prioritasSubFiber2x2 = solvePrioritasSub(matriksFiber2x2());
        prioritasSubHarga3x3 = solvePrioritasSub(matriksHarga3x3());
        prioritasSubFUP2x2 = solvePrioritasSub(matriksFUP2x2());
        prioritasSubBandwidth3x3 = solvePrioritasSub(matriksBandwidth3x3());
        prioritasSubLayananTambahan4x4 = solvePrioritasSub(matriksLayananTambahan4x4());
    }

    public SubKriteriaAhp() {
        hitungSemua();
    }
}
