
/* -----------------------------------------------------
-- Паралельне програмування                           --
--                                                    --
-- Лабораторна робота №3                              --
--                                                    --
-- Варіант: MO = MB*(MC*MM)*d + min(Z)*MC             --
--                                                    --
-- Виконав: Закревський Данило                        --
-- Група: ІО-24                                       --
-- Дата: 11.11.2024                                   --
----------------------------------------------------- */

using System.Diagnostics;

namespace lab3 {
    class Program {
        private const int p = 4;

        public static object Lock1 = new object();
        public static Mutex M1 = new Mutex();

        public static Semaphore S1 = new Semaphore(0, 3);
        public static Semaphore S2 = new Semaphore(0, 3);

        public static EventWaitHandle E1 = new EventWaitHandle(false, EventResetMode.ManualReset);
        public static EventWaitHandle E2 = new EventWaitHandle(false, EventResetMode.ManualReset);
        public static EventWaitHandle E3 = new EventWaitHandle(false, EventResetMode.ManualReset);
        public static EventWaitHandle E4 = new EventWaitHandle(false, EventResetMode.ManualReset);

        public static Barrier B1 = new Barrier(4);

        static void Main() {
            Console.Write("Enter the size of vectors and matrices: ");
            int n = Convert.ToInt32(Console.ReadLine());

            Resources resources = new(p, n);
            T1 thread1 = new T1(resources);
            T2 thread2 = new T2(resources);
            T3 thread3 = new T3(resources);
            T4 thread4 = new T4(resources);

            Thread t1 = new Thread(thread1.Run);
            Thread t2 = new Thread(thread2.Run);
            Thread t3 = new Thread(thread3.Run);
            Thread t4 = new Thread(thread4.Run);

            Stopwatch stopwatch = Stopwatch.StartNew();

            t1.Start();
            t2.Start();
            t3.Start();
            t4.Start();

            try {
                t1.Join();
                t2.Join();
                t3.Join();
                t4.Join();
            } catch (ThreadInterruptedException ex) {
                Console.WriteLine($"\nAn error occurred: {ex.Message}");
                Thread.CurrentThread.Interrupt();
            }

            stopwatch.Stop();
            Console.WriteLine($"\nTime taken: {stopwatch.ElapsedMilliseconds} ms.");
        }
    }
}
