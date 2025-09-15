import SwiftUI
import UIKit
import shared

struct ComposeViewController: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        let app = IOSAppKt.createIOSApp()
        return app
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }
}
