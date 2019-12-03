package main

import (
	"reflect"
	"testing"
)

func Test_findClosestIntersection(t *testing.T) {
	type args struct {
		path1 Path
		path2 Path
	}
	tests := []struct {
		name       string
		args       args
		wantResult Intersection
		wantErr    bool
	}{
		{
			name: "",
			args: args{
				path1: parse("R8,U5,L5,D3"),
				path2: parse("U7,R6,D4,L4"),
			},
			wantResult: Intersection{x: 3, y: 3, manhattanDistance: 6, steps: 40},
			wantErr:    false,
		},
		{
			name: "",
			args: args{
				path1: parse("R75,D30,R83,U83,L12,D49,R71,U7,L72"),
				path2: parse("U62,R66,U55,R34,D71,R55,D58,R83"),
			},
			wantResult: Intersection{x: 155, y: 4, manhattanDistance: 159, steps: 726},
			wantErr:    false,
		},
		{
			name: "",
			args: args{
				path1: parse("R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51"),
				path2: parse("U98,R91,D20,R16,D67,R40,U7,R15,U6,R7"),
			},
			wantResult: Intersection{x: 124, y: 11, manhattanDistance: 135, steps: 516},
			wantErr:    false,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			gotIntersection, err := findClosestIntersection(tt.args.path1, tt.args.path2)
			if (err != nil) != tt.wantErr {
				t.Errorf("findClosestIntersection() error = %v, wantErr %v", err, tt.wantErr)
				return
			}
			if !reflect.DeepEqual(gotIntersection, tt.wantResult) {
				t.Errorf("findClosestIntersection() gotIntersection = %v, want %v", gotIntersection, tt.wantResult)
			}
		})
	}
}

func Test_findShortestIntersection(t *testing.T) {
	type args struct {
		path1 Path
		path2 Path
	}
	tests := []struct {
		name       string
		args       args
		wantResult Intersection
		wantErr    bool
	}{
		{
			name: "",
			args: args{
				path1: parse("R8,U5,L5,D3"),
				path2: parse("U7,R6,D4,L4"),
			},
			wantResult: Intersection{x: 6, y: 5, manhattanDistance: 11, steps: 30},
			wantErr:    false,
		},
		{
			name: "",
			args: args{
				path1: parse("R75,D30,R83,U83,L12,D49,R71,U7,L72"),
				path2: parse("U62,R66,U55,R34,D71,R55,D58,R83"),
			},
			wantResult: Intersection{x: 158, y: -12, manhattanDistance: 170, steps: 610},
			wantErr:    false,
		},
		{
			name: "",
			args: args{
				path1: parse("R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51"),
				path2: parse("U98,R91,D20,R16,D67,R40,U7,R15,U6,R7"),
			},
			wantResult: Intersection{x: 107, y: 47, manhattanDistance: 154, steps: 410},
			wantErr:    false,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			gotResult, err := findShortestIntersection(tt.args.path1, tt.args.path2)
			if (err != nil) != tt.wantErr {
				t.Errorf("findShortestIntersection() error = %v, wantErr %v", err, tt.wantErr)
				return
			}
			if !reflect.DeepEqual(gotResult, tt.wantResult) {
				t.Errorf("findShortestIntersection() gotResult = %v, want %v", gotResult, tt.wantResult)
			}
		})
	}
}

func Test_findIntersections(t *testing.T) {
	type args struct {
		path1 Path
		path2 Path
	}
	tests := []struct {
		name string
		args args
		want []Intersection
	}{
		{
			name: "",
			args: args{
				path1: Path{PathPart{direction: UP, amount: 3}, PathPart{direction: RIGHT, amount: 4}},
				path2: Path{PathPart{direction: RIGHT, amount: 4}, PathPart{direction: UP, amount: 3}},
			},
			want: []Intersection{{x: 4, y: 3, manhattanDistance: 7, steps: 14}},
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if got := findIntersections(tt.args.path1, tt.args.path2); !reflect.DeepEqual(got, tt.want) {
				t.Errorf("findIntersections() = %+v, want %+v", got, tt.want)
			}
		})
	}
}
