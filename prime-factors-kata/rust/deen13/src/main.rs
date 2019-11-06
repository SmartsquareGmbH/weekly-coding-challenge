#[macro_use] extern crate nickel;

use nickel::{Nickel, HttpRouter};

fn factors(n: u64) -> Vec<u64> {
    let mut factors = vec![];
    let mut num = n;
    let mut div = 2;

    while num > 1 {
        while num % div == 0 {
            factors.push(div);
            num = num / div;
        }
        div += 1;
    }

    factors
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
