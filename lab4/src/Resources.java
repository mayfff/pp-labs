public class Resources {
    public final int NUMBER_OF_THREADS; // amount of processors
    public final int n; // size of vectors and matrices
    public final int h;

    public int[] B;
    public int[] Z;
    public int[] A;
    public int[][] MM;
    public int[][] MX;
    public int[][] MA;

    public int[] T1Kh;
    public int[] T3Kh;
    public int[] T2K2h;

    // shared
    public int p;
    public int b;
    public int d;
    public int[] K;
    public int[][] MT;

    public Resources (int NUMBER_OF_THREADS, int n) {
        if (n <= 0) throw new IllegalArgumentException("Invalid size of vectors and matrices");
        if (n % NUMBER_OF_THREADS != 0) throw new IllegalArgumentException("n must be a multiple of p");

        this.NUMBER_OF_THREADS = NUMBER_OF_THREADS;
        this.n = n;
        this.h = n / NUMBER_OF_THREADS;

        b = 0;

        B = new int[n];
        Z = new int[n];
        A = new int[n];
        K = new int[n];

        MM = new int[n][n];
        MX = new int[n][n];
        MT = new int[n][n];
        MA = new int[n][n];
    }
}