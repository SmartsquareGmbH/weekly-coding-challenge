LINKOPTS=
CCOPTS=-O3 -Wall -Werror

all: factor factor_test

factor: factor.o main.o factor.h Makefile
	clang ${LINKOPTS} -o $@ factor.o main.o

factor_test: factor.o factor_test.o factor.h Makefile
	clang ${LINKOPTS} -o $@ factor_test.o factor.o 

%.o: %.c factor.h Makefile
	clang -c ${CCOPTS} -o $@ $<

clean:
	rm -f factor factor.o main.o factor_test factor_test.o

run: factor
	./factor

test: factor_test
	./factor_test

.PHONY: all clean run test
