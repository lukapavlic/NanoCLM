import IContactResponse from "../types/contact/contactResponse.type";
import localData from "./data";

export default function getLocalData(
  searchString: String
): Array<IContactResponse> {
  let data = localData;
  console.log(searchString);
  return data;
}
