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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.evertonprdo.a9kq.R
import dev.evertonprdo.a9kq.domain.entities.MeterReading
import dev.evertonprdo.a9kq.features.meterreading.extensions.key
import dev.evertonprdo.a9kq.libs.snackbar.LocalSnackbarDispatcher
import dev.evertonprdo.a9kq.libs.utils.toDp
import dev.evertonprdo.a9kq.ui.components.SwipeToRemoveCard
import kotlinx.coroutines.Job
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime

@Composable
fun MeterReadingListScreen(
    onRequestAddRecord: () -> Unit,
    viewModel: MeterReadingListViewModel = MeterReadingListViewModel.create()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarDispatcher = LocalSnackbarDispatcher.current

    var job by remember { mutableStateOf<Job?>(null) }
    var count by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                MeterReadingListEvent.RemoveFailure -> {
                    job?.cancel()
                    job = snackbarDispatcher.showMessage("Something Goes Wrong")
                    count = 0
                }

                MeterReadingListEvent.ReadMarkedToRemove -> {
                    count++
                    job?.cancel()

                    job = snackbarDispatcher.showMessage(
                        message = "$count read removed",
                        label = "undo",
                        duration = SnackbarDuration.Short,
                        onDismiss = {
                            viewModel.onAction(MeterReadingListAction.DispatchRemovedMeterReadings)
                            count = 0
                        },
                        onAction = {
                            viewModel.onAction(MeterReadingListAction.UndoRemove)
                            count = 0
                        }
                    )
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Content(
            uiState = uiState,
            onAction = viewModel::onAction,
            modifier = Modifier.weight(1f)
        )

        Button(
            onClick = onRequestAddRecord,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp, 16.dp)
        ) { Text("Add New Record") }
    }
}

@Composable
private fun Content(
    uiState: MeterReadingListUiState,
    onAction: (MeterReadingListAction) -> Unit,
    modifier: Modifier = Modifier
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
        modifier = modifier.fillMaxWidth()
    ) {
        item {
            Text(
                text = "Energy Record History",
                style = MaterialTheme.typography.titleMedium
            )
        }

        when (uiState) {
            MeterReadingListUiState.Loading -> item { CircularProgressIndicator() }

            is MeterReadingListUiState.Failure -> item {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Something wrong")
                    Text("Error: ${uiState.cause.message}")
                }
            }

            is MeterReadingListUiState.Standard ->
                items(items = uiState.history, key = { it.key }) { item ->
                    Item(
                        item = item,
                        onRequestRemove = { onAction(MeterReadingListAction.RemoveMeterReading(item)) },
                        modifier = Modifier.fillParentMaxWidth()
                    )
                }

        }
    }
}

@Composable
private fun Item(
    item: MeterReading,
    onRequestRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    val readAt = item.readAt
    val local = readAt.toLocalDateTime(TimeZone.currentSystemDefault())
    val formatted = "%02d/%02d at %02dh%02d".format(
        local.month.number,
        local.day,
        local.hour,
        local.minute
    )

    SwipeToRemoveCard(
        onSwipe = onRequestRemove,
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.padding(24.dp, 16.dp)
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

