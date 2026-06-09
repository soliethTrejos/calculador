package ni.edu.uam.calculadora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ni.edu.uam.calculadora.models.Calculadora

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PantallaCalculadora()
        }
    }
}

@Composable
fun PantallaCalculadora() {

    val calculadora = Calculadora()

    var pantalla by remember { mutableStateOf("0") }
    var numeroAnterior by remember { mutableStateOf<Double?>(null) }
    var operador by remember { mutableStateOf<String?>(null) }
    var limpiarPantalla by remember { mutableStateOf(false) }

    fun formatearResultado(valor: Double): String {
        return if (valor % 1.0 == 0.0) {
            valor.toInt().toString()
        } else {
            valor.toString()
        }
    }

    fun escribirNumero(numero: String) {
        if (pantalla == "0" || limpiarPantalla || pantalla == "Error") {
            pantalla = numero
            limpiarPantalla = false
        } else {
            pantalla += numero
        }
    }

    fun escribirDecimal() {
        if (limpiarPantalla || pantalla == "Error") {
            pantalla = "0."
            limpiarPantalla = false
        } else if (!pantalla.contains(".")) {
            pantalla += "."
        }
    }

    fun seleccionarOperador(nuevoOperador: String) {
        numeroAnterior = pantalla.toDoubleOrNull()
        operador = nuevoOperador
        limpiarPantalla = true
    }

    fun calcularResultado() {
        val num1 = numeroAnterior
        val num2 = pantalla.toDoubleOrNull()

        if (num1 != null && num2 != null && operador != null) {
            val resultado = when (operador) {
                "+" -> calculadora.sumar(num1, num2)
                "-" -> calculadora.restar(num1, num2)
                "*" -> calculadora.multiplicar(num1, num2)
                "/" -> {
                    if (num2 == 0.0) {
                        pantalla = "Error"
                        return
                    } else {
                        calculadora.dividir(num1, num2)
                    }
                }
                else -> num2
            }

            pantalla = formatearResultado(resultado)
            numeroAnterior = null
            operador = null
            limpiarPantalla = true
        }
    }

    fun limpiarTodo() {
        pantalla = "0"
        numeroAnterior = null
        operador = null
        limpiarPantalla = false
    }

    fun borrarUltimo() {
        pantalla = if (pantalla.length > 1 && pantalla != "Error") {
            pantalla.dropLast(1)
        } else {
            "0"
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE9C3F5))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF7F7F7)
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Calculadora",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF555555)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(
                            color = Color(0xFFEFF4F4),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 20.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = pantalla,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF555555),
                        modifier = Modifier.testTag("resultado")
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                FilaBotones(
                    botones = listOf("C", "DEL", "/", "*"),
                    onClick = { valor ->
                        when (valor) {
                            "C" -> limpiarTodo()
                            "DEL" -> borrarUltimo()
                            "/" -> seleccionarOperador("/")
                            "*" -> seleccionarOperador("*")
                        }
                    }
                )

                FilaBotones(
                    botones = listOf("7", "8", "9", "-"),
                    onClick = { valor ->
                        when (valor) {
                            "-" -> seleccionarOperador("-")
                            else -> escribirNumero(valor)
                        }
                    }
                )

                FilaBotones(
                    botones = listOf("4", "5", "6", "+"),
                    onClick = { valor ->
                        when (valor) {
                            "+" -> seleccionarOperador("+")
                            else -> escribirNumero(valor)
                        }
                    }
                )

                FilaBotones(
                    botones = listOf("1", "2", "3", "="),
                    onClick = { valor ->
                        when (valor) {
                            "=" -> calcularResultado()
                            else -> escribirNumero(valor)
                        }
                    }
                )

                FilaBotones(
                    botones = listOf("0", ".", "="),
                    onClick = { valor ->
                        when (valor) {
                            "." -> escribirDecimal()
                            "=" -> calcularResultado()
                            else -> escribirNumero(valor)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun FilaBotones(
    botones: List<String>,
    onClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        botones.forEach { texto ->
            BotonCalculadora(
                texto = texto,
                modifier = Modifier.weight(1f),
                onClick = {
                    onClick(texto)
                }
            )
        }
    }
}

@Composable
fun BotonCalculadora(
    texto: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val esOperador = texto in listOf("+", "-", "*", "/", "=")
    val esEspecial = texto in listOf("C", "DEL")

    val colorBoton = when {
        texto == "=" -> Color(0xFFDFA2C9)
        esOperador -> Color(0xFFE6D7F2)
        esEspecial -> Color(0xFFEFF4F4)
        else -> Color(0xFFFFFFFF)
    }

    Button(
        onClick = onClick,
        modifier = modifier.height(58.dp),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorBoton,
            contentColor = Color.Black
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 6.dp
        )
    ) {
        Text(
            text = texto,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}