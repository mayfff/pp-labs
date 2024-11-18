public class T2 extends Thread {
    Resources resources;
    Monitor monitor;

    private final int begin;
    private final int end;

    public T2(Resources resources, Monitor monitor) {
        this.resources = resources;
        this.monitor = monitor;

        begin = resources.h;
        end = begin + resources.h;

        setName("T2");
    }

    @Override
    public void run() {
        System.out.printf("Thread %s has started\n", getName());
        try {
            // Очікування завершення заповнення в інших потоках
            monitor.waitForOthersToCompleteInput();

            // Обчислення 1: bi = Bh * Zh
            int b2 = Data.vectorMultiply(resources.B, resources.Z, begin, end);
            // Обчислення 2 & КД1: b = b + bi
            monitor.addToScalarB(b2);

            // Очікування завершення обчислення bi в інших потоках
            monitor.signalOthersAboutCalculationB();
            monitor.waitForOthersToCompleteCalculationB();

            // КД2: копіювання b
            int b = monitor.copyScalarB();

            // Обчислення 3: Ch = b * Zh
            int[] C2 = Data.vectorScalarMultiply(resources.Z, b, begin, end);

            // Обчислення 4: MAh = MXh * MT
            int[][] MA2 = Data.matrixMultiply(resources.MX, resources.MT, begin, end);
            Data.makeMatrix(resources.MA, MA2, begin, end);

            // Обчислення 5: Dh = d * Bh + Z * MMh
            int[] D2 = Data.addVectors(Data.vectorScalarMultiply(resources.B, resources.d, begin, end),
                    Data.vectorMatrixMultiply(resources.Z, resources.MM, begin, end));

            // Обчислення 6: Kh = sort(Dh) та очікування сортування в Т1
            int[] K2 = Data.sortVector(D2);
            monitor.waitForT1ToCompleteSorting();

            // Обчислення 7: K2h = sort(Kh, Kh) та сигнал Т4 про сортування K2h
            int[] K2h = Data.concatAndSort(resources.T1Kh, K2);
            resources.T2K2h = K2h;
            monitor.signalT4AboutSortingInT2();

            // Очікування кінцевого сортування в Т4
            System.out.println("T2 wait for t4");
            monitor.waitForFinalSort();

            // Обчислення 9: Xh = K * MAh
            int[] X2 = Data.vectorMatrixMultiply(resources.K, resources.MA, begin, end);

            // Обчислення 10: Ah = p * Xh + Ch
            int[] A2 = Data.addVectors(Data.vectorScalarMultiply(X2, resources.p), C2);
            Data.makeFinalVector(resources.A, A2, begin, end);

            monitor.signalT4AboutFinalCalculation();

        } catch (InterruptedException e) {
            System.out.printf("%s has been interrupted\n", getName());
            Thread.currentThread().interrupt();
        }
    }
}
