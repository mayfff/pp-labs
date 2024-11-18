public class T3 extends Thread {
    Resources resources;
    Monitor monitor;

    private final int begin;
    private final int end;

    public T3(Resources resources, Monitor monitor) {
        this.resources = resources;
        this.monitor = monitor;

        begin = 2 * resources.h;
        end = begin + resources.h;

        setName("T3");
    }

    @Override
    public void run() {
        System.out.printf("Thread %s has started\n", getName());
        try {
            // Заповнення даних
            resources.p = 1;

            // Очікування завершення заповнення в інших потоках
            monitor.signalOthersAboutInputCompleted();
            monitor.waitForOthersToCompleteInput();

            // Обчислення 1: bi = Bh * Zh
            int b3 = Data.vectorMultiply(resources.B, resources.Z, begin, end);
            // Обчислення 2 & КД1: b = b + bi
            monitor.addToScalarB(b3);

            // Очікування завершення обчислення bi в інших потоках
            monitor.signalOthersAboutCalculationB();
            monitor.waitForOthersToCompleteCalculationB();

            // КД2: копіювання b
            int b = monitor.copyScalarB();

            // Обчислення 3: Ch = b * Zh
            int[] C3 = Data.vectorScalarMultiply(resources.Z, b, begin, end);

            // Обчислення 4: MAh = MXh * MT
            int[][] MA3 = Data.matrixMultiply(resources.MX, resources.MT, begin, end);
            Data.makeMatrix(resources.MA, MA3, begin, end);

            // Обчислення 5: Dh = d * Bh + Z * MMh
            int[] D3 = Data.addVectors(Data.vectorScalarMultiply(resources.B, resources.d, begin, end),
                    Data.vectorMatrixMultiply(resources.Z, resources.MM, begin, end));

            // Обчислення 6: Kh = sort(Dh) та сигнал Т4 про сортування
            int[] K3 = Data.sortVector(D3);
            resources.T3Kh = K3;
            monitor.signalT4AboutSortingInT3();

            // Очікування кінцевого сортування в Т4
            System.out.println("T3 wait for t4");
            monitor.waitForFinalSort();

            // Обчислення 9: Xh = K * MAh
            int[] X3 = Data.vectorMatrixMultiply(resources.K, resources.MA, begin, end);

            // Обчислення 10: Ah = p * Xh + Ch
            int[] A3 = Data.addVectors(Data.vectorScalarMultiply(X3, resources.p), C3);
            Data.makeFinalVector(resources.A, A3, begin, end);

            monitor.signalT4AboutFinalCalculation();

        } catch (InterruptedException e) {
            System.out.printf("%s has been interrupted\n", getName());
            Thread.currentThread().interrupt();
        }
    }
}
