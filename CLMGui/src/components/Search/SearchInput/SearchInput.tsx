import * as React from 'react';
import TextField from '@mui/material/TextField';

const textFieldStyle = {
    width: "100%"
}

const SearchInput = () => {
    return (
        <TextField id="outlined-basic" label="Iskanje" variant="outlined" style={textFieldStyle}/>
    );
}

export default SearchInput;