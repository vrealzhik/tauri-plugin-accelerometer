use tauri::{
    plugin::{Builder, TauriPlugin},
    Manager, Runtime,
};

pub use models::*;

#[cfg(desktop)]
mod desktop;
#[cfg(mobile)]
mod mobile;

mod commands;
mod error;
mod models;

pub use error::{Error, Result};

#[cfg(desktop)]
use desktop::Accelerometer;
#[cfg(mobile)]
use mobile::Accelerometer;

/// Extensions to [`tauri::App`], [`tauri::AppHandle`] and [`tauri::Window`] to access the accelerometer APIs.
pub trait AccelerometerExt<R: Runtime> {
    fn accelerometer(&self) -> &Accelerometer<R>;
}

impl<R: Runtime, T: Manager<R>> crate::AccelerometerExt<R> for T {
    fn accelerometer(&self) -> &Accelerometer<R> {
        self.state::<Accelerometer<R>>().inner()
    }
}

/// Initializes the plugin.
pub fn init<R: Runtime>() -> TauriPlugin<R> {
    Builder::new("accelerometer")
        .invoke_handler(tauri::generate_handler![
            commands::start_listening,
            commands::stop_listening
        ])
        .setup(|app, api| {
            #[cfg(mobile)]
            let accelerometer = mobile::init(app, api)?;
            #[cfg(desktop)]
            let accelerometer = desktop::init(app, api)?;
            app.manage(accelerometer);
            Ok(())
        })
        .build()
}
