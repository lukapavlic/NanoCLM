import React from "react";
import { Routes, Route } from "react-router-dom";
import IContactResponse from "../../types/contact/contactResponse.type";
import PageNotFound from "../PageNotFound/PageNotFound";
import Search from "../Search/Search";

interface MainProps {
  searchString: String;
  searchResults: Array<IContactResponse>;
}

const Main = (props: MainProps) => {
  return (
    <Routes>
      <Route
        path="/"
        element={
          <Search
            searchString={props.searchString}
            searchResults={props.searchResults}
          />
        }
      />
      <Route path="*" element={<PageNotFound />} />
    </Routes>
  );
};

export default Main;
