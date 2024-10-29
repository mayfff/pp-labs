import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;

public class T2 extends Thread {
    private final Resources resources;

    private final int begin;
    private final int end;

    public T2(Resources resources) {
        setName("T2");
        this.resources = resources;

        begin = resources.h;
        end = begin + resources.h;
    }

    @Override
    public void run() {
        System.out.println("Thread " + getName() + " started");
        try {
            Thread.sleep(200);
            Main.CS1.lock(); // input B and MA
            Scanner in = new Scanner(System.in);
            System.out.printf("%s. Enter value to fill vector B: ", getName());
            resources.B = Data.fillVectorValue(resources.n, in.nextInt());

            System.out.printf("%s. Enter value to fill matrix MA: ", getName());
            resources.MA = Data.fillMatrixValue(resources.n, in.nextInt());
            Main.CS1.unlock();
            Main.B1.await(); // wait for other threads to input their data

            // calculate ah
            int a2 = Data.vectorMultiply(resources.B, resources.C, begin, end);
            resources.a.addAndGet(a2);

            // get shared Matrix MB
            Main.S1.acquire();
            int[][] MB2 = resources.MB;
            Main.S1.release();

            // calculate MTh
            int[][] MT2 = Data.matrixMultiply(resources.MA, MB2, begin ,end);
            Data.makeMatrix(resources.MT, MT2, begin, end);

            // Signal to threads T1 T3 T4 about calculating MTh
            Main.S3.release(3);
            // Waiting for signal from T1
            Main.S2.acquire();
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

            // Signal to thread T1 about calculating Zh
            Main.B2.await();


        } catch (InterruptedException | BrokenBarrierException ex) {
            System.out.printf("%s is interrupted.\n", getName());
            Thread.currentThread().interrupt();
        }
    }
}
