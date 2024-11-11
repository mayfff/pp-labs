namespace lab3 {

    public class Resources {
        public int p; // amount of processors
        public int n; // size of vectors and matrices
        public int h;

        public int[] Z;
        public int[,] MC;
        public int[,] MO;

        // shared
        public int d;
        public int a;
        public int[,] MM;
        public int[,] MB;


        public Resources(int p, int n) {
            if (n <= 0) throw new ArgumentException("Invalid size of vectors and matrices");
            if (n % p != 0) throw new ArgumentException("n must be a multiple of p");

            this.p = p; 
            this.n = n;
            h = n / p;

            Z = new int[n];
            MC = new int[n, n];
            MO = new int[n, n];

            MM = new int[n, n];
            MB = new int[n, n];
        }
    }
}