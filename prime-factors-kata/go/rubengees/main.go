package main

import (
	"encoding/json"
	"fmt"
	"log"
	"math"
	"strconv"
	"strings"

	"github.com/valyala/fasthttp"
)

func main() {
	if err := fasthttp.ListenAndServe(":8080", requestHandler); err != nil {
		log.Fatalf("Error in ListenAndServe: %s", err)
	}
}

func requestHandler(ctx *fasthttp.RequestCtx) {
	path := string(ctx.Path())
	number, _ := strconv.Atoi(path[strings.LastIndex(path, "/")+1 : len(path)])
	json, _ := json.Marshal(calculate(uint32(number)))

	fmt.Fprintf(ctx, string(json))
	ctx.SetContentType("application/json; charset=utf8")
	ctx.SetStatusCode(fasthttp.StatusOK)
}

func calculate(number uint32) []uint32 {
	result := []uint32{}
	n := number

	for n%2 == 0 {
		result = append(result, uint32(2))
		n /= 2
	}

	for i := uint32(3); i <= uint32(math.Sqrt(float64(n))); i += 2 {
		for n%i == 0 {
			result = append(result, i)
			n /= i
		}
	}

	if n > 2 {
		result = append(result, n)
	}

	return result
}
