
import xs, { Listener } from 'xstream'



//Probar socket.io

it('Stream Producer', (done: any) => {

    var producer = {
        start: function (listener: Listener<string>) {
            console.log('start');
            this.id = setInterval(() => { 
                console.log('setInterval');
                listener.next('yo');
            }, 100)
        },

        stop: function () {
            console.log('stop');
            clearInterval(this.id)
        },

        id: 0,
    }

    var stream = xs.create(producer)

    var  listener = 
    {
        next: (i: string) => console.log('next ', i),
        error: (err: any) => console.error(err),
        complete: () => {
            console.log('completed');
            done();
        }
    }

    stream.addListener( listener );
  });