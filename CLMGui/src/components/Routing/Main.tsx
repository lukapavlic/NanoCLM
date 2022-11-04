import React from 'react';
import { Routes, Route } from "react-router-dom";
import PageNotFound from "../PageNotFound/PageNotFound";
import Search from "../Search/Search";

const Main = () => {
    return (
        <Routes>
            <Route path="/" element={<Search />} />
            <Route path="*" element={<PageNotFound />} />
        </Routes>
    );
}

export default Main;