LINKOPTS=
CCOPTS=-O3 -Wall -Werror -DFASTER_FACTORS

all: factor factor_test factor_bench

factor: factor.o main.o factor.h Makefile
	clang ${LINKOPTS} -o $@ factor.o main.o

factor_test: factor.o factor_test.o factor.h Makefile
	clang ${LINKOPTS} -o $@ factor_test.o factor.o 

factor_bench: factor.o factor_bench.o factor.h Makefile
	clang ${LINKOPTS} -o $@ factor_bench.o factor.o 
	
%.o: %.c factor.h Makefile
	clang -c ${CCOPTS} -o $@ $<

clean:
	rm -f factor factor.o main.o factor_test factor_test.o factor_bench factor_bench.o

run: factor
	./factor

test: factor_test
	./factor_test

benchmark: factor_bench
	./factor_bench | tee bench-latest.csv

.PHONY: all clean run test benchmark help
