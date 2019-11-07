package de.smartsquare.factoring

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class FactoringController {

    @GetMapping("/generate/{payload}")
    fun primeFactors(@PathVariable payload: Long): List<Long> {
        val primes: MutableList<Long> = mutableListOf()
        var prime: Long = 2
        var primeSq = prime * prime
        var number = payload

        while (primeSq <= number) {
            if (number % prime == 0L) {
                primes += prime
                number /= prime
            } else {
                prime++
                primeSq = prime * prime
            }
        }

        if (number != 1L) primes += number

        return primes
    }

}
