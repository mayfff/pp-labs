/* -----------------------------------------------------
-- Паралельне програмування                           --
--                                                    --
-- Лабораторна робота №2                              --
--                                                    --
-- Варіант: Z = (B*C)*D + E*(MA*MB)*x                 --
--                                                    --
-- Виконав: Закревський Данило                        --
-- Група: ІО-24                                       --
-- Дата: 29.10.2024                                   --
----------------------------------------------------- */

import java.util.Scanner;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    private static final int NUMBER_OF_THREADS = 4;

    // Barrier for controlling input from all threads
    public static final CyclicBarrier B1 = new CyclicBarrier(NUMBER_OF_THREADS, () -> {
        System.out.println("All data have been inputted successfully");
    });

    public static final CyclicBarrier B2 = new CyclicBarrier(NUMBER_OF_THREADS);

    // Lock to control access to System.in
    public static final Lock CS1 = new ReentrantLock();
    // Lock to control access to shared x
    public static final Lock CS2 = new ReentrantLock();

    // Semaphore to control access to MB shared matrix;
    public static final Semaphore S1 = new Semaphore(1);
    // Semaphore to synchronize T2, T3, T4 with the completion of the calculation of 'm' in T1
    public static final Semaphore S2 = new Semaphore(0);
    // Semaphore to synchronize T1, T3, T4 with the completion of the calculation of 'MT' in T2
    public static final Semaphore S3 = new Semaphore(0);
    // Semaphore to synchronize T1, T2, T4 with the completion of the calculation of 'MT' in T3
    public static final Semaphore S4 = new Semaphore(0);
    // Semaphore to synchronize T1, T2, T3 with the completion of the calculation of 'MT' in T4
    public static final Semaphore S5 = new Semaphore(0);
    public static final Semaphore S6 = new Semaphore(1);

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter the size of vectors and matrices: ");
        final int n = in.nextInt();

        Resources resources = new Resources(NUMBER_OF_THREADS, n);

        T1 t1 = new T1(resources);
        T2 t2 = new T2(resources);
        T3 t3 = new T3(resources);
        T4 t4 = new T4(resources);

        long start = System.currentTimeMillis();

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
        } catch (InterruptedException ex) {
            System.out.printf("%nAn error occurred: %s%n", ex.getMessage());
            Thread.currentThread().interrupt();
        }

        long end = System.currentTimeMillis();
        System.out.println("\nTime taken: " + (end - start) + " ms.");
    }
}