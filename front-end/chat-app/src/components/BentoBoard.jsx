import React, { useState } from "react";
import RoomListing from "./RoomListing";
import ChatModal from "./ChatModal";
import { InputTextField } from "./InputTextField";
import CreateChatRoom from "./CreateChatRoom";
import ActiveRoomListing from "./ActiveRoomListing";
import { useDispatch, useSelector } from "react-redux";
import { toggleShowChat } from "../redux/slices/chatRoomSlice";

const BentoBoard = () => {
  const showChat = useSelector((state)=> state.chatRoom.showChat);
  const dispatch = useDispatch();

  const [currentUser, setCurrentUser] = useState("");
  const activeModal = useSelector((state)=> state.chatRoom.activeModal)

  const handleInputChange = (event) => {
    const {value} = event.target;
    setCurrentUser(value)
  };

  const handleToggleChat =()=>{
    dispatch(toggleShowChat())
  }

  return (
    <div className="p-4 md:min-w-5xl max-w-7xl mx-auto text-left border-black ">
      <div className="grid grid-cols-1 gap-2 md:grid-cols-2">
        {/* Top Full-Width Section */}
        <div className="md:col-span-2 bg-white rounded-2xl p-6 shadow-md border">
          <h2 className="text-4xl text-orange-600 font-semibold">CHAT BOX</h2>
          <p className="text-2xl text-uppercase">Create, Join, Chat</p>
          <div className="pt-4">
            <InputTextField
                name="enter username"
                title="Enter username before proceeding"
                placeholder={"Enter Username"}
                valueStore={currentUser}
                onChangeHandler={handleInputChange}
            />
          </div>
          
        </div>

        {/* Middle Left Section */}
        <div className="bg-white rounded-2xl p-6 shadow-md border">
          <RoomListing currentUser={currentUser}/>
        </div>

        {/* Middle Right Section */}
        <div className="bg-white rounded-2xl p-6 shadow-md border">
            <CreateChatRoom/>
        </div>

        {/* Bottom Section */}
        <div className="md:col-span-2 bg-white rounded-2xl p-6 shadow-md border">
            <ActiveRoomListing currentUser={currentUser}/>
        </div>
        <div className="flex justify-end">
          {
            showChat ? <ChatModal
                    key={`${currentUser}-${activeModal}`}
                    isOpen={showChat}
                    onClose={handleToggleChat}
                    currentUser={currentUser}
                    roomName={activeModal}
                    /> : null
          }
        </div>
      </div>
    </div>
  );
};

export default BentoBoard;
