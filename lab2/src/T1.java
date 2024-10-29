import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;

public class T1 extends Thread {
    private final Resources resources;

    private final int begin;
    private final int end;

    public T1(Resources resources) {
        setName("T1");
        this.resources = resources;

        begin = 0;
        end = begin + resources.h;
    }

    @Override
    public void run() {
        System.out.println("Thread " + getName() + " started");
        try {
            Thread.sleep(200);
            Main.CS1.lock(); // input x and C
            Scanner in = new Scanner(System.in);
            System.out.printf("%s. Enter value of x: ", getName());
            resources.x = in.nextInt();

            System.out.printf("%s. Enter value to fill vector C: ", getName());
            resources.C = Data.fillVectorValue(resources.n, in.nextInt());
            Main.CS1.unlock();
            Main.B1.await(); // wait for other threads to input their data

            // calculate ah
            int a1 = Data.vectorMultiply(resources.B, resources.C, begin, end);
            resources.a.addAndGet(a1);

            // get shared Matrix MB
            Main.S1.acquire();
            int[][] MB1 = resources.MB;
            Main.S1.release();

            // calculate MTh
            int[][] MT1 = Data.matrixMultiply(resources.MA, MB1, begin ,end);
            Data.makeMatrix(resources.MT, MT1, begin, end);

            // Signal to threads T2, T3, T4 about calculating MTh
            Main.S2.release(3);
            // Waiting for signal from T2
            Main.S3.acquire();
            // Waiting for signal from T3
            Main.S4.acquire();
            // Waiting for signal from T4
            Main.S5.acquire();

            // get shared x
            Main.CS2.lock();
            int xh = resources.x;
            Main.CS2.unlock();

            // get shared a
            Main.S6.acquire();
            int a = resources.a.get();
            Main.S6.release();

            // calculate Zh: Dh * a + (E * MTh) * x
            int[] Zh = Data.addVectors(Data.vectorScalarMultiply(resources.D, a, begin, end),
                                        Data.vectorScalarMultiply(Data.vectorMatrixMultiply(resources.E, resources.MT, begin, end), xh));
            Data.makeFinalVector(resources.Z, Zh, begin, end);

            // Waiting for T2, T3, T4 to calculate Zh
            Main.B2.await();

            // Output the result
            System.out.print(getName() + ". Vector Z: ");
            Data.printVector(resources.Z);
        } catch (InterruptedException | BrokenBarrierException ex) {
            System.out.printf("%s is interrupted.\n", getName());
            Thread.currentThread().interrupt();
        }
    }
}
