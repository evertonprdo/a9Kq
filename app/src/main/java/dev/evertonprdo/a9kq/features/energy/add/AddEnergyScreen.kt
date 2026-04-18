package dev.evertonprdo.a9kq.features.energy.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AddEnergyScreen(
    viewModel: AddEnergyViewModel = viewModel(factory = AddEnergyViewModel.factory),
    onSubmit: () -> Unit
) {
    fun handleOnSubmit() {
        viewModel.submit()
        onSubmit()
    }

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
                value = viewModel.amount,
                onValueChange = { viewModel.onAmountChange(it) }
            )

            Button(
                onClick = ::handleOnSubmit,
                enabled = viewModel.canBeSubmitted(),
                modifier = Modifier
                    .fillMaxWidth(0.75f)
            ) { Text("Submit") }
        }
    }
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