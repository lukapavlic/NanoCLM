import { Grid } from '@mui/material';
import React from 'react';
import IContactResponse from '../../types/contact/contactResponse.type';
import PageWrapper from '../Util/PageWrapper';
//import SearchInput from './SearchInput/SearchInput';
import SearchResultsTable from './SearchResults/SearchResultsTable';

interface SearchProps {
    searchString: String,
    searchResults: Array<IContactResponse>
}

const Search = (props: SearchProps) => {
    return (
        <PageWrapper>
            <Grid container justifyContent="center">

                <Grid item xs={12} md={8}>
                    <p>Search for string: "<b>{props.searchString}</b>"</p>
                    
                </Grid>
                <Grid item xs={12}>
                    <SearchResultsTable searchString={props.searchString} searchResults={props.searchResults} />
                </Grid>
            </Grid>
        </PageWrapper>

    );
}

export default Search;