import React from 'react';

export interface SearchResultItem {
    id: string;
    name: string;
}

export interface SearchResult {
    searchText: string;
    total: number,
    searchResult: Array<SearchResultItem>;
}

export interface InputSearch {
    q: string
}

export interface SearchResultItemCommand {
    command: string;
    sri: SearchResultItem;
}
