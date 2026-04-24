package com.toolbox.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.toolbox.R
import com.toolbox.ui.calculator.CalculatorScreen
import com.toolbox.ui.converter.ConverterScreen
import com.toolbox.ui.currency.CurrencyScreen

sealed class Screen(val route: String) {
    object Calculator : Screen("calculator")
    object Converter : Screen("converter")
    object Currency : Screen("currency")
}

data class BottomNavItem(
    val screen: Screen,
    val iconRes: Int,
    val labelRes: Int
)

@Composable
fun ToolBoxNavHost() {
    val navController = rememberNavController()
    val items = listOf(
        BottomNavItem(Screen.Calculator, R.drawable.ic_calculator, R.string.tab_calculator),
        BottomNavItem(Screen.Converter, R.drawable.ic_converter, R.string.tab_converter),
        BottomNavItem(Screen.Currency, R.drawable.ic_currency, R.string.tab_currency),
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { item ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == item.screen.route } == true,
                        onClick = {
                            navController.navigate(item.screen.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                painter = painterResource(item.iconRes),
                                contentDescription = stringResource(item.labelRes)
                            )
                        },
                        label = { Text(stringResource(item.labelRes)) }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Calculator.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues))
            {
                composable(Screen.Calculator.route) { CalculatorScreen() }
                composable(Screen.Converter.route) { ConverterScreen() }
                composable(Screen.Currency.route) { CurrencyScreen() }
            }
    }
}
