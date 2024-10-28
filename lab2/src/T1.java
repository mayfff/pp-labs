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
            Main.CS1.lock();
            Scanner in = new Scanner(System.in);
            System.out.printf("%s. Enter value of x: ", getName());
            resources.x.set(in.nextInt());

            System.out.printf("%s. Enter value to fill vector C: ", getName());
            resources.C = Data.fillVectorValue(resources.n, in.nextInt());
            Main.CS1.unlock();
            Main.B.await();

        } catch (InterruptedException | BrokenBarrierException ex) {
            System.out.printf("%s is interrupted.\n", getName());
            Thread.currentThread().interrupt();
        }
    }
}
