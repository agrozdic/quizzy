// import modula
const express = require('express');
const http = require('http');
const socketIO = require('socket.io');

// inicijalizacija express-a i pravljenje servera
const app = express();
const server = http.createServer(app);
const io = socketIO(server, {
  cors: {
    origin: '*',
    methods: ['GET', 'POST']
  }
});

// pokretanje servera
const port = 3000;
server.listen(port, '192.168.1.6', () => {
  console.log(`Server running on port ${port}`);
});

// model
class KoZnaZnaAnswer {
  constructor(correct, timeOf, player) {
      this.correct = correct;
      this.timeOf = timeOf;
      this.player = player;
  }
}

// podaci za igru
let players = [];
let playerSocket = {};
let scores = {};
let currentTurn = 0;
let koZnaZnaAnswers = [];
let switcher = 0;

app.use(express.static('public'));

// eventi
io.on('connection', (socket) => {
  console.log('A user connected with socket id: ' + socket.id);

  socket.on('joinGame', (playerName) => {
    if (players.length < 2) {
      players.push(playerName);
      playerSocket[socket.id] = playerName;
      scores[playerName] = 0;
      console.log('Player joined: ' + playerName + '. Total players: ' + players.length);
      io.emit('playerJoined', playerName);

      if (players.length === 2) {
        io.emit('startGame', playerSocket);
      }
    }
  });

  // globalno za igre
  socket.on('getTurn', () => {
    if (switcher <= 2) {
      if (currentTurn % 4 === 0) {
        io.emit('turn', players[0]);
        console.log(players[0] + ' turn');
      }
      if (currentTurn % 4 === 2) {
        io.emit('turn', players[1]);
        console.log(players[1] + ' turn');
      }
      currentTurn++;
    }
  });

  socket.on('resetTurn', () => {
    currentTurn = 0;
  });

  socket.on('getSwitcher', () => {
    io.emit('switcher', ++switcher);
  });

  socket.on('resetSwitcher', () => {
    switcher = 0;
  });

  // ko zna zna
  socket.on('koZnaZnaAnswer', (correct, timeOf, player) => {
    let answer = new KoZnaZnaAnswer(correct, timeOf, player);
    koZnaZnaAnswers.push(answer);
  });

  socket.on('koZnaZnaAnswerCheck', async () => {
    if (koZnaZnaAnswers.length === 2) {
      console.log(koZnaZnaAnswers);
      let answer1 = koZnaZnaAnswers[0];
      let answer2 = koZnaZnaAnswers[1];

      if ((answer1.correct && answer2.correct && answer1.timeOf < answer2.timeOf)
          || (answer1.correct && !answer2.correct)) {
        scores[answer1.player] += 10;
      } else if (answer2.correct && answer1.correct && answer2.timeOf < answer1.timeOf
        || (answer2.correct && !answer1.correct)) {
        scores[answer2.player] += 10;
      }

      if (!answer1.correct) {
        scores[answer1.player] -= 5;
      }

      if (!answer2.correct) {
        scores[answer2.player] -= 5;
      }

      io.emit('scoreUpdate', scores);
      console.log(scores);
      koZnaZnaAnswers = [];
    } else if (koZnaZnaAnswers.length === 1) {
      console.log(koZnaZnaAnswers);
      let answer1 = koZnaZnaAnswers[0];

      if (answer1.correct) {
        scores[answer1.player] += 10;
      }

      if (!answer1.correct) {
        scores[answer1.player] -= 5;
      }

      io.emit('scoreUpdate', scores);
      console.log(scores);
      koZnaZnaAnswers = [];
    }
  });

  // spojnice
  socket.on('spojniceSolution', (solved) => {
    io.emit('spojniceUpdate', solved);
  });

  socket.on('spojniceScoreUpdate', (player1, score1, player2, score2) => {
    scores[player1] = score1;
    scores[player2] = score2;

    io.emit('scoreUpdate', scores);
    console.log(scores);
  });

  socket.on('endSpojnice', () => {
    io.emit('endSpojnice');
    console.log('End of spojnice');
  });

  // diskonekcija
  socket.on('disconnect', () => {
    console.log('A user disconnected');

    const name = playerSocket[socket.id];
    const index = players.indexOf(name);
    players.splice(index, 1);
    delete scores[name];

    io.emit('playerLeft', name);
  });
});
