import UIKit
import SwiftUI
import ComposeApp

// 1. 自定义容器控制器
class ComposeContainerViewController: UIViewController {
    private var statusBarBackgroundView: UIView!
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        // 关键：在视图可见后再添加背景视图（确保窗口已初始化）
        setupStatusBarBackgroundView()
    }
    
    private func setupStatusBarBackgroundView() {
        // 获取状态栏高度（兼容刘海屏）
        let statusBarHeight = view.window?.windowScene?.statusBarManager?.statusBarFrame.height ?? 20
        // 颜色 0xFFF7F8FA
        let statusBarColor = UIColor(
            red: 0xF7 / 255.0,
            green: 0xF8 / 255.0,
            blue: 0xFA / 255.0,
            alpha: 1.0
        )
        
        // 创建背景视图
        statusBarBackgroundView = UIView(frame: CGRect(
            x: 0,
            y: 0,
            width: view.bounds.width,
            height: statusBarHeight
        ))
        statusBarBackgroundView.backgroundColor = statusBarColor
        statusBarBackgroundView.tag = 999 // 标记避免重复添加
        
        // 确保只添加一次
        if view.viewWithTag(999) == nil {
            view.addSubview(statusBarBackgroundView)
            view.bringSubviewToFront(statusBarBackgroundView) // 关键：置于顶层
        }
    }
    
    // 状态栏文字黑色
    override var preferredStatusBarStyle: UIStatusBarStyle {
        return .darkContent
    }
}

// 2. 包装为 SwiftUI 组件
struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        let containerVC = ComposeContainerViewController()
        // 添加 Compose 视图
        let composeVC = MainViewControllerKt.MainViewController()
        containerVC.addChild(composeVC)
        composeVC.view.frame = containerVC.view.bounds
        containerVC.view.addSubview(composeVC.view)
        composeVC.didMove(toParent: containerVC)
        return containerVC
    }
    
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

// 3. 主视图
struct ContentView: View {
    var body: some View {
        ComposeView()
            .edgesIgnoringSafeArea(.top) // 允许背景视图覆盖状态栏区域
    }
}
