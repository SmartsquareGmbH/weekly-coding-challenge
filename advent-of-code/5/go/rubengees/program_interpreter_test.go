package main

import (
	"reflect"
	"testing"
)

// We are missing many tests here, but that's because the program_interpreter is not comfortably testable anymore.
func Test_runProgram(t *testing.T) {
	type args struct {
		program []int
	}
	tests := []struct {
		name       string
		args       args
		wantResult []int
		wantErr    bool
	}{
		{
			name:       "example 1",
			args:       args{program: []int{1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50}},
			wantResult: []int{3500, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50},
			wantErr:    false,
		},
		{
			name:       "example 2",
			args:       args{program: []int{1, 0, 0, 0, 99}},
			wantResult: []int{2, 0, 0, 0, 99},
			wantErr:    false,
		},
		{
			name:       "example 3",
			args:       args{program: []int{2, 3, 0, 3, 99}},
			wantResult: []int{2, 3, 0, 6, 99},
			wantErr:    false,
		},
		{
			name:       "example 4",
			args:       args{program: []int{2, 4, 4, 5, 99, 0}},
			wantResult: []int{2, 4, 4, 5, 99, 9801},
			wantErr:    false,
		},
		{
			name:       "example 5",
			args:       args{program: []int{1, 1, 1, 4, 99, 5, 6, 0, 99}},
			wantResult: []int{30, 1, 1, 4, 2, 5, 6, 0, 99},
			wantErr:    false,
		},
		{
			name:       "new example 1",
			args:       args{program: []int{1002, 4, 3, 4, 33}},
			wantResult: []int{1002, 4, 3, 4, 99},
			wantErr:    false,
		},
		{
			name:       "new example 2",
			args:       args{program: []int{1101, 100, -1, 4, 0}},
			wantResult: []int{1101, 100, -1, 4, 99},
			wantErr:    false,
		},
		{
			name:       "no terminating opcode",
			args:       args{program: []int{1, 1, 1, 3}},
			wantResult: nil,
			wantErr:    true,
		},
		{
			name:       "invalid argument amount",
			args:       args{program: []int{1, 1, 1}},
			wantResult: nil,
			wantErr:    true,
		},
		{
			name:       "empty program",
			args:       args{program: []int{}},
			wantResult: nil,
			wantErr:    true,
		},
		{
			name:       "immediate termination",
			args:       args{program: []int{99}},
			wantResult: []int{99},
			wantErr:    false,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			gotResult, err := runProgram(tt.args.program)
			if (err != nil) != tt.wantErr {
				t.Errorf("runProgram() error = %v, wantErr %v", err, tt.wantErr)
				return
			}
			if !reflect.DeepEqual(gotResult, tt.wantResult) {
				t.Errorf("runProgram() gotResult = %v, want %v", gotResult, tt.wantResult)
			}
		})
	}
}

func Test_parseOpcode(t *testing.T) {
	type args struct {
		number int
	}
	tests := []struct {
		name       string
		args       args
		wantOpcode Opcode
		wantErr    bool
	}{
		{name: "1", args: args{number: 1}, wantOpcode: Opcode{value: 1, argumentMode1: 0, argumentMode2: 0, argumentMode3: 0}, wantErr: false},
		{name: "1001", args: args{number: 1001}, wantOpcode: Opcode{value: 1, argumentMode1: 0, argumentMode2: 1, argumentMode3: 0}, wantErr: false},
		{name: "11104", args: args{number: 11104}, wantOpcode: Opcode{value: 4, argumentMode1: 1, argumentMode2: 1, argumentMode3: 1}, wantErr: false},
		{name: "11100", args: args{number: 11104}, wantOpcode: Opcode{value: 4, argumentMode1: 1, argumentMode2: 1, argumentMode3: 1}, wantErr: false},
		{name: "99", args: args{number: 99}, wantOpcode: Opcode{value: 99, argumentMode1: 0, argumentMode2: 0, argumentMode3: 0}, wantErr: false},
		{name: "11109", args: args{number: 11109}, wantOpcode: Opcode{}, wantErr: true},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			gotOpcode, err := parseOpcode(tt.args.number)
			if (err != nil) != tt.wantErr {
				t.Errorf("parseOpcode() error = %v, wantErr %v", err, tt.wantErr)
				return
			}
			if !reflect.DeepEqual(gotOpcode, tt.wantOpcode) {
				t.Errorf("parseOpcode() gotOpcode = %v, want %v", gotOpcode, tt.wantOpcode)
			}
		})
	}
}
