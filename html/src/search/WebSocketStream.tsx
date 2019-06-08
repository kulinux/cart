
import xs, { Producer, Listener } from 'xstream'
import { Stream } from 'xstream';
import { SearchResultItem } from './Model';


export class WebSocketStream {
    connection: WebSocket;

    constructor(url: string) {
        this.connection = new WebSocket(url);
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
}
