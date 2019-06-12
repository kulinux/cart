import React from 'react';

export interface SearchResultItem {
    id: string;
    name: string;
}

export interface SearchResult {
    searchText: string;
    searchResult: Array<SearchResultItem>;
}

export interface InputSearch{
    text: string
}

export interface SearchResultItemCommand {
    command: string;
    sri: SearchResultItem;
}



//export default SearchResult;