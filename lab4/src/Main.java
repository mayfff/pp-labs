/* ------------------------------------------------------------
-- Паралельне програмування                                  --
--                                                           --
-- Лабораторна робота №4                                     --
--                                                           --
-- Варіант: A = p*(sort(d*B + Z*MM) * (MX*MT)) + (B*Z)*Z     --
--                                                           --
-- Виконав: Закревський Данило                               --
-- Група: ІО-24                                              --
-- Дата: 19.11.2024                                          --
------------------------------------------------------------ */

import java.util.Scanner;

public class Main {
    public static final int NUMBER_OF_THREADS = 4;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter the size of vectors and matrices: ");
        int N = in.nextInt();

        Resources resources = new Resources(NUMBER_OF_THREADS, N);
        Monitor monitor = new Monitor(resources);

        T1 t1 = new T1(resources, monitor);
        T2 t2 = new T2(resources, monitor);
        T3 t3 = new T3(resources, monitor);
        T4 t4 = new T4(resources, monitor);

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
        } catch (InterruptedException e) {
            System.out.println("Main thread has been interrupted");
            Thread.currentThread().interrupt();
        }

        long end = System.currentTimeMillis();

        System.out.printf("The time of execution: %d ms\n", end - start);
    }
}