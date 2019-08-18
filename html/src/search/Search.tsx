
import * as React from 'react';

import TextField from '@material-ui/core/TextField';

import Container from '@material-ui/core/Container';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';

import ListItemAvatar from '@material-ui/core/ListItemAvatar';
import ListItemSecondaryAction from '@material-ui/core/ListItemSecondaryAction';
import ListItemText from '@material-ui/core/ListItemText';
import Avatar from '@material-ui/core/Avatar';
import IconButton from '@material-ui/core/IconButton';
import FolderIcon from '@material-ui/icons/Folder';
import DeleteIcon from '@material-ui/icons/Delete';

import {SearchResult} from './SearchModel'
import {SearchResultItem} from './SearchModel'

const ListItemView: React.SFC<SearchResultItem> = (props) => <ListItem key={props.id}>
    <ListItemAvatar>
      <Avatar>
        <FolderIcon />
      </Avatar>
    </ListItemAvatar>
    <ListItemText
      primary={props.name}
      secondary={'Secondary text ' + props.id}
    />
    <ListItemSecondaryAction>
      <IconButton edge="end" aria-label="Delete">
        <DeleteIcon />
      </IconButton>
    </ListItemSecondaryAction>
  </ListItem>;

const jsonInit = {
  searchText: '',
  total: 0,
  searchResult: []
};

class Search extends React.Component<{}, SearchResult> {

  url: string = 'http://localhost:9000/sku/search';

  constructor(props: any) {
    super(props);
    this.state = jsonInit;
  }

  handleSearch(event: any) {
    this.setState({
      searchText: event.target.value,
      searchResult: []
    });
    fetch(this.url, {
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
      <Container id="search">
          <TextField
            id="search-input"
            label="Search"
            margin="normal"
            fullWidth
            value={this.state.searchText}
            onChange={this.handleSearch.bind(this)}
          />
          <p>Total Found {this.state.total}</p>
          <List>
            {this.state.searchResult.map((item, i) =>
              <ListItemView key={item.id} {...item}/>
            )}
            </List>
      </Container>
    );
  }
}

export default Search;
