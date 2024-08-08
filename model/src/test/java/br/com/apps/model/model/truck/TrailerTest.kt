package br.com.apps.model.model.truck

import br.com.apps.model.model.fleet.Trailer
import br.com.apps.model.test_cases.sampleTrailer
import br.com.apps.model.test_cases.sampleTrailerDto
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TrailerTest {

    private lateinit var trailer: Trailer

    @Before
    fun setup() {
        trailer = sampleTrailer()
    }

    //---------------------------------------------------------------------------------------------//
    // toDto()
    //---------------------------------------------------------------------------------------------//

    @Test
    fun `should return a TrailerDto when call toDto`() {
        val expected = sampleTrailerDto()

        val dto = trailer.toDto()

        assertEquals(expected, dto)

    }




}