import { createAsyncThunk, createSlice } from "@reduxjs/toolkit"


export const fetchHistory = createAsyncThunk(
    "chatRoom/fetchHistory",
    async (params) => {
        try{
            const {requestBody, axiosInstance} = params;
            const response = await axiosInstance.get(`/chatroom/${requestBody.roomId}/history?limit=${requestBody.limit}&offset=${requestBody.offset}`);
            return response;
        }catch(error){
            console.log(error)
        }
    }
);

export const getActiveChatRooms = createAsyncThunk(
    "chatRoom/getActiveChatRooms",
    async (params) => {
        try{
            const {participant, axiosInstance} = params;
            const response = await axiosInstance.get(`/chatroom/active/all?participant=${participant}`);
            return response.data?.data;
        }catch(error){
            console.log(error)
        }
    }
)

export const joinChatRoom = createAsyncThunk(
    "chatRoom/joinChatRoom",
    async (params) => {
        try{
            const {participant,roomName, axiosInstance} = params;
            const response = await axiosInstance.post(`/chatroom/${roomName}/join?participant=${participant}`);
            return response.data?.data;
        }catch(error){
            console.log(error)
        }
    }
)

export const exitChatRoom = createAsyncThunk(
    "chatRoom/exitChatRoom",
    async (params) => {
        try{
            const {participant,roomName, axiosInstance} = params;
            const response = await axiosInstance.post(`/chatroom/${roomName}/exit?participant=${participant}`);
            return response.data?.data;
        }catch(error){
            console.log(error)
        }
    }
)

export const getRoomListing = createAsyncThunk(
    "chatRoom/getRoomListing",
    async (params) => {
        try{
            const {axiosInstance} = params;
            const response = await axiosInstance.get(`/chatroom/all`);
            return response.data?.data;
        }catch(error){
            console.log(error)
        }
    }
)


const defaultState = {
    isError: false,
    isLoading: false,
    messages: [],
    chatRoomListing:[],
    activeChatRoomListing:[],
    activeModal: "",
    showChat: false
}
export const chatRoomSlice = createSlice({
    name: "chatRoom",
    initialState: defaultState,
    reducers:{
        appendMessage : (state,action) =>{
            state.messages.push(action.payload);
        },
        updateActiveModal: (state,action)=>{
            state.activeModal = action.payload;
        },
        toggleShowChat: (state) =>{
            state.showChat = !state.showChat;
        }
        
    },
    extraReducers:(builder)=>{
        {/*CHAT HISTORY API*/}
        builder.addCase(fetchHistory.rejected, (state)=>{
            state.isError = true;
        });
        builder.addCase(fetchHistory.pending, (state)=>{
            state.isLoading = true;
        });
        builder.addCase(fetchHistory.fulfilled, (state, action)=>{
            state.isError = false;
            state.isLoading = false;
            if(action.payload?.data?.data) {
                state.messages = action.payload.data.data.map((receivedMessage) =>{
                    const date = new Date(receivedMessage.timestamp);
                    const options = {
                        hour: '2-digit',
                        minute: '2-digit',
                        hour12: true, 
                        day: '2-digit',
                        month: 'short',
                        year: 'numeric'
                    };
                
                    const formattedDate = date.toLocaleString('en-US', options);
                    const incomingMessage = {
                        timestamp: formattedDate,
                        username: receivedMessage.participant,
                        content: receivedMessage.message,
                        type: receivedMessage.type
                    }
                    return incomingMessage;
                });
            }
        });
        {/*CHAT ROOM LISTING API*/}
        builder.addCase(getRoomListing.rejected, (state)=>{
            state.isError = true;
        });
        builder.addCase(getRoomListing.pending, (state)=>{
            state.isLoading = true;
        });
        builder.addCase(getRoomListing.fulfilled,(state,action)=>{
            state.isError = false;
            state.isLoading = false;
            if(action.payload) {
                state.chatRoomListing = action.payload
            }
        });

        {/*ACTIVE ROOM LISTING API*/}
        builder.addCase(getActiveChatRooms.rejected, (state)=>{
            state.isError = true;
        });
        builder.addCase(getActiveChatRooms.pending, (state)=>{
            state.isLoading = true;
        });
        builder.addCase(getActiveChatRooms.fulfilled,(state,action)=>{
            state.isError = false;
            state.isLoading = false;
            if(action.payload) {
                state.activeChatRoomListing = action.payload
            }
        });
    }
});

export const {appendMessage, updateActiveModal,toggleShowChat} = chatRoomSlice.actions;
export default chatRoomSlice.reducer;