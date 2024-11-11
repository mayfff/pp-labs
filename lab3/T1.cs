using System.Diagnostics;

namespace lab3 {
    public class T1 {
        private Resources resources;

        private int begin;
        private int end;
        
        public T1(Resources resources) {
            this.resources = resources;

            begin = 0;
            end = begin + resources.h;
        }

        public void Run() {
            Console.WriteLine("Thread T1 started");
            try {
                // input data
                Data.FillMatrix(resources.MB, 1);
                Data.FillMatrix(resources.MC, 1);

                // Wait for T4 to input data
                Program.S1.Release(3);
                Program.S2.WaitOne();

                // calculation 1: ai = min(Zh)
                int ai = Data.FindMinimum(resources.Z, begin, end);
                // calculation 2 and CS1: a = min(a, ai)
                Interlocked.Exchange(ref resources.a, Math.Min(resources.a, ai));

                // Signal to threads T2, T3, T4 about calculating a
                Program.E1.Set();
                // Waiting for signal from T2
                Program.E2.WaitOne();
                // Waiting for signal from T3
                Program.E3.WaitOne();
                // Waiting for signal from T4
                Program.E4.WaitOne();

                // CS2: get shared d
                int d;
                lock (Program.Lock1) {
                    d = resources.d;
                }

                // CS3: get shared a
                Program.M1.WaitOne();
                int a = resources.a;
                Program.M1.ReleaseMutex();
                
                // calculation3 : MOh = MB * (MCh * MM) * d + a * MCh
                int[,] MOh = Data.MatrixAddition(Data.MatrixScalarMultiply(Data.MatrixMultiply(Data.MatrixMultiply(resources.MC, resources.MM, begin, end), resources.MB), d), Data.MatrixScalarMultiply(resources.MC, a, begin, end));
                Data.MakeFinalMatrix(resources.MO, MOh, begin, end);

                // Waiting for T2, T3, T4 to calculate MOh
                Program.B1.SignalAndWait();
            } catch (ThreadInterruptedException) {
                Console.WriteLine("Thread T1 interrupted");
                Thread.CurrentThread.Interrupt();
            }
        }
    }
}