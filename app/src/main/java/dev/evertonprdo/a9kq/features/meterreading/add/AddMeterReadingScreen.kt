package dev.evertonprdo.a9kq.features.meterreading.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.evertonprdo.a9kq.libs.utils.toDp
import dev.evertonprdo.a9kq.ui.theme.Theme

@Composable
fun AddMeterReadingScreen(
    viewModel: AddMeterReadingViewModel = AddMeterReadingViewModel.create(),
    onSubmit: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Content(
        uiState = uiState,
        onValueChange = viewModel::updateMeterIndex,
        onDismissRequest = viewModel::dismissDialog,
        canBeSubmitted = viewModel.canBeSubmitted,
        onSubmit = { viewModel.submit(onSubmit) }
    )
}

@Composable
private fun Content(
    uiState: AddMeterUiState,
    canBeSubmitted: Boolean,
    onValueChange: (Int?) -> Unit,
    onDismissRequest: () -> Unit,
    onSubmit: () -> Unit
) {
    val submitting = uiState.submittingState

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(IntrinsicSize.Max)
        ) {
            UIntTextField(
                value = uiState.meterIndex,
                onValueChange = onValueChange
            )

            Button(
                onClick = onSubmit,
                enabled = canBeSubmitted,
                modifier = Modifier
                    .fillMaxWidth(0.75f)
            ) {
                if (submitting is AddMeterUiState.Submission.Idle)
                    Text("Submit")
                else
                    CircularProgressIndicator(
                        modifier = Modifier.size(MaterialTheme.typography.labelLarge.fontSize.toDp()),
                        strokeWidth = 2.dp
                    )
            }
        }
    }

    if (submitting is AddMeterUiState.Submission.Failure)
        AlertDialog(
            onDismissRequest = onDismissRequest,
            confirmButton = { TextButton(onDismissRequest) { Text("Confirm") } },
            title = { Text("Something goes wrong") },
            text = { Text("Error: ${submitting.cause.message}") }
        )

}

@Composable
fun UIntTextField(
    value: Int?,
    onValueChange: (Int?) -> Unit,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    var number by rememberSaveable(value) { mutableStateOf(value?.toString() ?: "") }
    var isDirty by remember { mutableStateOf(false) }

    val keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Number,
        imeAction = ImeAction.Send
    )

    OutlinedTextField(
        value = number,
        onValueChange = { newValue ->
            if (newValue.all { it.isDigit() }) {
                number = newValue
                isDirty = true
                onValueChange(newValue.toIntOrNull())
            }
        },
        label = { Text("Record Today's kWh") },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true,
        isError = isDirty && number.none { it.isDigit() },
        shape = MaterialTheme.shapes.large
    )
}

@Preview
@Composable
private fun AddMeterReadingScreenPreview() {
    Theme() {
        Content(
            uiState = AddMeterUiState(),
            canBeSubmitted = false,
            onDismissRequest = {},
            onValueChange = {},
            onSubmit = {}
        )
    }
}