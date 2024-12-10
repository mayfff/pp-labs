#include <iostream>
#include <algorithm>
#include <omp.h>
#include "Data.h"

using namespace std;

int main() {
	omp_lock_t Lock1;
	omp_init_lock(&Lock1);
	omp_lock_t Lock2;
	omp_init_lock(&Lock2);

	cout << "enter N = ";
	cin >> Data::N;
	int P = 4;
	int H = Data::N / P;

	Data::A = new int[Data::N];
	Data::B = new int[Data::N];
	Data::Z = new int[Data::N];
	Data::C = new int[Data::N];
	Data::D = new int[Data::N];

	Data::MM = new int* [Data::N];
	Data::MX = new int* [Data::N];
	Data::MA = new int* [Data::N];

	for (int i = 0; i < Data::N; i++) {
		Data::MM[i] = new int[Data::N];
		Data::MX[i] = new int[Data::N];
		Data::MA[i] = new int[Data::N];
	}

	int tid, bi, ei, di;
	double startTime = omp_get_wtime();
	#pragma omp parallel num_threads(P) private(tid, bi, ei, di)
	{
		tid = omp_get_thread_num() + 1;
		cout << "Thread T" << tid << " started" << endl;
		int start = (tid - 1) * H;
		int end = start + H;
		// Введення даних.
		switch (tid) {
			case 1: {
				Data::d = 1;
				Data::fillMatrix(Data::MM);
				break;
			}
			case 2: {
				Data::e = 1;
				Data::fillVector(Data::B);
				Data::fillMatrix(Data::MX);
				break;
			}
			case 4: {
				Data::fillVector(Data::Z);
				break;
			}
		}
		// Чекати сигнал про завершення введення даних в усіх потоках.
		#pragma omp barrier

		// Обчислення 1: MAH = MM * MXH.
		Data::matrixMult(start, end, Data::MA, Data::MM, Data::MX);
		#pragma omp barrier

		// КД1: Копіювання di = d.
		#pragma omp critical
		{
			di = Data::d;
		}

		// Обчислення 2: Dh = d * Bh + Z * MAh.
		Data::vectorScalarMult(start, end, Data::B, Data::d);
		Data::vectorMatrixMult(start, end, Data::C, Data::Z, Data::MA);
		Data::addVectors(start, end, Data::D, Data::B, Data::C);

		// Обчислення 3: DH = sort(DH).
		Data::vectorSort(start, end, Data::D);
		// Сигнал задачі Т2 про завершення сортування в Т1.
		// Т1 чекає сигнал від задачі Т1 про завершення сортування.
		//
		// Сигнал задачі Т4 про завершення сортування в Т3.
		// Т4 чекає сигнал від задачі Т3 про завершення сортування.
		#pragma omp barrier

		if (tid == 2) {
			// Обчислення 4: D2H = sort(DH, DH).
			Data::vectorSort(0, end, Data::D);
		} else if (tid == 4) {
			// Обчислення 4: D2H = sort(DH, DH).
			Data::vectorSort(2 * H, end, Data::D);
		}
		// Сигнал задачі Т4 про завершення часткового злиття в Т2.
		// Т4 чекає сигнал від задачі Т2 про завершення часткового злиття.
		#pragma omp barrier

		if (tid == 4) {
			// Обчислення 5: D4H = sort(D2H, D2H).
			Data::vectorSort(0, end, Data::D);
		}
		#pragma omp barrier	

		// Обчислення 6: bi = max(BH).
		bi = Data::vectorMax(start, end, Data::B);

		// КД2: Обчислення 7: b = max(b, bi).
		#pragma omp critical
		{
			Data::b = max(Data::b, bi);
		}
		// Чекати сигнал про обчислення b у всіх потоках.
		#pragma omp barrier

		// КД3: Копіювання ei = e.
		#pragma omp critical
		{
			ei = Data::e;
		}

		// КД4: Копіювання bi = b.
		#pragma omp critical
		{
			bi = Data::b;
		}

		// Обчислення 8: AH = DH * ei + bi * ZH.
		Data::vectorScalarMult(start, end, Data::D, ei);
		Data::vectorScalarMult(start, end, Data::Z, bi);
		Data::addVectors(start, end, Data::A, Data::D, Data::Z);
		// Чекати сигнал про завершення обчислення Аh у всіх потоках.
		#pragma omp barrier

		// T4 виводить результат А
		if ((tid == 4) && (Data::N < 12)) {
			for (int i = 0; i < Data::N; i++) {
				cout << Data::A[i] << " ";
			}
			cout << endl;
		}
	}

	double endTime = omp_get_wtime();
	cout << "Execution time: " << (endTime - startTime) * 1000 << " ms" << endl;
	return 0;
}