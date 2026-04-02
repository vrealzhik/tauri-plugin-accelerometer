use serde::de::DeserializeOwned;
use tauri::{plugin::PluginApi, AppHandle, Runtime};

use crate::models::*;

pub fn init<R: Runtime, C: DeserializeOwned>(
    app: &AppHandle<R>,
    _api: PluginApi<R, C>,
) -> crate::Result<Accelerometer<R>> {
    Ok(Accelerometer(app.clone()))
}

/// Access to the accelerometer APIs.
pub struct Accelerometer<R: Runtime>(AppHandle<R>);

impl<R: Runtime> Accelerometer<R> {

    pub fn start_listening(&self) -> crate::Result<AccelerometerResult> {
        Ok(AccelerometerResult::default())
    }

    pub fn stop_listening(&self) -> crate::Result<AccelerometerResult> {
        Ok(AccelerometerResult::default())
    }
}
