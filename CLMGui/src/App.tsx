import React from "react";
import "./App.css";
import Main from "./components/Routing/Main";
import { useEffect } from "react";
import "./assets/css/styles.css";
import SearchNavbar from "./components/Navbar/SearchNavbar";
import ISearch from "./types/search/searchPost.type";
import SearchDataService from "./services/search.service";
import IContactResponse from "./types/contact/contactResponse.type";
import getLocalData from "./services/local_data.service";

function App() {
  const [searchString, setSearchString] = React.useState<string>("");
  const [searchResults, setSearchResults] = React.useState<Array<IContactResponse>>([]);

  useEffect(() => {
    document.title = "nanoCLM";
    setSearchResults(getLocalData(searchString));
  }, []);
  useEffect(() => {
    const data: ISearch = {
      //TODO: get from auth
      userToken: "user@clm.com", //TODO: user must be under allowed users (in database)
      tenantUniqueName: searchString,
      sortBy: "name", //TODO: check...
      page: 0,
    };

    const fetchData = async () => {
      const searchResponse = await SearchDataService.create(data);
      console.log(searchResponse);
      setSearchResults(searchResponse.results);
    };
    fetchData().catch(console.error);
  }, [searchString]);
  return (
    <div>
      <SearchNavbar
        searchString={searchString}
        setSearchString={setSearchString}
      ></SearchNavbar>
      <Main searchString={searchString} searchResults={searchResults}></Main>
    </div>
  );
}

export default App;
