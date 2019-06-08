
import xs, { Listener } from 'xstream'
import { WebSocket, Server } from 'mock-socket';


class ChatApp {
    messages: Array<string>;
    connection: WebSocket;

    constructor(url: string) {
      this.messages = [];
      this.connection = new WebSocket(url);
      
      this.connection.onmessage = (event: any) => {
          console.log('Message from server ', event.data);
          this.messages.push(event.data);
      };
    }
    
    sendMessage(message: string) {
      this.connection.send(message);
    }
  }


it('WebSocket should be mocked', (done: any) => {
    const fakeURL = 'ws://localhost:8080';
    const mockServer = new Server(fakeURL);
    
    mockServer.on('connection', (socket: WebSocket) => {
      let i = 0;
      setInterval(() => {
          console.log('Envio!!!');
          socket.send('Envio ' + i)
          i = i + 1;
      }, 100);
      /*
      socket.on('message', (data: any) => {
        console.log('Message from client ', data);
        socket.send('test message from mock server');
      });
      */
    });
    
    const app = new ChatApp(fakeURL);
    app.sendMessage('test message from app'); // NOTE: this line creates a micro task
    
    // NOTE: this timeout is for creating another micro task that will happen after the above one
    setTimeout(() => {  
      mockServer.stop(done);
    }, 1000);
});