namespace lab3 {
    public class T3 {
        private Resources resources;

        private int begin;
        private int end;
        
        public T3(Resources resources) {
            this.resources = resources;

            begin = 2 * resources.h;
            end = begin + resources.h;
        }

        public void Run() {
            Console.WriteLine("Thread T3 started");
            try {
                // Wait for T1 and T4 to input their data
                Program.S1.WaitOne();
                Program.S2.WaitOne();

                // calculation 1: ai = min(Zh)
                int ai = Data.FindMinimum(resources.Z, begin, end);
                // calculation 2 and CS1: a = min(a, ai)
                Interlocked.Exchange(ref resources.a, Math.Min(resources.a, ai));

                // Signal to threads T1, T2, T4 about calculating a
                Program.E3.Set();
                // Waiting for signal from T1
                Program.E1.WaitOne();
                // Waiting for signal from T2
                Program.E2.WaitOne();
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
                //Data.PrintMatrix(Data.MatrixMultiply(resources.MC, resources.MM, begin, end));
                int[,] MOh = Data.MatrixAddition(Data.MatrixScalarMultiply(Data.MatrixMultiply(Data.MatrixMultiply(resources.MC, resources.MM, begin, end), resources.MB), d), Data.MatrixScalarMultiply(resources.MC, a, begin, end));
                Data.MakeFinalMatrix(resources.MO, MOh, begin, end);

                // Waiting for T1, T2, T4 to calculate MOh
                Program.B1.SignalAndWait();
            } catch (ThreadInterruptedException) {
                Console.WriteLine("Thread T1 interrupted");
                Thread.CurrentThread.Interrupt();
            }
        }
    }
}