package dev.evertonprdo.a9kq

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dev.evertonprdo.a9kq.di.ServiceLocator
import dev.evertonprdo.a9kq.domain.usecases.RemoveMeterReadingUseCase
import dev.evertonprdo.a9kq.ui.theme.Theme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        ServiceLocator.initialize(applicationContext)

        // TODO: Better manage pending removals
        ServiceLocator.appCoroutineScope.launch {
            RemoveMeterReadingUseCase(
                meterReadingRepository = ServiceLocator.meterReadingRepository,
                pendingMeterReadingsDataStore = ServiceLocator.pendingMeterReadingsDataStore
            )()
        }

        setContent {
            Theme() {
                App()
            }
        }
    }
}
