import IContactResponse from "../types/contact/contactResponse.type";

const localData: Array<IContactResponse> = [
  {
    mongoId: "6376bf90e3e8695771c9cd84",
    uniqueId: "ii-222501031370600-347",
    title: "Tilen",
    props: { ime: "Tilen", letnikStudija: "3" },
    attrs: ["prijava2022", "komisija1"],
    comments: { c1: "komentar1", c2: "komentar2" },
  },
  {
    mongoId: "6376d3601934ee79907796c0",
    uniqueId: "ii-227573033429400-7131",
    title: "Tilen",
    props: { ime: "Tilen", letnikStudija: "3" },
    attrs: ["prijava2022", "komisija1"],
    comments: { c1: "komentar1", c2: "komentar2" },
  },
];
export default localData;
