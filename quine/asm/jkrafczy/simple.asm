%define stdout         0x1
%define syscall_write   0x2000004
%define syscall_exit    0x2000001

global _main
section .text
_main:
    mov rax, syscall_write
    mov rdi, stdout
    mov rsi, msg
    mov rdx, msglen
    syscall

    mov rax, syscall_exit
    mov rdi, 0
    syscall

section .data
msg:
incbin "simple.asm"
msglen equ $ - msg