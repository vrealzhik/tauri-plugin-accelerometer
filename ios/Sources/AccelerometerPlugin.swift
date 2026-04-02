import Tauri
import UIKit
import CoreMotion

@objc(AccelerometerPlugin)
class AccelerometerPlugin: Plugin {
    private let motionManager = CMMotionManager()

    @objc func startListener(_ invoke: Invoke) {
        guard motionManager.isAccelerometerAvailable else {
            invoke.reject("Accelerometer not available")
            return
        }

        motionManager.accelerometerUpdateInterval = 1.0 / 60.0
        motionManager.startAccelerometerUpdates(to: .main) { [weak self] data, error in
            guard let data = data, error == nil else { return }

            let payload: [String: Any] = [
                "x": data.acceleration.x,
                "y": data.acceleration.y,
                "z": data.acceleration.z,
                "timestamp": Date().timeIntervalSince1970 * 1000
            ]
            self?.trigger("accelerometer:update", payload)
        }
        invoke.resolve()
    }

    @objc func stopListener(_ invoke: Invoke) {
        motionManager.stopAccelerometerUpdates()
        invoke.resolve()
    }
}