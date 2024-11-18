public class T1 extends Thread {
    Resources resources;
    Monitor monitor;

    private final int begin;
    private final int end;

    public T1(Resources resources, Monitor monitor) {
        this.resources = resources;
        this.monitor = monitor;

        begin = 0;
        end = resources.h;

        setName("T1");
    }

    @Override
    public void run() {
        System.out.printf("Thread %s has started\n", getName());
        try {
            // Заповнення даних
            resources.MM = Data.fillMatrixValue(resources.n, 1);
            resources.MX = Data.fillMatrixValue(resources.n, 1);
            resources.B = Data.fillVectorValue(resources.n, 1);

            // Очікування завершення заповнення в інших потоках
            monitor.signalOthersAboutInputCompleted();
            monitor.waitForOthersToCompleteInput();

            // Обчислення 1: bi = Bh * Zh
            int b1 = Data.vectorMultiply(resources.B, resources.Z, begin, end);
            // Обчислення 2 & КД1: b = b + bi
            monitor.addToScalarB(b1);

            // Очікування завершення обчислення bi в інших потоках
            monitor.signalOthersAboutCalculationB();
            monitor.waitForOthersToCompleteCalculationB();

            // КД2: копіювання b
            int b = monitor.copyScalarB();

            // Обчислення 3: Ch = b * Zh
            int[] C1 = Data.vectorScalarMultiply(resources.Z, b, begin, end);

            // Обчислення 4: MAh = MXh * MT
            int[][] MA1 = Data.matrixMultiply(resources.MX, resources.MT, begin, end);
            Data.makeMatrix(resources.MA, MA1, begin, end);

            // Обчислення 5: Dh = d * Bh + Z * MMh
            int[] D1 = Data.addVectors(Data.vectorScalarMultiply(resources.B, resources.d, begin, end),
                                        Data.vectorMatrixMultiply(resources.Z, resources.MM, begin, end));

            // Обчислення 6: Kh = sort(Dh) та сигнал Т2 про сортування
            int[] K1 = Data.sortVector(D1);
            resources.T1Kh = K1;
            monitor.signalT2AboutSortingInT1();

            System.out.println("T1 wait for t4");
            // Очікування кінцевого сортування в Т4
            monitor.waitForFinalSort();

            // Обчислення 9: Xh = K * MAh
            int[] X1 = Data.vectorMatrixMultiply(resources.K, resources.MA, begin, end);

            // Обчислення 10: Ah = p * Xh + Ch
            int[] A1 = Data.addVectors(Data.vectorScalarMultiply(X1, resources.p), C1);
            Data.makeFinalVector(resources.A, A1, begin, end);

            monitor.signalT4AboutFinalCalculation();

        } catch (InterruptedException e) {
            System.out.printf("%s has been interrupted\n", getName());
            Thread.currentThread().interrupt();
        }
    }
}
