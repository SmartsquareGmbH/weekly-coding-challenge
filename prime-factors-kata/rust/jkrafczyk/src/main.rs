extern crate hyper;
use hyper::{Body, Request, Response, Server, Method, StatusCode};
use hyper::rt::Future;
use hyper::service::service_fn_ok;

const PATH_PREFIX: &str = "/generate/";
const ERROR_RESPONSE: &str = "\"ERROR\"";

fn factor(mut n: u32) -> Vec<u32> {
    let mut result = Vec::new();
    if n < 2 {
        return result;
    }
    result.reserve(32);

    while (n != 0) && (n % 2 == 0) {
        n /= 2;
        result.push(2);
    }

    let mut p = 3;
    let mut p2 = 9;
    while p2 <= n {
        if n % p == 0 {
            result.push(p);
            n /= p;
        } else {
            p += 2;
            p2 = p*p;
        }
    }
    if n > 1 {
        result.push(n);
    }

    return result;
}

fn factor_service(req: Request<Body>) -> Response<Body> {
    let mut res = Response::new(Body::from(ERROR_RESPONSE));
    *res.status_mut() = StatusCode::NOT_FOUND;
    res.headers_mut().insert("Content-Type", "application/json".parse().unwrap());
    //We could do fancy regex-based routing here. But we won't.

    //GET request below /generate/
    if req.method() != &Method::GET {
        return res;
    } if !req.uri().path().starts_with(PATH_PREFIX) {
        return res;
    }

    //...with at least one additional character after the slash
    let arg = &req.uri().path()[PATH_PREFIX.len()..];
    if arg.len() == 0 {
        return res;
    }

    //and the after-the-slash-part being a 32-bit unsigned number
    let arg_parsed = arg.parse();
    if arg_parsed.is_err() {
        return res;
    }

    let n: u32 = arg_parsed.unwrap();
    let result = format!("{:?}", factor(n));
    *res.body_mut() = Body::from(result);
    *res.status_mut() = StatusCode::OK;
    return res;
}

fn main() {
    let addr = ([0, 0, 0, 0], 8080).into();

    let new_svc = || {
        service_fn_ok(factor_service)
    };

    let server = Server::bind(&addr)
        .serve(new_svc)
        .map_err(|e| eprintln!("server error: {}", e));

    println!("Listening on {:?}", addr);
    hyper::rt::run(server);
}
