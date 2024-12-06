package com.vbash.compose.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AuthTextField(
    modifier: Modifier = Modifier,
    value: String,
    onChange: (String) -> Unit,
    label: String,
    isError: Boolean,
    errorMessage: String = "",
    options: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    TextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        isError = isError,
        supportingText = {
            if (isError) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        },
        keyboardOptions = options,
        visualTransformation = visualTransformation,
    )
}

@Preview
@Composable
fun EmailTextFieldPreviewError() {
    AuthTextField(Modifier, "", {}, "Enter your email", true, "Error")
}

@Preview
@Composable
fun EmailTextFieldPreview() {
    AuthTextField(Modifier, "", {}, "Enter your email", false, "Error")
}
