import * as React from "react";
import { useState } from "react";
import TextField from "@mui/material/TextField";
import ISearch from "../../../types/search/search.type";
import SearchDataService from "../../../services/search.service";
import ISearchResponse from "../../../types/search/searchResponse.type";

const textFieldStyle = {
  width: "100%",
};

const SearchInput = () => {
  const [searchRes, setSearchRes] = useState<ISearchResponse | null>(null);
  const [wordEntered, setWordEntered] = useState<string>("");

  const handleSearch = async (
    target: React.ChangeEvent<HTMLInputElement>
  ): Promise<void> => {
    const searchWord = target.target.value;
    setWordEntered(searchWord);

    const data: ISearch = {
      headers: {
        //TODO: get from auth
        userToken: "user@clm.com", //TODO: user must be under allowed users (in database)
        tenantUniqueName: searchWord,
      },
      body: { sortBy: "name" }, //TODO: check...
    };

    try {
      const searchResponse = await SearchDataService.create(data);
      setSearchRes(searchResponse);
    } catch (error) {
      //dont know what all of the exceptions from spring are...
      console.log(error);
    }

    console.log(searchRes);
  };

  return (
    <TextField
      id="outlined-basic"
      label="Iskanje"
      variant="outlined"
      style={textFieldStyle}
      value={wordEntered}
      onChange={handleSearch}
      placeholder="Curently only tenant unique name is supported (must be in database)..."
    />
  );
};

export default SearchInput;
