package de.smartsquare.factoring

import org.junit.jupiter.api.Test

class FactoringControllerTest {
    private val factoringController = FactoringController()

    @Test
    fun primeFactors() {
        val result = factoringController.primeFactors(666)

        assert(result == listOf(2L, 3L, 3L, 37L))
    }
}
