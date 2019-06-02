import React from 'react';
import ReactDOM from 'react-dom';
import Stream from './Stream';
import xs from 'xstream'


it('Stream', (done: any) => {
  /*
  const div = document.createElement('div');
  ReactDOM.render(<App />, div);
  ReactDOM.unmountComponentAtNode(div);
  */
  console.log("Test is working!!!");

  var stream = xs.periodic(100)
    .filter(i => i % 2 === 0)
    .map(i => i * i)
    .endWhen(xs.periodic(1000).take(1));

  stream.addListener({
    next: i => console.log(i),
    error: err => console.error(err),
    complete: () => {
      console.log('completed'),
      done();
    }
  });




});