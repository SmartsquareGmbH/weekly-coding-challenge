package main

import (
	"encoding/json"
	"flag"
	"fmt"
	"log"
	"math"
	"net"
	"os"
	"os/exec"
	"runtime"
	"strconv"
	"strings"

	"github.com/valyala/fasthttp"
	"github.com/valyala/fasthttp/reuseport"
)

var (
	addr     = flag.String("addr", ":8080", "TCP address to listen to")
	prefork  = flag.Bool("prefork", false, "use prefork")
	affinity = flag.Bool("affinity", false, "use affinity for prefork")
	child    = flag.Bool("child", false, "is child proc")
)

func main() {
	flag.Parse()

	if err := fasthttp.Serve(listener(), requestHandler); err != nil {
		log.Fatalf("Error in ListenAndServe: %s", err)
	}
}

func listener() net.Listener {
	if *prefork == false {
		ln, _ := net.Listen("tcp4", *addr)

		return ln
	}

	if *child == false {
		children := make([]*exec.Cmd, runtime.NumCPU())

		for i := range children {
			if *affinity {
				children[i] = exec.Command(os.Args[0], "-prefork", "-child")
			} else {
				children[i] = exec.Command("taskset", "-c", fmt.Sprintf("%d", i), os.Args[0], "-prefork", "-child")
			}
			children[i].Stdout = os.Stdout
			children[i].Stderr = os.Stderr

			if err := children[i].Start(); err != nil {
				log.Fatal(err)
			}
		}

		for _, child := range children {
			if err := child.Wait(); err != nil {
				log.Print(err)
			}
		}

		os.Exit(0)
		panic("After Exit")
	}

	listener, err := reuseport.Listen("tcp4", *addr)
	if err != nil {
		log.Fatal(err)
	}

	return listener
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
