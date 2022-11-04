import { Grid } from '@mui/material';
import React from 'react';
import PageWrapper from '../Util/PageWrapper';
import SearchInput from './SearchInput/SearchInput';
const Search = () => {
    return (
        <PageWrapper>
            <Grid container justifyContent="center">
                <Grid item xs={12} md={8}>
                    <SearchInput />
                </Grid>
                <Grid item>

                </Grid>
            </Grid>
        </PageWrapper>
        
    );
}

export default Search;