package dev.evertonprdo.a9kq

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dev.evertonprdo.a9kq.di.ServiceLocator
import dev.evertonprdo.a9kq.ui.theme.Theme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        ServiceLocator.initialize(applicationContext)

        setContent {
            Theme() {
                App()
            }
        }
    }
}
