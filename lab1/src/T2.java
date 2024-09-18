import java.util.Scanner;

public class T2 extends Thread {
    String name;
    final int N;
    Data helper;

    public T2(String name, int N, int priority) {
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

        int[][] MF = new int[N][N], MG = new int[N][N], MH = new int[N][N];

        synchronized (System.in) {
            if (N == 3) {
                MF = helper.fillMatrixHandle(3, "MF", name);
                MG = helper.fillMatrixHandle(3, "MG", name);
                MH = helper.fillMatrixHandle(3, "MH", name);
            } else {
                Scanner in = new Scanner(System.in);
                System.out.print("Select fill Method:\n1 - random filling\n2 - filling with single element\nYour choice: ");
                switch (in.nextInt()) {
                    case 1:
                        MF = helper.fillMatrixRandom(N);
                        MG = helper.fillMatrixRandom(N);
                        MH = helper.fillMatrixRandom(N);
                        break;
                    case 2:
                        System.out.print("Enter value to fill elements: ");
                        int value = in.nextInt();
                        MF = helper.fillMatrixValue(N, value);
                        MG = helper.fillMatrixValue(N, value);
                        MH = helper.fillMatrixValue(N, value);
                        break;
                }
            }
        }

        int[][] result = helper.sortMatrix(helper.addMatrix(MF, helper.matrixMultiply(MG, MH)));
        if (N < 5) {
            System.out.printf("Thread %s. Result: \n", name);
            helper.printMatrix(result);
        }
        System.out.printf("%s is finished.\n", name);
    }
}
