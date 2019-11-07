package de.smartsquare.factoring

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

@SpringBootTest
class FactoringControllerTest {
    private val controller = standaloneSetup(FactoringController()).build()
    private val factoringController = FactoringController()

    @Test
    fun prime_factoring() {
        val response = controller
            .get("/generate/" + 666)
            .andReturn()
            .response
            .contentAsString

        assert(response.equals("[2,3,3,37]"))
    }

    @Test
    fun prime_factoring_with_huge_payload() {
        val result = factoringController.primeFactors(4294967295)

        assert(result.sum() == 65819L)
    }

}
