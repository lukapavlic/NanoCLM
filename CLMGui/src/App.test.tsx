import React from "react";
import { render} from "@testing-library/react";
import App from "./App";
import { BrowserRouter } from "react-router-dom";

test("renders App component", () => {
  render(
    <BrowserRouter>
      <App />
    </BrowserRouter>
  );
  expect(document.title).toEqual("nanoCLM");
});
