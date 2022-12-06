import React from "react";
import "./App.css";
import Main from "./components/Routing/Main";
import { useEffect } from "react";
import "./assets/css/styles.css";
import SearchNavbar from "./components/Navbar/SearchNavbar";
import getLocalData from "./services/local_data.service";
import Contact from "./types/contact/Contact";
import ISearch from "./types/search/search.type";
import SearchDataService from "./services/search.service";

function App() {
  const [searchString, setSearchString] = React.useState<string>("");
  const [searchResults, setSearchResults] = React.useState<Array<Contact>>([]);

  useEffect(() => {
    document.title = "nanoCLM";
    setSearchResults(getLocalData(searchString));
  }, []);
  useEffect(() => {
    const data: ISearch = {
      headers: {
        //TODO: get from auth
        userToken: "user@clm.com", //TODO: user must be under allowed users (in database)
        tenantUniqueName: searchString,
      },
      body: { sortBy: "name" }, //TODO: check...
    };

    const fetchData = async () => {
      const searchResponse = await SearchDataService.create(data);
      console.log(searchResponse);
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
