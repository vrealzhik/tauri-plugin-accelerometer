use tauri::{command, AppHandle, Runtime};

use crate::models::*;
use crate::AccelerometerExt;
use crate::Result;

#[command]
pub(crate) async fn start_listening<R: Runtime>(app: AppHandle<R>) -> Result<AccelerometerResult> {
    app.accelerometer().start_listening()
}

#[command]
pub(crate) async fn stop_listening<R: Runtime>(app: AppHandle<R>) -> Result<AccelerometerResult> {
    app.accelerometer().stop_listening()
}
