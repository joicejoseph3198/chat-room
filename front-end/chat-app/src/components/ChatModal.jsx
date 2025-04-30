import React, { useEffect, useRef } from "react";
import MessageInput from "./MessageInput";
import useWebSocket from "../hooks/useWebSocket";
import { useDispatch, useSelector } from "react-redux";
import { useAxios } from "../util/axiosUtil";
import { appendMessage, fetchHistory } from "../redux/slices/chatRoomSlice";

const ChatModal = ({ isOpen, onClose, currentUser, roomName}) => {
  if (!isOpen) return null;
  const handleMessageReceived = (receivedMessage) => {
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
    dispatch(appendMessage(incomingMessage));
  };
  
  const {connected, sendMessage} = useWebSocket('http://localhost:8080/api/chatapp/ws',roomName,currentUser, handleMessageReceived);

  const msgHistory = useSelector((state) => state.chatRoom)
  const dispatch = useDispatch();
  const axiosInstance = useAxios();
  const bottomRef = useRef(null);
  
  useEffect(()=>{
    const requestBody = {
      roomId: roomName,
      limit: 50,
      offset: 0
    }
    dispatch(fetchHistory({requestBody, axiosInstance}))
  },[])

  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: "auto" });
  }, [msgHistory]);


  const handleSend = (newMessageText) => {
    const now = new Date().getTime();

    const newMessage = {
      timestamp: now,
      participant: currentUser,
      message: newMessageText,
      type: 'CHAT'
    };
    sendMessage(`/ws/chat/${roomName}`, newMessage);
  };

  return (
    <div className="fixed inset-0 flex items-center justify-center z-50">
      <div className="bg-white/30 backdrop-blur-md border border-black w-full max-w-xl max-h-4/5 rounded-xl shadow-lg flex flex-col p-4 overflow-hidden">
        {/* Header */}
        <div className="flex justify-between items-center border-b pb-2 mb-2">
          <h2 className="text-lg font-semibold">{roomName.toUpperCase()}</h2>
          <button onClick={onClose} className="text-gray-500 hover:text-black">&times;</button>
        </div>

        {/* Messages */}
        <div className="flex-1 overflow-y-auto scroll-hidden space-y-3 pr-5">
          {msgHistory?.messages.map((msg, index) => {
            const isMine = msg.username === currentUser;
            const isChatMessage = msg.type == 'CHAT' || msg.type == null
            const isLast = index === msgHistory?.messages.length - 1;
            return (
              <div
                key={index}
                className={`flex  ${isChatMessage  ?(isMine ? "justify-end" : "justify-start") : "justify-center"}`}
              >
                  {
                    msg.type == 'CHAT' || msg.type == null ?
                    <>
                    <div className={`text-sm text-gray-800 max-w-[70%] ${ isMine ? "text-right" : "text-left"}`}>
                        <div className={`text-xs text-gray-500 mb-0.5 ${ isMine ? "text-right" : "text-left"}`}>
                        {msg.timestamp} &nbsp; <span className="text-orange-500 font-medium">{msg.username}</span>
                        </div>
                    <div className={`whitespace-pre-wrap ${isMine ? "justify-end" : "justify-start"}`}>{msg.content}</div> 
                    </div>
                    </>
                   :
                    <div className="text-xs text-justify text-gray-500">{msg.timestamp} &nbsp; {msg.content}</div>
                  }
                  
                {isLast && <div ref={bottomRef} />} {/* this triggers scroll */}
              </div>
            );
          })}
        </div>
        <MessageInput onSend={handleSend}/>
      </div>
    </div>
  );
};

export default ChatModal;
