extern crate hyper;

use hyper::{Body, Request, Response, Server, StatusCode};
use hyper::service::service_fn_ok;
use hyper::rt::{self, Future};

fn api(req: Request<Body>) -> Response<Body> {
    let mut response = Response::new(Body::empty());

    let path: Vec<&str> = req.uri().path().split('/').collect();
    if path.len() <= 2 || path[1] != "generate" {
	*response.status_mut() = StatusCode::NOT_FOUND;
	return response;
    }

    let maybe_number = path[2].parse::<u32>();
    let resp_body = match maybe_number {
	Ok(n) => {
	    let primes = factorize(n);
	    format!("{:?}", primes)
	},
	Err(_e) => {
	    *response.status_mut() = StatusCode::BAD_REQUEST;
	    "Given input was not a 32bit unsigned integer!".to_owned()
	}
    };

    *response.body_mut() = Body::from(resp_body);
    return response;
}

fn main() {
    let addr = ([127, 0, 0, 1], 8080).into();

    let server = Server::bind(&addr)
	.serve(|| service_fn_ok(api))
	.map_err(|e| eprintln!("server error: {}", e));

    println!("Listening on http://{}", addr);

    rt::run(server);
}

fn factorize(number: u32) -> Vec<u32> {
    let mut primes: Vec<u32> = Vec::new();
    let mut n: u32 = number;
    let mut prime = 2;
    while n > 1 {
    	while n % prime == 0 {
	    primes.push(prime);
	    n /= prime;
	}
	prime += 1;
    }
    primes
}
