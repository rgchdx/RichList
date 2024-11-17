package hu.ait.todocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import hu.ait.todocompose.navigation.MainNavigation
import hu.ait.todocompose.ui.screen.loading.LoadingScreen
import hu.ait.todocompose.ui.screen.summary.SummaryScreen
import hu.ait.todocompose.ui.screen.shopping.TodoListScreen
import hu.ait.todocompose.ui.theme.TodoComposeTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                        innerPadding ->
                    TodoAppNavHost(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun TodoAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = stringResource(R.string.splash)
) {
    NavHost(navController = navController, startDestination = startDestination
    ) {
        composable("splash") {
            SplashScreen(navController)
        }
        composable(MainNavigation.TodoListScreen.route) {
            TodoListScreen(
                onNavigateToSummary = { normalCount, normalPrice, foodCount, foodPrice, electronicsCount, electronicsPrice, bookCount, bookPrice, otherCount, otherPrice ->
                    navController.navigate(
                        MainNavigation.SummaryScreen.createRoute(normalCount, normalPrice, foodCount, foodPrice, electronicsCount, electronicsPrice, bookCount, bookPrice, otherCount, otherPrice))
                }
            )
        }

        composable(MainNavigation.SummaryScreen.route,
            arguments = listOf(
                navArgument("normalCount") { type = NavType.IntType },
                navArgument("normalPrice") { type = NavType.IntType },
                navArgument("foodCount") { type = NavType.IntType },
                navArgument("foodPrice") { type = NavType.IntType },
                navArgument("electronicsCount") { type = NavType.IntType },
                navArgument("electronicsPrice") { type = NavType.IntType },
                navArgument("bookCount") { type = NavType.IntType },
                navArgument("bookPrice") { type = NavType.IntType },
                navArgument("otherCount") { type = NavType.IntType },
                navArgument("otherPrice") { type = NavType.IntType }
            )
        ) {
            val normalCount = it.arguments?.getInt(stringResource(R.string.normalcount)) ?: 0
            val normalPrice = it.arguments?.getInt(stringResource(R.string.normalprice)) ?: 0
            val foodCount = it.arguments?.getInt(stringResource(R.string.foodcount)) ?: 0
            val foodPrice = it.arguments?.getInt(stringResource(R.string.foodprice)) ?: 0
            val electronicsCount = it.arguments?.getInt(stringResource(R.string.electronicscount)) ?: 0
            val electronicsPrice = it.arguments?.getInt(stringResource(R.string.electronicsprice)) ?: 0
            val bookCount = it.arguments?.getInt(stringResource(R.string.bookcount)) ?: 0
            val bookPrice = it.arguments?.getInt(stringResource(R.string.bookprice)) ?: 0
            val otherCount = it.arguments?.getInt(stringResource(R.string.othercount)) ?: 0
            val otherPrice = it.arguments?.getInt(stringResource(R.string.otherprice)) ?: 0

            SummaryScreen(
                normalItemCount = normalCount,
                normalItemPrice = normalPrice,
                foodItemCount = foodCount,
                foodItemPrice = foodPrice,
                electronicsItemCount = electronicsCount,
                electronicsItemPrice = electronicsPrice,
                bookItemCount = bookCount,
                bookItemPrice = bookPrice,
                otherItemCount = otherCount,
                otherItemPrice = otherPrice
            )
        }
    }
}

@Composable
fun SplashScreen(navController: NavHostController = rememberNavController()) {
    LaunchedEffect(Unit){
        kotlinx.coroutines.delay(3000)
        navController.navigate("todolist"){
            popUpTo("splash"){
                inclusive = true
            }

        }
    }
    LoadingScreen()
}

