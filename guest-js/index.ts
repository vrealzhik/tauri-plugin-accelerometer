import { invoke, addPluginListener } from "@tauri-apps/api/core";

export async function onUpdate(handler: (event: any) => void) {
  return await addPluginListener("accelerometer", "update", handler);
}
