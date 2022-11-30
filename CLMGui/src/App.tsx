import React from "react";
import "./App.css";
import Main from "./components/Routing/Main";
import { useEffect } from "react";
import "./assets/css/styles.css";
import SearchNavbar from "./components/Navbar/SearchNavbar";
import getLocalData from "./services/local_data.service";
import Contact from "./components/Types/Contact";

function App() {
  const [searchString, setSearchString] = React.useState<String>("");
  const [searchResults, setSearchResults] = React.useState<Array<Contact>>([]);

  useEffect(() => {
    document.title = "nanoCLM";
    setSearchResults(getLocalData(searchString));
  });
  return (
    <div>
      <SearchNavbar searchString={searchString} setSearchString={setSearchString}></SearchNavbar>
      <Main searchString={searchString} searchResults={searchResults}></Main>
    </div>
  );
}

export default App;
