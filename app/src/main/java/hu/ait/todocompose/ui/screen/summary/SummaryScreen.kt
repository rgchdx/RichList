package hu.ait.todocompose.ui.screen.summary

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import hu.ait.todocompose.R

@Composable
fun SummaryScreen(
    modifier: Modifier = Modifier,
    normalItemCount: Int,
    normalItemPrice: Int,
    foodItemCount: Int,
    foodItemPrice: Int,
    electronicsItemCount: Int,
    electronicsItemPrice: Int,
    bookItemCount: Int,
    bookItemPrice: Int,
    otherItemCount: Int,
    otherItemPrice: Int
) {
    val totalItemCount = foodItemCount+electronicsItemCount+bookItemCount+otherItemCount
    val totalPrice = foodItemPrice+electronicsItemPrice+bookItemPrice+otherItemPrice

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Text(
            text = stringResource(R.string.summary),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        CategorySummaryCard(
            category = stringResource(R.string.food),
            itemCount = foodItemCount,
            itemPrice = foodItemPrice
        )
        CategorySummaryCard(
            category = stringResource(R.string.electronics),
            itemCount = electronicsItemCount,
            itemPrice = electronicsItemPrice
        )
        CategorySummaryCard(
            category = stringResource(R.string.books),
            itemCount = bookItemCount,
            itemPrice = bookItemPrice
        )
        CategorySummaryCard(
            category = stringResource(R.string.other),
            itemCount = otherItemCount,
            itemPrice = otherItemPrice
        )
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF27B20)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ){
            Column(
                modifier = Modifier.padding(16.dp)
            ){
                Text(
                    text = stringResource(R.string.overall_totals),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFDAE812)
                )
                Row(modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween){
                    Text(
                        text = stringResource(R.string.total_items, totalItemCount),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFDAE812)
                    )
                    Text(
                        text = stringResource(R.string.total_price, totalPrice),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFDAE812)
                    )
                }
            }
        }

    }
}

@Composable
fun CategorySummaryCard(
    category: String,
    itemCount: Int,
    itemPrice: Int
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF27B20)),
        modifier = Modifier
            .fillMaxWidth()
    ){
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = stringResource(R.string.items, category),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.count, itemCount),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray
                )
            }
            Text(
                text = stringResource(R.string.total, itemPrice),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }

    }
}