package main

import "testing"

func Test_onlyIncreases(t *testing.T) {
	type args struct {
		number int
	}
	tests := []struct {
		name string
		args args
		want bool
	}{
		{name: "123456", args: args{number: 123456}, want: true},
		{name: "111111", args: args{number: 111111}, want: true},
		{name: "112233", args: args{number: 112233}, want: true},
		{name: "223450", args: args{number: 223450}, want: false},
		{name: "654321", args: args{number: 654321}, want: false},
		{name: "666665", args: args{number: 666665}, want: false},
		{name: "656565", args: args{number: 656565}, want: false},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if got := onlyIncreases(tt.args.number); got != tt.want {
				t.Errorf("onlyIncreases() = %v, want %v", got, tt.want)
			}
		})
	}
}

func Test_hasPair(t *testing.T) {
	type args struct {
		number int
	}
	tests := []struct {
		name string
		args args
		want bool
	}{
		{name: "111111", args: args{number: 111111}, want: true},
		{name: "114567", args: args{number: 114567}, want: true},
		{name: "114511", args: args{number: 114511}, want: true},
		{name: "101010", args: args{number: 101010}, want: false},
		{name: "123456", args: args{number: 123456}, want: false},
		{name: "123789", args: args{number: 123789}, want: false},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if got := hasPair(tt.args.number); got != tt.want {
				t.Errorf("hasPair() = %v, want %v", got, tt.want)
			}
		})
	}
}

func Test_hasPairWithoutGroup(t *testing.T) {
	type args struct {
		number int
	}
	tests := []struct {
		name string
		args args
		want bool
	}{
		{name: "112233", args: args{number: 112233}, want: true},
		{name: "112233", args: args{number: 112345}, want: true},
		{name: "111122", args: args{number: 111122}, want: true},
		{name: "111122", args: args{number: 123422}, want: true},
		{name: "123444", args: args{number: 123444}, want: false},
		{name: "555789", args: args{number: 555789}, want: false},
		{name: "579999", args: args{number: 579999}, want: false},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if got := hasPairWithoutGroup(tt.args.number); got != tt.want {
				t.Errorf("hasPairWithoutGroup() = %v, want %v", got, tt.want)
			}
		})
	}
}
