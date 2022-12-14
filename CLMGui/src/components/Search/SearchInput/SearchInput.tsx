import * as React from "react";
import TextField from "@mui/material/TextField";

const textFieldStyle = {
  width: "100%",
};

const SearchInput = () => {
 

  return (
    <TextField
      id="outlined-basic"
      label="Iskanje"
      variant="outlined"
      style={textFieldStyle}
      placeholder="Curently only tenant unique name is supported (must be in database)..."
    />
  );
};

export default SearchInput;
