import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Data {
    public int[][] fillMatrixHandle(int N, String matrixName, String threadName) {
        Scanner in = new Scanner(System.in);
        System.out.printf("Fill matrix %s in Thread %s:\n", matrixName, threadName);
        int[][] matrix = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                matrix[i][j] = in.nextInt();
            }
        }
        return matrix;
    }

    public int[][] fillMatrixRandom(int N) {
        int[][] matrix = new int[N][N];
        Random random = new Random();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                matrix[i][j] = random.nextInt(100);
            }
        }
        return matrix;
    }

    public int[][] fillMatrixValue(int N, int value) {
        int[][] matrix = new int[N][N];
        for (int i = 0; i < N; i++) {
            Arrays.fill(matrix[i], value);
        }
        return matrix;
    }

    public int[] fillVectorHandle(int N, String vectorName, String threadName) {
        Scanner in = new Scanner(System.in);
        System.out.printf("Fill vector %s in Thread %s:\n", vectorName, threadName);
        int[] vector = new int[N];
        for (int i = 0; i < N; i++) {
            vector[i] = in.nextInt();
        }
        return vector;
    }

    public int[] fillVectorRandom(int N) {
        int[] vector = new int[N];
        Random random = new Random();
        for (int i = 0; i < N; i++) {
            vector[i] = random.nextInt(100);
        }
        return vector;
    }

    public int[] fillVectorValue(int N, int value) {
        int[] vector = new int[N];
        Arrays.fill(vector, value);
        return vector;
    }

    public int[][] matrixTranspose(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = i + 1; j < matrix.length; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }
        return matrix;
    }

    public int[][] matrixMultiply(int[][] matrix1, int[][] matrix2) {
        int[][] result = new int[matrix1.length][matrix1.length];
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix1.length; j++) {
                for (int k = 0; k < matrix1.length; k++) {
                    result[i][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }
        return result;
    }

    public int[][] matrixVectorMultiply(int[][] matrix, int[] vector) {
        int[][] result = new int[matrix.length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                for (int k = 0; k < matrix.length; k++) {
                    result[i][j] += matrix[i][k] * vector[k];
                }
            }
        }
        return result;
    }

    public int[][] matrixScalarMultiply(int[][] matrix, int scalarValue) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                matrix[i][j] *= scalarValue;
            }
        }
        return matrix;
    }

    public int vectorMultiply(int[] vector1, int[] vector2) {
        int sum = 0;
        for (int i = 0; i < vector1.length; i++) {
            sum += vector1[i] * vector2[i];
        }
        return sum;
    }

    public int[] vectorScalarMultiply(int[] vector, int scalarValue) {
        for (int i = 0; i < vector.length; i++) {
            vector[i] *= scalarValue;
        }
        return vector;
    }

    public int[][] sortMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            Arrays.sort(row);
        }
        return matrix;
    }

    public int[] sortVector(int[] vector) {
        Arrays.sort(vector);
        return vector;
    }

    public int[] addVectors(int[] vector1, int[] vector2) {
        int[] result = new int[vector1.length];
        for (int i = 0; i < vector1.length; i++) {
            result[i] = vector1[i] + vector2[i];
        }
        return result;
    }

    public int[][] addMatrix(int[][] matrix1, int[][] matrix2) {
        int[][] result = new int[matrix1.length][matrix1.length];
        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix1.length; j++) {
                result[i][j] = matrix1[i][j] + matrix2[i][j];
            }
        }
        return result;
    }

    public int findMatrixMax(int[][] matrix) {
        int max = 0;
        for (int[] row : matrix) {
            for (int j = 0; j < matrix.length; j++) {
                max = Math.max(max, row[j]);
            }
        }
        return max;
    }

    public int findMatrixMin(int[][] matrix) {
        int min = Integer.MAX_VALUE;
        for (int[] row : matrix) {
            for (int j = 0; j < matrix.length; j++) {
                min = Math.min(min, row[j]);
            }
        }
        return min;
    }

    public int findVectorMax(int[] vector) {
        int max = 0;
        for (int num : vector) {
            max = Math.max(max, num);
        }
        return max;
    }

    public void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int j = 0; j < matrix.length; j++) {
                System.out.print(row[j] + " ");
            }
            System.out.println();
        }
    }

    public void printVector(int[] vector) {
        for (int num : vector) {
            System.out.print(num + " ");
        }
    }
}
