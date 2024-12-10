#include "Data.h"
#include <algorithm>

using namespace std;


int Data::N = 4;
int* Data::A;
int* Data::B;
int* Data::Z;
int* Data::C;
int* Data::D;
int Data::d;
int Data::e;
int Data::b = INT_MIN;
int** Data::MM;
int** Data::MX;
int** Data::MA;

void Data::fillMatrix(int** matrix) {
	for (int i = 0; i < N; i++) {
		for (int j = 0; j < N; j++) {
			matrix[i][j] = 1;
		}
	}
}

void Data::fillVector(int* vector) {
	for (int i = 0; i < N; i++) {
		vector[i] = 1;
	}
}

void Data::matrixMult(int start, int end, int** result, int** matrix1, int** matrix2) {
	for (int i = start; i < end; i++) {
		for (int j = 0; j < N; j++) {
			result[i][j] = 0;
			for (int k = 0; k < N; k++) {
				result[i][j] += matrix1[i][k] * matrix2[k][j];
			}
		}
	}
}

void Data::vectorMatrixMult(int start, int end, int* result, int* vector, int** matrix) {
	for (int i = start; i < end; i++) {
		result[i] = 0;
	}

	for (int i = start; i < end; i++) {
		for (int j = 0; j < N; j++) {
			result[i] += vector[j] * matrix[j][i];
		}
	}
}

void Data::vectorScalarMult(int start, int end, int* vector, int scalar) {
	for (int i = start; i < end; i++) {
		vector[i] *= scalar;
	}
}

void Data::addVectors(int start, int end, int* result, int* vector1, int* vector2) {
	for (int i = start; i < end; i++) {
		result[i] = vector1[i] + vector2[i];
	}
}

int Data::vectorMax(int start, int end, int* vector) {
	int max = vector[start];
	for (int i = start; i < end; i++) {
		if (vector[i] > max) {
			max = vector[i];
		}
	}
	return max;
}

void Data::vectorSort(int start, int end, int* vector) {
	sort(vector + start, vector + end);
}


