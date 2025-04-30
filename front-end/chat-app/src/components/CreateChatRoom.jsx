import React from 'react'
import { useDispatch, useSelector } from 'react-redux';
import { createRoomSubmit, resetField, updateField } from '../redux/slices/createRoomSlice';
import { useAxios } from '../util/axiosUtil';
import { InputTextField } from './InputTextField';
import { getRoomListing } from '../redux/slices/chatRoomSlice';
import { toast } from 'react-toastify';

const CreateChatRoom = () => {
    const createRoomData = useSelector((state) => state.createRoom)
    const dispatch = useDispatch();
    const axiosInstance = useAxios();

    const handleInputChange = (event) => {
      const {name,value } = event.target;
      dispatch(updateField({name, value}))
    };
    const handleCreateRoom = async () => {
        const requestBody = {
          name: createRoomData.roomName,
          owner: createRoomData.roomOwner,
          description: createRoomData.roomDescription
        }
        if(!createRoomData.roomName || createRoomData.roomName.length < 2 || !createRoomData.roomOwner || createRoomData.roomOwner.length < 2){
          toast('Required fields are missing')
        }else{
          dispatch(createRoomSubmit({requestBody, axiosInstance}));
          dispatch(resetField());
          await dispatch(getRoomListing({axiosInstance}))
          toast('Action successful')
        }
    };

  return (
    <div>
        <h2 className="text-2xl font-semibold py-5">Create Room</h2>
        <div className="flex flex-col gap-2 justify-end">
        <InputTextField
        title="Room Name"
        placeholder="Enter a unique room name"
        name="roomName"
        valueStore={createRoomData.roomName}
        onChangeHandler={handleInputChange}
        />
        <textarea
            placeholder="Enter description (Optional)"
            value={createRoomData.roomDescription}
            name="roomDescription"
            onChange={handleInputChange}
            className="resize-none bg-transparent text-black w-full h-30 focus:outline-none px-4 py-2 text-xs font-semibold border-2 rounded-md"
        />
        <InputTextField
        title="Owner"
        placeholder="Enter your username"
        name="roomOwner"
        valueStore={createRoomData.roomOwner}
        onChangeHandler={handleInputChange}
        />
        <button
            className="px-4 py-2 text-black text-left hover:text-gray-500 hover:cursor-pointer"
            onClick={handleCreateRoom}
        >
            [CREATE CHAT ROOM]
        </button>
        </div>
    </div>
  )
}

export default CreateChatRoom