
import xs, { Producer, Listener } from 'xstream'
import { Stream } from 'xstream';
import { SearchResultItem, SearchResultItemCommand } from './Model';


export class WebSocketStream {
    connection: WebSocket;

    constructor(url: string) {
        this.connection = new WebSocket(url);
    }

    send(msg: object) {
        if( this.connection.readyState ) {
            this.connection.send(JSON.stringify(msg));
        } else {
            setTimeout(() => this.send(msg), 100);
        }
    }

    openStream(): Stream<SearchResultItemCommand> {
        let con = this.connection;
        let producer: Producer<SearchResultItemCommand> = {
            start: function (listener: Listener<SearchResultItemCommand>) {
                con.onmessage = (event: MessageEvent) => {
                    console.log('Message raw', event.data);
                    listener.next(JSON.parse(event.data));
                };
            },
    
            stop: function () {
                console.log('stop');
            },
        }

        return xs.create(producer);
        
    }

    buildListener(cb: (sr: SearchResultItemCommand) => void): Listener<SearchResultItemCommand> {
        var  listener : Listener<SearchResultItemCommand> = 
        {
            next: (sr: SearchResultItemCommand) => {
                console.log('next ', sr)
                cb(sr);
            },
            error: (err: any) => console.error(err),
            complete: () => {
                console.log('completed');
            }
        }
        return listener;
    }
}
