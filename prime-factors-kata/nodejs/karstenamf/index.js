let express = require('express');
let app = express();

app.use(function(req, res, next) {
  res.header("Access-Control-Allow-Origin", "http://localhost:8080");
  res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
  next();
});
app.listen(8080);
console.log("Server running");

app.get('/generate/:n', (req, res) => {
  let number = req.params.n
  let resp = []
  for(primediv = 2; number != 1; primediv++) {
    while(number % primediv == 0) {
      number = number / primediv
      resp.push(primediv)
    }
  }
  res.send(resp)
});
