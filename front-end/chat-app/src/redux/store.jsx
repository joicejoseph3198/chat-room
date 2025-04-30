import { configureStore } from "@reduxjs/toolkit";
import createRoomReducer  from "./slices/createRoomSlice";
import chatRoomReducer from "./slices/chatRoomSlice";

export const store = configureStore({
    reducer:{
        createRoom: createRoomReducer,
        chatRoom: chatRoomReducer
    }
})