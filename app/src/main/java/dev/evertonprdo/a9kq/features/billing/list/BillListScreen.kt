package dev.evertonprdo.a9kq.features.billing.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun BillListScreen(
    viewModel: BillListViewModel = BillListViewModel.create(),
    onRequestAddBill: () -> Unit
) {
    val bills by viewModel.bills.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 100.dp, start = 16.dp, end = 16.dp)
        ) {

            item {
                Text(
                    text = "Bill's History",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillParentMaxWidth()
                )
            }

            items(items = bills) { bill ->

                Card() {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp, 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Curr: " + bill.currRead.toString())
                        Text("Next: " + bill.nextRead.toString())
                        Text("Amount: " + bill.amount.toString())
                        Text("Total: " + bill.total.toString())
                    }
                }
            }
        }

        Button(
            onClick = onRequestAddBill, modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) { Text("Add Bill ->") }
    }
}