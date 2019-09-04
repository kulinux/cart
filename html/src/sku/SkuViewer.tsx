
import * as React from 'react'
import './SkuViewer.css';
import {Sku} from './SkuModel'

const jsonInit = {
  id: '',
  name: '',
  attr: new Map<string, string>(
    [
      ['', '']
    ]
  )
};


class SkuViewer extends React.Component<{}, Sku> {

  urlSku: string = 'http://localhost:9000/sku/';


  constructor(props: any) {
    super(props);
    this.state = {...jsonInit, id: props.match.params.id};
  }


  componentDidMount() {
    fetch(this.urlSku + this.state.id, {
      method: 'GET',
      referrerPolicy: "unsafe-url",
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
      }
    })
    .then((response) => { return response.json(); } )
    .then((response) => {
      let map = new Map<string, string>();
      for( let key in response.attr ) {
        map.set(key, response.attr[key]);
      }

      this.setState({
        id: response.attr['code'],
        name: response.attr['product_name'],
        attr: map
      });
      console.log('Json ', response);
    });

  }

  renderAttr(attr: Map<string, string>) {
    return Array.from(attr)
      .map((item, i) => 
        <li key={item[0]} className="sku-attr">
          <span className="sku-attr-label">{item[0]}</span>
          <span className="sku-attr-value">{item[1]}</span>
        </li>
      )
  }

  render() {
    return (
      <div className="sku-container">
        <article className="sku-main">
          <header>
            {this.state.name}
            <img src={this.state.attr.get("image_url")}/>
            <img src={this.state.attr.get("image_ingredients_url")}/>
          </header>
          <main>
            <p>Ingredientes</p>
            <ul className="sku-attrs">
              {this.renderAttr(this.state.attr)}
            </ul>
          </main>
        </article>
        <aside className="sku-aside">
          Aside
        </aside>
      </div>
    );
  }
}

export default SkuViewer;