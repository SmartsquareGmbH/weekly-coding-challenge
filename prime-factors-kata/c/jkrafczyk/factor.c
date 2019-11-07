#include "./factor.h"
#include <stdio.h>

#ifdef FASTER_FACTORS
const factor_int_t SMALL_PRIMES[] = {3, 5, 7, 11, 13, 17, 19};
const int N_SMALL_PRIMES = sizeof(SMALL_PRIMES) / sizeof(factor_int_t);
#endif

uint8_t factor(factor_int_t n, sized_factors_t factors) {
    if (n <= 1) {
        return 0;
    }
    uint8_t nfactors = 0;

//fast-path factor '2':
    while ((n & 1) == 0) {
        factors[nfactors++] = 2;
        n >>= 1;
    }
    factor_int_t p = 3;
    factor_int_t psquared = 9;
#ifdef FASTER_FACTORS
    int small_primes_idx = 0;
#endif

    while (psquared <= n) {
        if (n % p == 0) {
            factors[nfactors] = p;
            n /= p;
            nfactors++;
        } else {
#ifdef FASTER_FACTORS
            if (small_primes_idx < N_SMALL_PRIMES) {
                p = SMALL_PRIMES[small_primes_idx++];
            } else {
                p+=2;
            }
#else
            p+=2;
#endif
            psquared = p*p;
        }
    }

    if (n > 1) {
        factors[nfactors] = n;
        nfactors++;
    }

    return nfactors;
}
