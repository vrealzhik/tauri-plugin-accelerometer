const COMMANDS: &[&str] = &[
    "start_listening",
    "stop_listening",
    "register_listener",
    "unregister_listener",
];

fn main() {
    tauri_plugin::Builder::new(COMMANDS)
        .android_path("android")
        .ios_path("ios")
        .build();
}
