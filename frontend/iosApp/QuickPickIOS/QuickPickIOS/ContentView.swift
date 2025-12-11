import SwiftUI
import shared

// struct ContentView: View {
//     var body: some View {
//         Text("Hello from Quick Pick iOS + KMP!")
//             .padding()
//     }
// }

struct ContentView: View {
    var body: some View {
        ComposeView().ignoresSafeArea(.all, edges: .bottom)
    }
}

struct ComposeView: UIViewControllerRepresentable {
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }

    func makeUIViewController(context: Context) -> UIViewController {
        Main_iosKt.MainViewController()
    }
}


struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
