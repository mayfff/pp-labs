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
            Main.CS1.lock();
            Scanner in = new Scanner(System.in);
            System.out.printf("%s. Enter value to fill vector B: ", getName());
            resources.B = Data.fillVectorValue(resources.n, in.nextInt());

            System.out.printf("%s. Enter value to fill matrix MA: ", getName());
            resources.MA = Data.fillMatrixValue(resources.n, in.nextInt());
            Main.CS1.unlock();
            Main.B.await();

        } catch (InterruptedException | BrokenBarrierException ex) {
            System.out.printf("%s is interrupted.\n", getName());
            Thread.currentThread().interrupt();
        }
    }
}
