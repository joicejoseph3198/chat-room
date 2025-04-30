import axios from "axios";
import { createContext, useContext } from "react";

export const axiosContext = createContext(); // creates a context
export const useAxios = () => useContext(axiosContext); // I can use useAxios directly in the components

export const AxiosProvider = ({ children }) => {
  const axiosInstance = axios.create({
    baseURL: "http://localhost:8080/api/chatapp/",
    headers: {
      "Content-Type": "application/json",
    },
  });

  return (
    <axiosContext.Provider value={axiosInstance}>
      {children}
    </axiosContext.Provider>
  );
};