const PROXY_CONFIG = [
  {
    context: ["/public", "/private", "/auth"],
    target: "https://localhost:8443",
    secure: false,
    logLevel: "debug",
    changeOrigin: true,
  },
];

module.exports = PROXY_CONFIG;
