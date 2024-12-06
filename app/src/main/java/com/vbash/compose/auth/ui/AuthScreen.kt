package com.vbash.compose.auth.ui


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vbash.compose.ui.AuthTextField
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = viewModel(),
    onShowMessage: (message: String) -> Unit,
    onTermsAndConditionTextClick: () -> Unit,
) {
    val authState by viewModel.state.collectAsStateWithLifecycle()

    val localContext = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is AuthViewModel.Event.ShowError -> onShowMessage(localContext.getString(event.message))
            }
        }
    }

    AuthForm(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        authState.email,
        viewModel::onEmailChange,
        authState.isEmailError,
        authState.emailErrorMessage,
        authState.password,
        viewModel::onPasswordChange,
        authState.isPasswordError,
        authState.passwordErrorMessage,
        authState.isCheckBoxChecked,
        viewModel::onCheckBoxChecked,
        viewModel::onLoginBtnClick,
        onTermsAndConditionTextClick,
    )
}


@Composable
fun AuthForm(
    modifier: Modifier,
    email: String,
    onEmailTextChanges: (String) -> Unit,
    isError: Boolean,
    errorMessage: String,
    password: String,
    onPasswordTextChanges: (String) -> Unit,
    isPasswordError: Boolean,
    passwordErrorMessage: String,
    isCheckBoxChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onLoginBtnClick: () -> Unit,
    onTermsAndConditionTextClick: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AuthTextField(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            email,
            onEmailTextChanges,
            "Enter email",
            isError,
            errorMessage,
            KeyboardOptions(keyboardType = KeyboardType.Email),
        )
        Spacer(Modifier.height(8.dp))
        AuthTextField(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            password,
            onPasswordTextChanges,
            "Enter password",
            isPasswordError,
            passwordErrorMessage,
            KeyboardOptions(keyboardType = KeyboardType.Password),
            PasswordVisualTransformation(),
        )
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(isCheckBoxChecked, onCheckedChange)
            Text(buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Color(0xff0057D9),
                        textDecoration = TextDecoration.Underline
                    )
                ) {
                    append("Read terms and conditions")
                }
            },
                Modifier
                    .fillMaxWidth()
                    .clickable {
                        onTermsAndConditionTextClick()
                    })
        }
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = onLoginBtnClick,
            modifier = Modifier.clickable(onClick = onLoginBtnClick)
        ) { Text("Login") }
    }
}