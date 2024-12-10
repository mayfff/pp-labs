#pragma once

#include <iostream>

class Data {
public:
	static int N;
	static int* A;
	static int* B;
	static int* Z;
	static int d;
	static int e;
	static int b;
	static int** MM;
	static int** MX;
	static int** MA;

	static int* C;
	static int* D;

	static void fillMatrix(int** matrix);
	static void fillVector(int* vector);
	static void matrixMult(int start, int end, int** result, int** matrix1, int** matrix2);
	static void vectorMatrixMult(int start, int end, int* result, int* vector, int** matrix);
	static void vectorScalarMult(int start, int end, int* vector, int scalar);
	static void addVectors(int start, int end, int* result, int* vector1, int* vector2);
	static int vectorMax(int start, int end, int* vector);
	static void vectorSort(int start, int end, int* vector);
};