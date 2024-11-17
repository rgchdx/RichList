package hu.ait.todocompose.navigation

sealed class MainNavigation(val route: String) {
    object TodoListScreen : MainNavigation("todolist")

    object SummaryScreen : MainNavigation(
        "summaryscreen?normalCount={normalCount}&normalPrice={normalPrice}&foodCount={foodCount}&foodPrice={foodPrice}&electronicsCount={electronicsCount}&electronicsPrice={electronicsPrice}&bookCount={bookCount}&bookPrice={bookPrice}&otherCount={otherCount}&otherPrice={otherPrice}"
    ) {
        fun createRoute(
            normalCount: Int,
            normalPrice: Int,
            foodCount: Int,
            foodPrice: Int,
            electronicsCount: Int,
            electronicsPrice: Int,
            bookCount: Int,
            bookPrice: Int,
            otherCount: Int,
            otherPrice: Int
        ): String {
            return "summaryscreen?normalCount=$normalCount&normalPrice=$normalPrice&foodCount=$foodCount&foodPrice=$foodPrice&electronicsCount=$electronicsCount&electronicsPrice=$electronicsPrice&bookCount=$bookCount&bookPrice=$bookPrice&otherCount=$otherCount&otherPrice=$otherPrice"
        }
    }
}