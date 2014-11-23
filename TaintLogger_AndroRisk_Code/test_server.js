var restify = require("restify");

var server = restify.createServer();


var logIMEI = function (request, response, next) {
	console.log("Recieved IMEI: " + request.params.imei);
	
	response.send({ success: true });
};

var logLocation = function (request, response, next) {
	console.log("Recieved Location: " + request.params.location);
	
	response.send({ success: true });
};

server.get("/imei/:imei", logIMEI);
server.get("/location/:location", logLocation);

// Start server
server.listen(8081, function() {
	console.log("%s listening at %s", server.name, server.url);
});