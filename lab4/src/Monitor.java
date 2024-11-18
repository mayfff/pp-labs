public class Monitor {
    private final Resources resources;

    private int inputCompletedCounter;
    private int calculationBCompletedCounter;
    private int finalCalculationCompletedCounter;

    private boolean T1SortCompleted;
    private boolean T2SortCompleted;
    private boolean T3SortCompleted;
    private boolean finalSortCompleted;

    public Monitor(Resources resources) {
        this.resources = resources;

        inputCompletedCounter = 0;
        calculationBCompletedCounter = 0;
        finalCalculationCompletedCounter = 0;

        T1SortCompleted = false;
        T2SortCompleted = false;
        T3SortCompleted = false;
        finalSortCompleted = false;
    }

    public synchronized void addToScalarB(int value) {
        resources.b += value;
    }

    public synchronized int copyScalarB() {
        return resources.b;
    }

    public synchronized int copyScalarP() {
        return resources.p;
    }

    public synchronized int copyScalarD() {
        return resources.d;
    }

    public synchronized void signalOthersAboutInputCompleted() {
        inputCompletedCounter++;
        if (inputCompletedCounter == resources.NUMBER_OF_THREADS - 1) {
            System.out.println("All data has been inputted successfully");
            notifyAll();
        }
    }

    public synchronized void waitForOthersToCompleteInput() throws InterruptedException {
        if (inputCompletedCounter < resources.NUMBER_OF_THREADS - 1) {
            wait();
        }
    }

    public synchronized void signalOthersAboutCalculationB() {
        calculationBCompletedCounter++;
        if (calculationBCompletedCounter == resources.NUMBER_OF_THREADS) {
            notifyAll();
        }
    }

    public synchronized void waitForOthersToCompleteCalculationB() throws InterruptedException {
        if (calculationBCompletedCounter < resources.NUMBER_OF_THREADS) {
            wait();
        }
    }

    public synchronized void signalT2AboutSortingInT1() {
        T1SortCompleted = true;
        notifyAll();
    }

    public synchronized void waitForT1ToCompleteSorting() throws InterruptedException {
        while (!T1SortCompleted) {
            wait();
        }
    }

    public synchronized void signalT4AboutSortingInT3() {
        T3SortCompleted = true;
        notifyAll();
    }

    public synchronized void waitForT3ToCompleteSorting() throws InterruptedException {
        while (!T3SortCompleted) {
            wait();
        }
    }

    public synchronized void signalT4AboutSortingInT2() {
        T2SortCompleted = true;
    }

    public synchronized void waitForT2ToCompleteSorting() throws InterruptedException {
        while (!T2SortCompleted) {
            wait();
        }
    }

    public synchronized void signalFinalSortCompleted() {
        finalSortCompleted = true;
        notifyAll();
    }

    public synchronized void waitForFinalSort() throws InterruptedException {
        while (!finalSortCompleted) {
            wait();
        }
    }

    public synchronized void signalT4AboutFinalCalculation() {
        finalCalculationCompletedCounter++;
        if (finalCalculationCompletedCounter == resources.NUMBER_OF_THREADS - 1) {
            notifyAll();
        }
    }

    public synchronized void waitForOthersToCompleteFinalCalculation() throws InterruptedException {
        if (finalCalculationCompletedCounter < resources.NUMBER_OF_THREADS - 1) {
            wait();
        }
    }
}
