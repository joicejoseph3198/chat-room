import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";

const defaultState = {
    roomName: "",
    roomOwner: "",
    isPrivate: false,
    roomDescription: "",
    passCode: "",
    isError: false,
    isLoading: false
}

export const createRoomSubmit = createAsyncThunk(
    "createRoom/submit",
    async (params) => {
        try{
            const {requestBody, axiosInstance} = params;
            const response = await axiosInstance.post(`/chatroom/`, requestBody);
            return response;
        }catch(error){
            console.log(error)
        }
    }
)

export const createRoomSlice = createSlice({
    name: "createRoom",
    initialState: defaultState,
    reducers:{
        updateField: (state,action) => {
            const { name, value } = action.payload;
            const keys = name.split(".");
            if (keys.length > 1) {
              state[keys[0]][keys[1]] = value;
            } else {
              state[name] = value;
            }
        },
        resetField: (state) => {
            Object.assign(state, defaultState);
        }
    },
    extraReducers:(builder)=>{
        
        {/*CREATE ROOM APIs*/}

        builder.addCase(createRoomSubmit.rejected, (state)=>{
            state.isError = true;
        });
         
        builder.addCase(createRoomSubmit.pending, (state)=>{
            state.isLoading = true;
        });
         
        builder.addCase(createRoomSubmit.fulfilled, (state)=>{
            state.isError = false;
            state.isLoading = false;
        });
    }
})

export const {updateField, resetField} = createRoomSlice.actions;
export default createRoomSlice.reducer;