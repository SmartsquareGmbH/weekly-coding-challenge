package main

import (
	"reflect"
	"testing"
)

func Test_parse(t *testing.T) {
	type args struct {
		input string
	}
	tests := []struct {
		name string
		args args
		want []int
	}{
		{name: "Short list", args: args{input: "1,2,3,4"}, want: []int{1, 2, 3, 4}},
		{name: "List with newlines", args: args{input: "1,2,3,4\n"}, want: []int{1, 2, 3, 4}},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if got := parse(tt.args.input); !reflect.DeepEqual(got, tt.want) {
				t.Errorf("parse() = %v, want %v", got, tt.want)
			}
		})
	}
}

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
			name:       "Example 1",
			args:       args{program: []int{1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50}},
			wantResult: []int{3500, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50},
			wantErr:    false,
		},
		{
			name:       "Example 2",
			args:       args{program: []int{1, 0, 0, 0, 99}},
			wantResult: []int{2, 0, 0, 0, 99},
			wantErr:    false,
		},
		{
			name:       "Example 3",
			args:       args{program: []int{2, 3, 0, 3, 99}},
			wantResult: []int{2, 3, 0, 6, 99},
			wantErr:    false,
		},
		{
			name:       "Example 4",
			args:       args{program: []int{2, 4, 4, 5, 99, 0}},
			wantResult: []int{2, 4, 4, 5, 99, 9801},
			wantErr:    false,
		},
		{
			name:       "Example 5",
			args:       args{program: []int{1, 1, 1, 4, 99, 5, 6, 0, 99}},
			wantResult: []int{30, 1, 1, 4, 2, 5, 6, 0, 99},
			wantErr:    false,
		},
		{
			name:       "Error",
			args:       args{program: []int{1, 1, 1, 3}},
			wantResult: nil,
			wantErr:    true,
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
