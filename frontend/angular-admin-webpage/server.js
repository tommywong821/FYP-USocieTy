function requireHTTPS(req, res, next) {
  // The 'x-forwarded-proto' check is for Heroku
  if (!req.secure && req.get('x-forwarded-proto') !== 'https') {
    return res.redirect('https://' + req.get('host') + req.url);
  }
  next();
}

const express = require('express');
const path = require('path');

const app = express();
const port = 4200;

app.use(requireHTTPS);
app.use(express.static(__dirname + '/dist/ngok3fyp-frontend'));

app.get('/*', function (req, res) {
  res.sendFile(path.join(__dirname + '/dist/ngok3fyp-frontend/index.html'));
});

app.listen(process.env.PORT || port, () => {
  console.log(`Example app listening on port ${port}`);
});
