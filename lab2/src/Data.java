import java.util.Arrays;

public class Data {
    public static int[][] fillMatrixValue(int N, int value) {
        int[][] matrix = new int[N][N];
        for (int i = 0; i < N; i++) {
            Arrays.fill(matrix[i], value);
        }
        return matrix;
    }

    public static int[] fillVectorValue(int N, int value) {
        int[] vector = new int[N];
        Arrays.fill(vector, value);
        return vector;
    }

    public static int[][] matrixMultiply(int[][] matrix1, int[][] matrix2) {
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

    public static int[][] matrixScalarMultiply(int[][] matrix, int scalarValue) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                matrix[i][j] *= scalarValue;
            }
        }
        return matrix;
    }

    public static int[] vectorMatrixMultiply(int[] vector, int[][] matrix) {
        int[] result = new int[vector.length];

        for (int j = 0; j < vector.length; j++) {
            for (int k = 0; k < vector.length; k++) {
                result[j] += matrix[k][j] * vector[k];
            }
        }

        return result;
    }

    public static int vectorMultiply(int[] vector1, int[] vector2) {
        int result = 0;

        for (int i = 0; i < vector1.length; i++) {
            result += vector1[i] * vector2[i];
        }

        return result;
    }

    public static int[] vectorScalarMultiply(int[] vector, int scalarValue) {
        for (int i = 0; i < vector.length; i++) {
            vector[i] *= scalarValue;
        }
        return vector;
    }

    public static void printVector(int[] vector) {
        for (int num : vector) {
            System.out.print(num + " ");
        }
    }
}