
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


interface SearchResultItem {
  id: string;
  name: string;
}
interface SearchState {
  searchText: string;
  searchResult: Array<SearchResultItem>;
}


const ListItemView: React.SFC<SearchResultItem> = (props) => <ListItem key={props.id}>
    <ListItemAvatar>
      <Avatar>
        <FolderIcon />
      </Avatar>
    </ListItemAvatar>
    <ListItemText
      primary={props.name}
      secondary={'Secondary text'}
    />
    <ListItemSecondaryAction>
      <IconButton edge="end" aria-label="Delete">
        <DeleteIcon />
      </IconButton>
    </ListItemSecondaryAction>
  </ListItem>;

const jsonInit = {
  searchText: '',
  searchResult: [
    { id: '1', name: 'Calamares Roamana' },
    { id: '2', name: 'Micolor' },
    { id: '3', name: 'Papel Bater' },
  ]
};



class Search extends React.Component<{}, SearchState> {

  constructor(props: SearchState) {
    super(props);
    this.state = jsonInit;
  }

  handleSearch(event: any) {
    console.log('SEARCH!!!' + event.target.value);
    this.setState({searchText: event.target.value})
  }

  render() {
    return (
      <Container id="search">
          <TextField
            id="search-input"
            label="Search"
            defaultValue=""
            margin="normal"
            fullWidth
            value={this.state.searchText}
            onChange={this.handleSearch.bind(this)}
          />
          <List>
            {this.state.searchResult.map((item, i) =>
              <ListItemView id="id" name="name"/>
            )}
            </List>
      </Container>
    );
  }
}

export default Search;
