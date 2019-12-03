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
		want Path
	}{
		{
			name: "",
			args: struct{ input string }{input: "U12,D14"},
			want: Path{PathPart{direction: UP, amount: 12}, PathPart{direction: DOWN, amount: 14}}},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if got := parse(tt.args.input); !reflect.DeepEqual(got, tt.want) {
				t.Errorf("parse() = %v, want %v", got, tt.want)
			}
		})
	}
}
