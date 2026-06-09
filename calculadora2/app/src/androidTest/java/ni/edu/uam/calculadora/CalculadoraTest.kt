package ni.edu.uam.calculadora

import ni.edu.uam.calculadora.models.Calculadora
import org.junit.Assert.assertEquals
import org.junit.Test

class CalculadoraTest {

    @Test
    fun verificarResta() {
        val calculadora = Calculadora()

        val resultado = calculadora.restar(10, 4)

        assertEquals(6, resultado)
    }

    @Test
    fun verificarRestaConNumerosNegativos() {
        val calculadora = Calculadora()

        val resultado = calculadora.restar(-5, -3)

        assertEquals(-2, resultado)
    }

    @Test
    fun verificarRestaConResultadoNegativo() {
        val calculadora = Calculadora()

        val resultado = calculadora.restar(3, 8)

        assertEquals(-5, resultado)
    }
}