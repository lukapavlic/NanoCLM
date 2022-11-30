import React from 'react';
import { Routes, Route } from "react-router-dom";
import PageNotFound from "../PageNotFound/PageNotFound";
import Search from "../Search/Search";
import Contact from '../../types/contact/Contact';

interface MainProps {
    searchString: String,
    searchResults: Array<Contact>
  }

const Main = (props: MainProps) => {
    return (
        <Routes>
            <Route path="/" element={<Search searchString={props.searchString} searchResults={props.searchResults}/>} />
            <Route path="*" element={<PageNotFound />} />
        </Routes>
    );
}

export default Main;