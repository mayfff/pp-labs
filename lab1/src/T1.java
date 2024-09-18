import java.util.Scanner;

public class T1 extends Thread {
    String name;
    final int N;
    Data helper;

    public T1(String name, int N, int priority) {
        this.name = name;
        this.N = N;
        setPriority(priority);
        helper = new Data();
    }

    @Override
    public void run() {
        System.out.printf("%s is running.\n", name);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.out.printf("%s is interrupted.\n", name);
        }

        int[][] MA = new int[N][N], MB = new int[N][N];
        int[] A = new int[N], B = new int[N];

        synchronized (System.in) {
            if (N == 3) {
                MA = helper.fillMatrixHandle(3, "MA", name);
                MB = helper.fillMatrixHandle(3, "MB", name);
                A = helper.fillVectorHandle(3, "A", name);
                B = helper.fillVectorHandle(3, "B", name);
            } else {
                Scanner in = new Scanner(System.in);
                System.out.print("Select fill Method:\n1 - random filling\n2 - filling with single element\nYour choice: ");
                switch (in.nextInt()) {
                    case 1:
                        MA = helper.fillMatrixRandom(N);
                        MB = helper.fillMatrixRandom(N);
                        A = helper.fillVectorRandom(N);
                        B = helper.fillVectorRandom(N);
                        break;
                    case 2:
                        System.out.print("Enter value to fill elements: ");
                        int value = in.nextInt();
                        MA = helper.fillMatrixValue(N, value);
                        MB = helper.fillMatrixValue(N, value);
                        A = helper.fillVectorValue(N, value);
                        B = helper.fillVectorValue(N, value);
                        break;
                }
            }
        }

        int result = helper.findMatrixMax(helper.matrixMultiply(MA, MB)) * helper.vectorMultiply(A, B);
        if (N < 10) {
            System.out.printf("Thread %s. Result = %d\n", name, result);
        }
    }
}
