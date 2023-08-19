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
server.listen(port, '192.168.1.3', () => {
  console.log(`Server running on port ${port}`);
});

let players = [];
let scores = {};
let currentTurn = 0;

app.use(express.static('public'));

// eventi
io.on('connection', (socket) => {
  console.log('A user connected with socket id: ' + socket.id);

  socket.emit('gameData', { currentTurn, scores });

  socket.on('joinGame', (playerName) => {
    if (players.length < 2) {
      players.push(playerName);
      scores[playerName] = 0;
      io.emit('playerJoined', playerName);

      if (players.length === 2) {
        io.emit('startGame');
      }
    }
  });

  // TODO - samo red i bodove da vraca
  socket.on('answerQuestion', (answer) => {
    const currentPlayer = players[currentTurn];
    if (currentPlayer === socket.id) {
      // Check if the answer is correct
      // (Implement your own logic here)

      // Update the score
      if (answerIsCorrect) {
        scores[currentPlayer]++;
      }

      // Increment the turn
      currentTurn = (currentTurn + 1) % 2;

      // Emit the updated scores and current turn
      io.emit('scoreUpdate', scores);
      io.emit('turnChange', currentTurn);
    }
  });

  socket.on('disconnect', () => {
    console.log('A user disconnected');
    const index = players.indexOf(socket.id);
    if (index !== -1) {
      const playerName = players[index];
      players.splice(index, 1);
      delete scores[playerName];
      io.emit('playerLeft', playerName);
    }
  });
});
