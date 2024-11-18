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

    public static int[][] matrixMultiply(int[][] matrix1, int[][] matrix2, int startRow, int endRow) {
        int n = matrix1.length;
        int[][] result = new int[endRow - startRow][n];

        for (int i = startRow; i < endRow; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    result[i - startRow][j] += matrix1[i][k] * matrix2[k][j];
                }
            }
        }
        return result;
    }

    public static int[] vectorMatrixMultiply(int[] vector, int[][] matrix, int begin, int end) {
        int numCols = end - begin;
        int[] result = new int[numCols];

        int[][] slicedMatrix = new int[matrix.length][numCols];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < numCols; j++) {
                slicedMatrix[i][j] = matrix[i][begin + j];
            }
        }

        for (int i = 0; i < slicedMatrix.length; i++) {
            for (int j = 0; j < slicedMatrix[i].length; j++) {
                result[j] += slicedMatrix[i][j] * vector[i];
            }
        }

        return result;
    }

    public static int vectorMultiply(int[] vector1, int[] vector2, int begin, int end) {
        int result = 0;

        for (int k = begin; k < end; k++) {
            result += vector1[k] * vector2[k];
        }

        return result;
    }

    public static int[] vectorScalarMultiply(int[] vector, int scalarValue, int begin, int end) {
        int[] result = new int[end - begin];

        for (int i = 0; i < end - begin; i++) {
            result[i] = vector[i + begin] * scalarValue;
        }

        return result;
    }

    public static int[] vectorScalarMultiply(int[] vector, int scalarValue) {
        for (int i = 0; i < vector.length; i++) {
            vector[i] *= scalarValue;
        }

        return vector;
    }

    public static int[] addVectors(int[] vector1, int[] vector2) {
        for (int i = 0; i < vector1.length; i++) {
            vector1[i] += vector2[i];
        }

        return vector1;
    }

    public static void printVector(int[] vector) {
        for (int num : vector) {
            System.out.print(num + " ");
        }
    }

    public static void makeMatrix(int[][] matrix, int[][] slice, int begin, int end) {
        for (int i = begin; i < end; i++) {
            for (int j = 0; j < slice[i - begin].length; j++) {
                matrix[i][j] = slice[i - begin][j];
            }
        }
    }

    public static int[] sortVector(int[] vector) {
        int[] sortedVector = vector.clone();
        Arrays.sort(sortedVector);
        return sortedVector;
    }

    public static int[] concatAndSort(int[] vector1, int[] vector2) {
        int[] result = new int[vector1.length + vector2.length];
        System.arraycopy(vector1, 0, result, 0, vector2.length);
        System.arraycopy(vector2, 0, result, vector1.length,
                vector2.length);
        return sortVector(result);
    }


    public static void makeFinalVector(int[] Z, int[] Zh, int begin, int end) {
        int ind = 0;
        for (int i = begin; i < end; i++) {
            Z[i] = Zh[ind++];
        }
    }
}
