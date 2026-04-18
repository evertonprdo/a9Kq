package dev.evertonprdo.a9kq.features.bill.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.evertonprdo.a9kq.lib.numericvt.NumericVisualTransformation

@Composable
fun AddBillScreen(
    viewModel: AddBillViewModel = AddBillViewModel.create(),
    onSubmit: () -> Unit
) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val numKeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Add Bill",
                style = MaterialTheme.typography.displaySmall
            )

            OutlinedTextField(
                value = viewModel.currRead,
                onValueChange = { viewModel.currRead = it },
                label = { Text("Current Read") },
                isError = !viewModel.isCurrReadValid
            )

            OutlinedTextField(
                value = viewModel.nextRead,
                onValueChange = { viewModel.nextRead = it },
                label = { Text("Next Read") },
                isError = !viewModel.isNextReadValid
            )

            OutlinedTextField(
                value = viewModel.amount,
                onValueChange = { v -> if (v.all { it.isDigit() }) viewModel.amount = v },
                label = { Text("Amount") },
                suffix = { Text("kWh") },
                keyboardOptions = numKeyboardOptions
            )

            OutlinedTextField(
                value = viewModel.total,
                onValueChange = { viewModel.total = it },
                label = { Text("Total") },
                prefix = { Text("R$") },
                visualTransformation = NumericVisualTransformation(),
                keyboardOptions = numKeyboardOptions
            )

            Button(
                onClick = { viewModel.submit(); onSubmit() },
                enabled = viewModel.canSubmit
            ) { Text("Submit") }
        }
    }
}