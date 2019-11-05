#ifndef FACTOR_H
#define FACTOR_H

#include <stdint.h>

typedef uint32_t factor_int_t;
#define N_BITS (sizeof(factor_int_t) * 8)
#define MAX_FACTORS N_BITS
typedef factor_int_t sized_factors_t[MAX_FACTORS];
typedef factor_int_t *unsized_factors_t;

uint8_t factor(factor_int_t n, sized_factors_t factors);

#endif