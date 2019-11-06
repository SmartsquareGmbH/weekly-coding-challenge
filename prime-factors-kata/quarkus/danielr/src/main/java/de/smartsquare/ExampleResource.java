package de.smartsquare;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/generate")
public class ExampleResource {

    @GET
    @Path("{number}")
    @Produces(MediaType.TEXT_PLAIN)
    public String primefactors(@PathParam("number") long number) {
        List<Long> primes = new ArrayList<>();
        long prime = 2;
        while (number > 1) {
            while (number % prime == 0) {
                primes.add(prime);
                number /= prime;
            }
            prime++;
        }

        return primes.toString();
    }
}