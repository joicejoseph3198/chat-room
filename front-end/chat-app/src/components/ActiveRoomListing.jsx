import React, { useEffect, useState } from 'react'
import { useDispatch, useSelector } from 'react-redux';
import { useAxios } from '../util/axiosUtil';
import { exitChatRoom, getActiveChatRooms, toggleShowChat, updateActiveModal } from '../redux/slices/chatRoomSlice';
import { toast } from 'react-toastify';

const ActiveRoomListing = ({currentUser}) => {
    const rooms = useSelector((state)=> state.chatRoom.activeChatRoomListing);
    const dispatch = useDispatch();
    const axiosInstance = useAxios();
    
    useEffect(()=>{
        if(currentUser.length > 1 && typeof currentUser == "string" || currentUser instanceof String){
          dispatch(getActiveChatRooms({participant: currentUser, axiosInstance}));
        }
    },[axiosInstance, currentUser, dispatch])

    const handleRefresh = () => {
      dispatch(getActiveChatRooms({participant: currentUser, axiosInstance}));
    }

    const handleOpenChat = (room) => {
      dispatch(updateActiveModal(room.toLowerCase()));
      dispatch(toggleShowChat())
    }

    const handleExitChat = async(roomName) => {
      if(roomName && currentUser && currentUser.length > 2){
        await dispatch(exitChatRoom({participant: currentUser, roomName: roomName.toLowerCase(), axiosInstance}));
        handleRefresh();
        toast('Action successful')
      }else
      toast('Please provide a valid username before proceeding')
    }


  return (
    <div className="p-4 max-w-4xl">
      <h2 className="text-2xl font-semibold mb-4 text-left">Participating Chat Rooms</h2>
      {rooms.length > 0 ?
      <>
      <h2 className="text-sm font-bold mb-4 text-right">
        <button className="px-2 py-2 text-black text-left cursor-pointer" onClick={handleRefresh}>
            [REFRESH] 
        </button>
      </h2>
      <div className="overflow-x-auto rounded-lg">
        <table className="min-w-full divide-y divide-gray-200 bg-white">
          <thead>
            <tr>
              <th className="px-4 py-3 text-left text-sm font-medium text-gray-600">#</th>
              <th className="px-4 py-3 text-left text-sm font-medium text-gray-600">Room Name</th>
              <th className="px-4 py-3 text-left text-sm font-medium text-gray-600">Actions</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-200">
            {rooms.map((room, index) => (
              <tr key={room.id || index}>
                <td className="px-4 py-2 text-sm text-gray-800">{index + 1}</td>
                <td className="px-4 py-2 text-sm font-medium text-orange-600">{room}</td>
                <td className="px-4 py-2 text-sm text-gray-800">
                    <button className="px-2 py-2 text-black text-left cursor-pointer hover:text-gray-500 hover:cursor-pointer" onClick={()=>handleOpenChat(room)}>
                    [OPEN CHAT] 
                    </button>
                    <button className="px-2 py-2 text-black text-left cursor-pointer hover:text-gray-500 hover:cursor-pointer" onClick={()=>handleExitChat(room)}>
                    [EXIT CHATROOM] 
                    </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      </>
      : <p className='text-gray-600 text-sm'>You're currently not part of any active chat rooms.</p>
      }
    </div>
  );
}

export default ActiveRoomListing