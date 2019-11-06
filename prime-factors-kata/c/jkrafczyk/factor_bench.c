#include "./factor.h"
#include <inttypes.h>
#include <stdio.h>
#include <time.h>

#define PRIMING_RUNS 10
#define TEST_RUNS 100000

const factor_int_t TEST_NUMBERS[] = {65535, 65536, 65537, 2147483647};
const int N_TEST_NUMBERS = sizeof(TEST_NUMBERS) / sizeof(factor_int_t);

double benchmark(factor_int_t n) {
    sized_factors_t actual_factors;    
    //Do a few trial runs to prime caches and branch prediction
    for (int i=0; i<PRIMING_RUNS; i++) {
         factor(n, actual_factors);
    }

    //Do the actual benchmark
    clock_t start_clk = clock();
    for (size_t i=0; i<TEST_RUNS; i++) {
        factor(n, actual_factors);
    }
    clock_t end_clk = clock();
    double time_taken = (double)(end_clk - start_clk) / ((double)CLOCKS_PER_SEC/1000./1000.);
    printf("%"PRIu64", %.2f, %.2f\n", (uint64_t)n, time_taken, time_taken/TEST_RUNS);
    return time_taken;
}

int main(int argc, char *argv[]) {
    printf("n,us/total,us/call\n");
    for (int n=0; n<10000; n++) {
        benchmark(n);
    }
    for (int idx=0; idx < N_TEST_NUMBERS; idx++) {
        benchmark(TEST_NUMBERS[idx]);
    }
}