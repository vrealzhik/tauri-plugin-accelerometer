use serde::{Deserialize, Serialize};

#[derive(Debug, Clone, Default, Deserialize, Serialize)]
#[serde(rename_all = "camelCase")]
pub struct AccelerometerResult {
    pub x: f64,
    pub y: f64,
    pub z: f64,
    pub timestamp: i64,
}
