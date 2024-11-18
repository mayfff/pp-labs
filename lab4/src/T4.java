public class T4 extends Thread {
    Resources resources;
    Monitor monitor;

    private final int begin;
    private final int end;

    public T4(Resources resources, Monitor monitor) {
        this.resources = resources;
        this.monitor = monitor;

        begin = 3 * resources.h;
        end = begin + resources.h;

        setName("T4");
    }

    @Override
    public void run() {
        System.out.printf("Thread %s has started\n", getName());
        try {
            resources.A = Data.fillVectorValue(resources.n, 1);
            resources.Z = Data.fillVectorValue(resources.n, 1);
            resources.MT = Data.fillMatrixValue(resources.n, 1);
            resources.d = 1;

            // Очікування завершення заповнення в інших потоках
            monitor.signalOthersAboutInputCompleted();
            monitor.waitForOthersToCompleteInput();

            // Обчислення 1: bi = Bh * Zh
            int b4 = Data.vectorMultiply(resources.B, resources.Z, begin, end);
            // Обчислення 2 & КД1: b = b + bi
            monitor.addToScalarB(b4);

            // Очікування завершення обчислення bi в інших потоках
            monitor.signalOthersAboutCalculationB();
            monitor.waitForOthersToCompleteCalculationB();

            // КД2: копіювання b
            int b = monitor.copyScalarB();

            // Обчислення 3: Ch = b * Zh
            int[] C4 = Data.vectorScalarMultiply(resources.Z, b, begin, end);

            // Обчислення 4: MAh = MXh * MT
            int[][] MA4 = Data.matrixMultiply(resources.MX, resources.MT, begin, end);
            Data.makeMatrix(resources.MA, MA4, begin, end);

            // Обчислення 5: Dh = d * Bh + Z * MMh
            int[] D4 = Data.addVectors(Data.vectorScalarMultiply(resources.B, resources.d, begin, end),
                    Data.vectorMatrixMultiply(resources.Z, resources.MM, begin, end));

            // Обчислення 6: Kh = sort(Dh) та очікування сортування в Т3
            int[] K4 = Data.sortVector(D4);
            monitor.waitForT3ToCompleteSorting();

            // Обчислення 7: K2h = sort(Kh, Kh) та очікування сортування K2h в Т2
            int[] K2h = Data.concatAndSort(resources.T3Kh, K4);
            monitor.waitForT2ToCompleteSorting();

            // Обчислення 8 та сигнал іншим потокам про кінцеве сортування
            int[] K = Data.concatAndSort(resources.T2K2h, K2h);
            resources.K = K;
            monitor.signalFinalSortCompleted();

            // Обчислення 9: Xh = K * MAh
            int[] X4 = Data.vectorMatrixMultiply(resources.K, resources.MA, begin, end);

            // Обчислення 10: Ah = p * Xh + Ch
            int[] A4 = Data.addVectors(Data.vectorScalarMultiply(X4, resources.p), C4);
            Data.makeFinalVector(resources.A, A4, begin, end);

            // Очікування завершення обчислення Аh в інших потоках та друк результату
            monitor.waitForOthersToCompleteFinalCalculation();
            Data.printVector(resources.A);
            System.out.println();

        } catch (InterruptedException e) {
            System.out.printf("%s has been interrupted\n", getName());
            Thread.currentThread().interrupt();
        }
    }
}
