#include "./factor.h"
#include <stdio.h>

uint8_t factor(factor_int_t n, sized_factors_t factors) {
    factor_int_t p = 2;
    factor_int_t psquared = 4;
    uint8_t nfactors = 0;

    while (psquared <= n) {
        if (n % p == 0) {
            factors[nfactors] = p;
            n /= p;
            nfactors++;
        } else {
            p++;
            psquared = p*p;
        }
    }

    if (n > 1 || nfactors == 0) {
        factors[nfactors] = n;
        nfactors++;
    }

    return nfactors;
}
