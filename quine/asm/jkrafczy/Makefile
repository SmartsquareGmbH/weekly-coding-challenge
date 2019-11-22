all: simple meta

simple: simple.o Makefile
	ld -macosx_version_min 10.14.0 -lSystem -o simple simple.o -no_pie

simple.o: simple.asm Makefile
	nasm -g -o simple.o -f macho64 simple.asm

meta: meta.o Makefile
	ld -macosx_version_min 10.14.0 -lSystem -o meta meta.o -no_pie

meta.o: meta.asm Makefile
	nasm -g -o meta.o -f macho64 meta.asm

.PHONY: run-simple run-meta clean replicate

clean:
	rm -rf simple.o meta.o simple meta meta.sh tmp

run-simple: simple 
	./simple

run-meta: meta
	./meta | tee meta.sh
	chmod +x meta.sh

replicate: run-meta
	./meta.sh