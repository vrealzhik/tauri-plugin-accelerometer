use serde::de::DeserializeOwned;
use tauri::{
    plugin::{PluginApi, PluginHandle},
    AppHandle, Runtime,
};

use crate::models::*;

#[cfg(target_os = "ios")]
tauri::ios_plugin_binding!(init_plugin_accelerometer);

// initializes the Kotlin or Swift plugin classes
pub fn init<R: Runtime, C: DeserializeOwned>(
    _app: &AppHandle<R>,
    api: PluginApi<R, C>,
) -> crate::Result<Accelerometer<R>> {
    #[cfg(target_os = "android")]
    let handle = api.register_android_plugin("com.plugin.accelerometer", "AccelerometerPlugin")?;
    #[cfg(target_os = "ios")]
    let handle = api.register_ios_plugin(init_plugin_accelerometer)?;
    Ok(Accelerometer(handle))
}

/// Access to the accelerometer APIs.
pub struct Accelerometer<R: Runtime>(PluginHandle<R>);

impl<R: Runtime> Accelerometer<R> {

    pub fn start_listening(&self) -> crate::Result<AccelerometerResult> {
        self.0
            .run_mobile_plugin("startListening", ())
            .map_err(Into::into)
    }

    pub fn stop_listening(&self) -> crate::Result<AccelerometerResult> {
        self.0
            .run_mobile_plugin("stopListening", ())
            .map_err(Into::into)
    }
}
