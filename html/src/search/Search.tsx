
import * as React from 'react';



import {SearchResult} from './SearchModel'
import {SearchResultItem} from './SearchModel'

const ListItemView: React.SFC<any> = (props) =>
    <li key={props.id}>
      <a href={"/sku/" + props.id}>
        {props.name}
      </a>
      <button onClick={() => props.add(props.id)}>Add</button>
    </li>

  ;

const jsonInit = {
  searchText: '',
  total: 0,
  searchResult: []
};

class Search extends React.Component<{}, SearchResult> {

  urlSearch: string = 'http://localhost:9000/sku/search';
  urlAdd: string = 'http://localhost:9000/prepare/cart/add'

  constructor(props: any) {
    super(props);
    this.state = jsonInit;
  }

  add(id: string) {
    console.log('Add ', id);
    fetch(this.urlAdd, {
      method: 'PUT',
      referrerPolicy: "unsafe-url",
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({id: id, quantity: 0})
    })
    .then((response) => { return response.json(); } )
    .then((response) => {
      console.log('Add rsp ', response);
    });
  }

  handleSearch(event: any) {
    this.setState({
      searchText: event.target.value,
      searchResult: []
    });
    fetch(this.urlSearch, {
      method: 'POST',
      referrerPolicy: "unsafe-url",
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({q: event.target.value})
    })
    .then((response) => { return response.json(); } )
    .then((response) => {
      console.log('response', response);
      this.setState(
        {
          searchText: this.state.searchText,
          searchResult: response.items,
          total: response.total
        }
      )
    });
  }

  render() {
    return (
      <main id="search">
          <input
            id="search-input"
            value={this.state.searchText}
            onChange={this.handleSearch.bind(this)}
          />
          <p>Total Found {this.state.total}</p>
          <ul>
            {this.state.searchResult.map((item, i) =>
              <ListItemView key={item.id} add={(id: string) => this.add(id)} {...item}/>
            )}
          </ul>
      </main>
    );
  }
}

export default Search;
