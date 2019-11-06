extern crate hyper;
use hyper::{Body, Request, Response, Server, Method, StatusCode};
use hyper::rt::Future;
use hyper::service::service_fn_ok;

const PATH_PREFIX: &str = "/generate/";
const ERROR_RESPONSE: &str = "\"ERROR\"";

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

    let mut n: u32 = arg_parsed.unwrap();

    let mut first = true;
    let mut result = "[".to_string();
    let mut p = 2;
    let mut p2 = 4;

    while p2 <= n {
        if n % p == 0 {
            if first {
                first = false;
            } else {
                result.push_str(",");
            }
            result.push_str(&p.to_string());
            n /= p;
        } else {
            p += 1;
            p2 = p*p;
        }
    }
    if n != 1 {
        if !first {
            result.push_str(",");
        }
        result.push_str(&n.to_string());
    }
    result.push_str("]");
    *res.body_mut() = Body::from(result);
    *res.status_mut() = StatusCode::OK;
    return res;
}

fn main() {
    // This is our socket address...
    let addr = ([0, 0, 0, 0], 8080).into();

    // A `Service` is needed for every connection, so this
    // creates one from our `hello_world` function.
    let new_svc = || {
        // service_fn_ok converts our function into a `Service`
        service_fn_ok(factor_service)
    };

    let server = Server::bind(&addr)
        .serve(new_svc)
        .map_err(|e| eprintln!("server error: {}", e));

    // Run this server for... forever!
    hyper::rt::run(server);
}
