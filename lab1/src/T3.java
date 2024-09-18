import java.util.Scanner;

public class T3 extends Thread {
    String name;
    final int N;
    Data helper;

    public T3(String name, int N, int priority) {
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

        int[][] MP = new int[N][N], MM = new int[N][N];
        int[] O = new int[N], R = new int[N], S = new int[N];

        synchronized (System.in) {
            if (N == 3) {
                MP = helper.fillMatrixHandle(3, "MP", name);
                MM = helper.fillMatrixHandle(3, "MM", name);
                O = helper.fillVectorHandle(3, "O", name);
                R = helper.fillVectorHandle(3, "R", name);
                S = helper.fillVectorHandle(3, "S", name);
            } else {
                Scanner in = new Scanner(System.in);
                System.out.print("Select fill Method:\n1 - random filling\n2 - filling with single element\nYour choice: ");
                switch (in.nextInt()) {
                    case 1:
                        MP = helper.fillMatrixRandom(N);
                        MM = helper.fillMatrixRandom(N);
                        O = helper.fillVectorRandom(N);
                        R = helper.fillVectorRandom(N);
                        S = helper.fillVectorRandom(N);
                        break;
                    case 2:
                        System.out.print("Enter value to fill elements: ");
                        int value = in.nextInt();
                        MP = helper.fillMatrixValue(N, value);
                        MM = helper.fillMatrixValue(N, value);
                        O = helper.fillVectorValue(N, value);
                        R = helper.fillVectorValue(N, value);
                        S = helper.fillVectorValue(N, value);
                        break;
                }
            }

        }
        int result = helper.findMatrixMin(helper.matrixVectorMultiply(helper.matrixTranspose(helper.matrixMultiply(MP, MM)), O)) + helper.vectorMultiply(R, helper.sortVector(S));
        System.out.printf("Thread %s. Result = %d\n", name, result);
        System.out.printf("%s is finished.\n", name);
    }
}