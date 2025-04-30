import { createBrowserRouter } from "react-router";
import MainContainer from "../components/MainContainer";


export const router = createBrowserRouter([
    {
      path: "/",
      element: <MainContainer/>,
      children: [ 
       
      ],
    },
  ]);