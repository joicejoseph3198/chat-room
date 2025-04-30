import React, { useEffect, useRef } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useAxios } from "../util/axiosUtil";
import { getActiveChatRooms, getRoomListing, joinChatRoom } from "../redux/slices/chatRoomSlice";
import { toast } from "react-toastify";

const RoomListing = (currentUser) => {
    const rooms = useSelector((state)=> state.chatRoom.chatRoomListing);
    const activeRooms = useSelector((state)=> state.chatRoom.activeChatRoomListing);
    const dispatch = useDispatch();
    const axiosInstance = useAxios();
    const roomsMapRef = useRef(new Map());
    
    // Fetch room listing on component load
    useEffect(()=>{
        dispatch(getRoomListing({axiosInstance}));
    },[])

    // Temporary flow to demonstrate, user is required to enter username before proceeding
    // Each time user enters a new username, we refetch the rooms that the user is part of
    useEffect(()=>{
        if(currentUser && currentUser?.length > 1){
            dispatch(getActiveChatRooms({participant: currentUser, axiosInstance}));
        }
    },[currentUser, dispatch, axiosInstance])

    useEffect(() => { 
      roomsMapRef.current = new Map(activeRooms.map(room => [room, room]));
      dispatch(getRoomListing({axiosInstance}));
    }, [activeRooms, dispatch, axiosInstance]);
    
    const handleRefresh = () => {
        dispatch(getRoomListing({participant: currentUser.currentUser, axiosInstance}));
        dispatch(getActiveChatRooms({participant: currentUser.currentUser, axiosInstance}));
    }

    const handleJoin = async (roomName) => {
        if(roomName && currentUser.currentUser && currentUser.currentUser.length > 2){
          await dispatch(joinChatRoom({participant: currentUser.currentUser, roomName: roomName.toLowerCase(), axiosInstance}));
          handleRefresh()
          toast('Action successful')
        }else{
          toast('Please provide a valid username before proceeding')
        }
    }
          

  return (
    <div className="p-4 max-w-4xl mx-auto">
      <h2 className="text-2xl font-semibold mb-4 text-left">Room Listings</h2>
      {
      rooms.length < 1 ? 
      <p className="text-gray-600">No active chat rooms found.</p> :
      <>
      <h2 className="text-xs font-bold mb-4 text-right">
        <button className="px-2 py-2 text-black text-left cursor-pointer" onClick={handleRefresh}>
            [REFRESH] 
        </button>
      </h2>
      <div className="overflow-x-auto overflow-y-auto max-h-60 pr-4 rounded-lg">
        <table className="min-w-full divide-y divide-gray-200 bg-white">
          <thead>
            <tr>
              <th className="px-4 py-3 text-left text-sm font-medium text-gray-600">#</th>
              <th className="px-4 py-3 text-left text-sm font-medium text-gray-600">Room Name</th>
              <th className="px-4 py-3 text-left text-sm font-medium text-gray-600">Owner</th>
              <th className="px-4 py-3 text-left text-sm font-medium text-gray-600">Description</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-200">
            {rooms.map((room, index) => (
              <tr key={room.id || index}>
                <td className="px-4 py-2 text-sm text-gray-800">{index + 1}</td>
                <td className="px-4 py-2 text-sm font-medium text-orange-600">{room.name}</td>
                <td className="px-4 py-2 text-sm text-gray-800">{room.owner}</td>
                <td className="px-4 py-2 text-xs text-gray-800">{room.description}</td>
                <td className="px-4 py-2 text-sm text-gray-800">
                    {roomsMapRef.current.has(room.name) ? "" : 
                    <button className="px-2 py-2 text-black text-left cursor-pointer hover:text-gray-500 hover:cursor-pointer" onClick={()=>handleJoin(room.name)}>
                    [JOIN] 
                    </button> }</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      </>
      }

    </div>
  );
};

export default RoomListing;
