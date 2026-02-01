const grpc = require("@grpc/grpc-js");
const protoLoader = require("@grpc/proto-loader");

const packageDef = protoLoader.loadSync("./location.proto", {
  keepCase: true,
});

const proto = grpc.loadPackageDefinition(packageDef).location;

const client = new proto.LocationService(
  "localhost:9093",
  grpc.credentials.createInsecure(),
);

const stream = client.streamDriverLocation((err, ack) => {
  if (err) console.error(err);
  else console.log("ACK:", ack);
});

stream.on("error", console.error);

const driverId = "driver-123";

setInterval(() => {
  const lat = 18.5204 + Math.random() * 0.001;
  const lng = 73.8567 + Math.random() * 0.001;

  stream.write({
    driver_id: driverId,
    lat,
    lng,
    available: true,
  });

  console.log("Sent location:", lat, lng);
}, 1000);

setTimeout(() => {
  stream.end();
}, 30000);
