#include <stdio.h>
#include <inttypes.h>
#include <stdarg.h>
#include "factor.h"

static uint32_t n_failures = 0;

void print_testcase(factor_int_t n, int xnf, sized_factors_t xf) {
    printf("factor(%"PRIu64") == (%i factors) ", (uint64_t)n, xnf);
    for (int i=0; i<xnf; i++) {
        printf("%s%"PRIu64, i==0?"":" * ", (uint64_t)xf[i]);
    }
}


void fail(factor_int_t n, int xnf, sized_factors_t xf, int anf, sized_factors_t af) {
    printf("[x] ");
    print_testcase(n, xnf, xf);
    printf(" != ");
    print_testcase(n, anf, af);
    printf("\n");
    n_failures++;
}

void succeed(factor_int_t n, int xnf, sized_factors_t xf) {
    printf("[v] ");
    print_testcase(n, xnf, xf);
    printf("\n");
}

//Passed factors are *always* int!
//This might fail if factor_int_t is larger than uint32_t
void verify_factors(factor_int_t n, int expected_num_factors, ...) {
    sized_factors_t expected_factors;
    va_list argp;
    va_start(argp, expected_num_factors);
    for (int i=0; i<expected_num_factors; i++) {
        expected_factors[i] = va_arg(argp, int);
    }
    va_end(argp);
    sized_factors_t actual_factors;
    int actual_num_factors = factor(n, actual_factors);
    if (actual_num_factors != expected_num_factors) {
        return fail(n, expected_num_factors, expected_factors, actual_num_factors, actual_factors);
    }

    factor_int_t product = 1;
    for (int i=0; i < actual_num_factors; i++) {
        if (expected_factors[i] != actual_factors[i]) {
            return fail(n, expected_num_factors, expected_factors, actual_num_factors, actual_factors);
        }
        product *= actual_factors[i];
    } 

    if (product != n) {
        return fail(n, expected_num_factors, expected_factors, actual_num_factors, actual_factors);
    }

    succeed(n, expected_num_factors, expected_factors);
}

int main(int argc, char *argv[]) {
    verify_factors(1, 1, 1);
    verify_factors(2, 1, 2);
    verify_factors(4, 2, 2, 2);
    verify_factors(5, 1, 5);
    verify_factors(6, 2, 2, 3);
    verify_factors(8, 3, 2, 2, 2);
    verify_factors(9, 2, 3, 3);
    verify_factors(255, 3, 3, 5, 17);
    verify_factors(256, 8, 2, 2, 2, 2, 2, 2, 2, 2);
    verify_factors(257, 1, 257);
    verify_factors(4294967295, 5, 3, 5, 17, 257, 65537);

    if (n_failures) {
        return 1;
    } else {
        return 0;
    }
}