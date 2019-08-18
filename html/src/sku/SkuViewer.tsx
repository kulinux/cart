
import * as React from 'react'
import './SkuViewer.css';
import {Sku} from './SkuModel'

const jsonInit = {
  id: 'XXXXXXXX',
  name: 'Nocilla',
  attr: new Map<string, string>(
    [
      ['aceites', 'naturales'],
      ['cereales', 'avellanas, almendras'],
      ['vitaminas', 'C,D,E']
    ]
  )
};


class SkuViewer extends React.Component<{}, Sku> {

  constructor(props: any) {
    super(props);
    this.state = jsonInit;
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