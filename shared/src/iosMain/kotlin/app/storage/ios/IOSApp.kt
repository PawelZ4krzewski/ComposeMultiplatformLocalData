package app.storage.ios

import androidx.compose.ui.window.ComposeUIViewController
import app.storage.ui.App
import platform.UIKit.UIViewController

fun createIOSApp(): UIViewController {
    return ComposeUIViewController { App() }
}
