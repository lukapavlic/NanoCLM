import Contact from "../types/contact/Contact";
import localData from "./data";

export default function getLocalData(searchString: String): Array<Contact> {
    let data = localData;
    console.log(searchString);
    return data;

}