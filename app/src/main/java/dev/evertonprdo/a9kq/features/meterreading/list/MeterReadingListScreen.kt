package dev.evertonprdo.a9kq.features.meterreading.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.evertonprdo.a9kq.R
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime

@Composable
fun MeterReadingListScreen(
    viewModel: MeterReadingListViewModel = viewModel(factory = MeterReadingListViewModel.factory),
    onRequestAddRecord: () -> Unit
) {
    val readings by viewModel.meterReadings.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(
                top = 16.dp,
                end = 16.dp,
                start = 16.dp,
                bottom = 100.dp
            ),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            item {
                Text(
                    text = "Energy Record History",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            items(items = readings, key = { it.readAt }) { item ->
                val readAt = item.readAt
                val local = readAt.toLocalDateTime(TimeZone.currentSystemDefault())
                val formatted = "%02d/%02d at %02dh%02d".format(
                    local.month.number,
                    local.day,
                    local.hour,
                    local.minute
                )

                Card() {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .padding(24.dp, 16.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val style = MaterialTheme.typography.titleMedium
                            Icon(
                                painter = painterResource(R.drawable.calendar),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(style.fontSize.toDp(1.2f))
                            )
                            Text(
                                text = formatted,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = style
                            )
                        }

                        Text(
                            text = item.meterIndex.toString(),
                            fontWeight = FontWeight.Medium,
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                }
            }
        }

        Button(
            onClick = onRequestAddRecord,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp, 16.dp)
        ) { Text("Add New Record") }
    }
}

@Composable
fun TextUnit.toDp(scale: Float = 1f): Dp {
    val density = LocalDensity.current
    val value = this * scale

    return with(density) {
        value.toDp()
    }
}