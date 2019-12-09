package com.rubengees.day5

object OpcodeParser {

    fun parse(input: Int): Opcode<Opcode.OpcodeResult> {
        return when (val code = input.digits().take(2).mergeDigits()) {
            1 -> Opcode.Add
            2 -> Opcode.Multiply
            3 -> Opcode.ReadInput
            4 -> Opcode.WriteOutput
            99 -> Opcode.Halt
            else -> error("Unknown opcode: $code")
        }
    }
}
