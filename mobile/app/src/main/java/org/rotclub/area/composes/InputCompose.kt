package org.rotclub.area.composes

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import org.rotclub.area.lib.roundedValue
import org.rotclub.area.ui.theme.FrispyTheme

enum class InputType {
    DEFAULT,
    PASSWORD
}

@Composable
fun FrispyInput(
    value: MutableState<String>, modifier: Modifier = Modifier,
    onValueChange: ((String) -> Unit) = {}, label: String = "", inputType: InputType = InputType.DEFAULT,
    error: Boolean = false
) {
    val thisOnValueChange: (String) -> Unit = { value.value = it; onValueChange(it) }
    OutlinedTextField(
        value = value.value,
        onValueChange = { thisOnValueChange(it) },
        label = { Text(text = label) },
        singleLine = true,
        shape = RoundedCornerShape(roundedValue),
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = when (inputType) {
                InputType.DEFAULT -> KeyboardType.Text
                InputType.PASSWORD -> KeyboardType.Password
            }
        ),
        visualTransformation = when (inputType) {
            InputType.DEFAULT -> VisualTransformation.None
            InputType.PASSWORD -> PasswordVisualTransformation()
        },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            unfocusedLabelColor = if (error) FrispyTheme.Error500 else FrispyTheme.Surface400,
            unfocusedBorderColor = if (error) FrispyTheme.Error500 else FrispyTheme.Surface400,
            focusedBorderColor = if (error) FrispyTheme.Error500 else FrispyTheme.Primary500,
            focusedLabelColor = if (error) FrispyTheme.Error500 else FrispyTheme.Primary500,
            cursorColor = FrispyTheme.Primary300,
            focusedTextColor = FrispyTheme.TextColor,
            unfocusedTextColor = FrispyTheme.Surface100
        )
    )
}
