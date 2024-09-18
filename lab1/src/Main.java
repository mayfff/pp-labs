/* -----------------------------------------------------
-- Паралельне програмування                           --
--                                                    --
-- Лабораторна робота №1                              --
--                                                    --
-- Варіант:                                           --
-- F1 -> c = MAX(MA*MB)*(A*B) (1.11)                  --
-- F2 -> ML = SORT(MF + MG*MH) (2.14)                 --
-- F3 -> s = MIN(O*TRANS(MP*MM)) + (R*SORT(S)) (3.17) --
--                                                    --
-- Виконав:                                           --
-- Закревський Данило ІО-24                           --
----------------------------------------------------- */

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter N value: ");
        final int N = in.nextInt();

        T1 t1 = new T1("T1", N, Thread.MAX_PRIORITY);
        T2 t2 = new T2("T2", N, Thread.MIN_PRIORITY);
        T3 t3 = new T3("T3", N, Thread.NORM_PRIORITY);

        t1.start();
        t2.start();
        t3.start();

        long start = System.currentTimeMillis();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }

        long end = System.currentTimeMillis();
        System.out.println("Time taken: " + (end - start) + " ms.");
        in.close();
    }
}