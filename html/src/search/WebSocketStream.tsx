
import xs, { Producer, Listener } from 'xstream'
import { Stream } from 'xstream';
import { SearchResultItem } from './Model';


export class WebSocketStream {
    connection: WebSocket;

    constructor(url: string) {
        this.connection = new WebSocket(url);
    }

    send(msg: object) {
        this.connection.send(JSON.stringify(msg));
    }

    openStream(): Stream<SearchResultItem> {
        let con = this.connection;
        let producer: Producer<SearchResultItem> = {
            start: function (listener: Listener<SearchResultItem>) {
                con.onmessage = (event: MessageEvent) => {
                    listener.next(JSON.parse(event.data));
                };
            },
    
            stop: function () {
                console.log('stop');
            },
        }

        return xs.create(producer);
        
    }

    buildListener(cb: (sr: SearchResultItem) => void): Listener<SearchResultItem> {
        var  listener : Listener<SearchResultItem> = 
        {
            next: (sr: SearchResultItem) => console.log('next ', sr.id),
            error: (err: any) => console.error(err),
            complete: () => {
                console.log('completed');
            }
        }
        return listener;
    }
}
