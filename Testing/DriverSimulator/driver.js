const WebSocket = require("ws");

const driverId = "driver-1";

const socket = new WebSocket(`ws://localhost:8081/ws?driverId=${driverId}`);

let lat = 12.5302;
let lng = 77.8459;

socket.on("open", () => {
  console.log(`🚗 Driver ${driverId} connected to WebSocket`);

  // Send location every 3 seconds
  setInterval(() => {
    // simulate small movement
    lat += (Math.random() - 0.5) * 0.001;
    lng += (Math.random() - 0.5) * 0.001;

    const payload = {
      driverId: driverId,
      lat: lat,
      lng: lng,
      available: true,
    };

    socket.send(JSON.stringify(payload));

    console.log("📍 Sent location:", payload);
  }, 3000);
});

socket.on("message", (data) => {
  console.log("📩 Message from server:", data.toString());
});

socket.on("close", () => {
  console.log("❌ Connection closed");
});

socket.on("error", (err) => {
  console.error("⚠️ Socket error:", err.message);
});
