#[macro_use] extern crate nickel;

use nickel::{Nickel, HttpRouter};

fn factors(number: u64) -> Vec<u64> {
    let mut primes: Vec<u64> = Vec::new();
    let mut n = number;
    let mut prime = 2;
    let mut prime_sq = prime * prime;

    while prime_sq <= n {
	    if n % prime == 0 {
	       primes.push(prime);
	       n /= prime;
    	}
	    else {
	      prime += 1;
	      prime_sq = prime * prime;
	    }
    }

    if n != 1 {
	    primes.push(n);
    }

    primes
}

fn main() {
    let mut server = Nickel::new();

    server.get("/generate/:number", middleware! { |request|
        format!("{:?}", match request.param("number") {
            Some(ref number) => match number.parse::<u64>() {
                Ok(n) => factors(n),
                Err(_) => Vec::new()
            },
            None => Vec::new(),
        })
    });

    match server.listen("127.0.0.1:8080") {
        Ok(_) => println!("Successfully started"),
        Err(e) => println!("Startup failed: {}", e)
    }
}
