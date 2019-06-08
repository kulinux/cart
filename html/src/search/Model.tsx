import React from 'react';

export interface SearchResultItem {
    id: string;
    name: string;
}

export interface SearchResult {
    searchText: string;
    searchResult: Array<SearchResultItem>;
}



//export default SearchResult;