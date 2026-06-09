package ni.edu.uam.calculadora

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test

class PantallaCalculadoraTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun verificarRestaConNumerosIngresados() {

        composeTestRule.setContent {
            PantallaCalculadora()
        }

        composeTestRule
            .onNodeWithTag("numero1")
            .performTextInput("10")

        composeTestRule
            .onNodeWithTag("numero2")
            .performTextInput("4")

        composeTestRule
            .onNodeWithText("Restar")
            .performClick()

        composeTestRule
            .onNodeWithTag("resultado")
            .assertTextEquals("6")
    }
}