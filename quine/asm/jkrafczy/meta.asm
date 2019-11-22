; This one is not actually a quine.
; It doesn't /just/ output its own code. It outputs a shell script.
; That shell script then writes several files - simple.asm, meta.asm and Makefile.
; simple.asm is a simpler quine, meta.asm is this file, and Makefile contains 
; the build instructions for both meta and simple. 
%define stdout         0x1
%define syscall_write   0x2000004
%define syscall_exit    0x2000001


global _main
section .text
_main:
    mov rax, syscall_write
    mov rdi, stdout
    mov rsi, msg
    mov rdx, msg.len
    syscall

exit:
    mov rax, syscall_exit
    mov rdi, 0
    syscall


section .data
msg:
db "#!/usr/bin/env bash",10
db "set -e", 10
db "mkdir -p tmp", 10
db "cat > tmp/meta.asm <<EOF", 10
incbin "meta.asm"
db 10, "EOF", 10

db "cat > tmp/simple.asm <<EOF", 10
incbin "simple.asm"
db 10, "EOF", 10

db "cat > tmp/Makefile <<EOF", 10
incbin "Makefile"
db 10, "EOF", 10

db "cd tmp", 10
db "make run-meta", 10


.len equ $ - msg