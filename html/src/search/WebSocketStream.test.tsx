import xs, { Listener } from 'xstream'
import { WebSocket, Server } from 'mock-socket';
import { WebSocketStream } from './WebSocketStream';
import { SearchResultItem } from './Model';


it('WebSocket to Stream', (done: any) => {

    const fakeURL = 'ws://localhost:8080';
    const mockServer = new Server(fakeURL);
    
    mockServer.on('connection', (socket: WebSocket) => {
      let i = 0;
      setInterval(() => {
          console.log('Envio!!!');
          socket.send(
              JSON.stringify(
                  {id: 'ID' + i , name: 'Name ' + 1}
              )
          );
          i = i + 1;
      }, 100);


    });

    const app = new WebSocketStream(fakeURL);

    const stream = app.openStream();

    var  listener : Listener<SearchResultItem> = 
    {
        next: (sr: SearchResultItem) => console.log('next ', sr.id),
        error: (err: any) => console.error(err),
        complete: () => {
            console.log('completed');
            done();
        }
    }

    stream.addListener( listener );


    
    // NOTE: this timeout is for creating another micro task that will happen after the above one
    setTimeout(() => {  
      mockServer.stop(done);
    }, 1000);
});