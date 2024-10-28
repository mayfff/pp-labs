import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;

public class T4 extends Thread {
    private final Resources resources;

    private final int begin;
    private final int end;

    public T4(Resources resources) {
        setName("T4");
        this.resources = resources;

        begin = 3 * resources.h;
        end = begin + resources.h;
    }

    @Override
    public void run() {
        System.out.println("Thread " + getName() + " started");
        try {
            Main.CS1.lock();
            Scanner in = new Scanner(System.in);
            System.out.printf("%s. Enter value to fill vector D: ", getName());
            resources.D = Data.fillVectorValue(resources.n, in.nextInt());

            System.out.printf("%s. Enter value to fill matrix MB: ", getName());
            resources.MA = Data.fillMatrixValue(resources.n, in.nextInt());
            Main.CS1.unlock();
            Main.B.await();

        } catch (InterruptedException | BrokenBarrierException ex) {
            System.out.printf("%s is interrupted.\n", getName());
            Thread.currentThread().interrupt();
        }
    }
}
