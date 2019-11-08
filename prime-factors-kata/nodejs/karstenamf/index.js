let express = require('express');
let app = express();

app.use(function (req, res, next) {
  res.header("Access-Control-Allow-Origin", "http://localhost:8080");
  res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
  next();
});
app.listen(8080);
console.log("Server running");

app.get('/generate/:n', (req, res) => {
  let number = Number(req.params.n)
  let resp = []

  while (number % 2 === 0) {
    resp.push(2)
    number /= 2
  }

  for(i = 3; i <= Math.sqrt(number); i += 2) {
    while(number % i === 0) {
      resp.push(i)
      number /= i
    }
  }

  if(number > 2) {
    resp.push(number)
  }

  res.send(resp)
});
