import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    let appContainer: ObservableValueWrapper<AppContainer>
    
    init() {
        self.appContainer = ObservableValueWrapper<AppContainer>(
            value: AppContainer(factory: Factory())
        )
    }
    
    var body: some Scene {
        WindowGroup {
            ViewModelStoreOwnerProvider {
                ContentView()
            }
            .environmentObject(appContainer)
        }
    }
}
