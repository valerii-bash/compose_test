package com.vbash.compose.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.vbash.compose.R
import com.vbash.compose.auth.ui.AuthScreen
import com.vbash.compose.home.ui.HomeScreen
import com.vbash.compose.home.ui.HomeViewModel
import com.vbash.compose.ui.theme.MyAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()
            MyAppTheme {
                RootNavHost(state, ::openBrowser)
            }
        }
    }

    fun openBrowser(url: String) {
        val webpage = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        startActivity(intent)
    }

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RootNavHost(
    authStatus: MainViewModel.AuthenticatedStatus,
    openBrowser: (url: String) -> Unit,
) {
    val startDestination =
        when (authStatus) {
            MainViewModel.AuthenticatedStatus.Yes -> Screens.Home.route
            MainViewModel.AuthenticatedStatus.No -> Screens.Auth.route
            MainViewModel.AuthenticatedStatus.Unknown -> Screens.Splash.route
        }
    val rootNavController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { _ ->
        NavHost(navController = rootNavController, startDestination = startDestination) {
            composable(Screens.Splash.route) {
                Box(Modifier.fillMaxSize()) {
                    Image(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .clip(CircleShape),
                        painter = painterResource(R.drawable.ic_launcher_background),
                        contentDescription = "",
                    )
                    Image(
                        modifier = Modifier.align(Alignment.Center),
                        painter = painterResource(R.drawable.ic_launcher_foreground),
                        contentDescription = "",
                    )
                }
            }
            authNavGraph(
                onShowMessage = { message ->
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(message)
                    }
                },
                onTermsAndConditionTextClick = {
                    openBrowser("https://google.com/")
                })
            homeNavGraph(onShowMessage = { message ->
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(message)
                }
            })
        }
    }
}


fun NavGraphBuilder.authNavGraph(
    onShowMessage: (message: String) -> Unit,
    onTermsAndConditionTextClick: () -> Unit,
) {
    navigation(
        startDestination = "auth",
        route = Screens.Auth.route,
    ) {
        composable("auth") {
            AuthScreen(
                viewModel = hiltViewModel(),
                onShowMessage = onShowMessage,
                onTermsAndConditionTextClick = onTermsAndConditionTextClick,
            )
        }
    }
}

fun NavGraphBuilder.homeNavGraph(
    onShowMessage: (message: String) -> Unit,
) {
    navigation(
        startDestination = "home",
        route = Screens.Home.route,
    ) {
        composable("home") {
            val vm: HomeViewModel = hiltViewModel()
            HomeScreen(vm, onShowMessage = onShowMessage)
        }
    }
}