
import React from 'react';

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

import Stream from './Stream';
import xs from 'xstream'


class Search extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      searchText: ''
    };
  }

  handleSearch(event) {
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
            {this.props.searchResult.map((item, i) =>
                <ListItem key={item.id}>
                  <ListItemAvatar>
                    <Avatar>
                      <FolderIcon />
                    </Avatar>
                  </ListItemAvatar>
                  <ListItemText
                    primary={item.name}
                    secondary={'Secondary text'}
                  />
                  <ListItemSecondaryAction>
                    <IconButton edge="end" aria-label="Delete">
                      <DeleteIcon />
                    </IconButton>
                  </ListItemSecondaryAction>
                </ListItem>
            )}
            </List>
      </Container>
    );
  }
}

export default Search;
