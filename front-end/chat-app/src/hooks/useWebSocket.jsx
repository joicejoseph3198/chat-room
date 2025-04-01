
import { useEffect, useState } from 'react';
import sockjs from "sockjs-client/dist/sockjs"
import { Stomp } from "@stomp/stompjs";


const useWebSocket = (url, roomId, onMessageReceived) => {
  const [stompClient, setStompClient] = useState(null);
  const [connected, setConnected] = useState(false);

  useEffect(() => {
    
    const socket = new sockjs(url);
    const client = Stomp.over(socket);

    client.connect({}, () => {
      console.log("Connected to WebSocket server");
      setConnected(true);
      setStompClient(client); 
      
      client.subscribe(`/topic/chatroom/${roomId}`, (message) => {
        const parsedMessage = JSON.parse(message.body);
        console.log("New Message: ", parsedMessage.message);
        if (onMessageReceived) onMessageReceived(parsedMessage); 
      });
    }, (frame) => {
      console.error("STOMP Error:", frame);
    });

    return () => {
      console.log("Cleaning up WebSocket...");
      client.disconnect(() => {
        console.log("Disconnected from WebSocket server");
        setConnected(false);
        setStompClient(null);
      });
    };
  }, [url, roomId]); 

  const sendMessage = (destination, message) => {
    if (stompClient && stompClient.connected) {
      stompClient.send(destination, {}, JSON.stringify(message));
    } else {
      console.error("WebSocket client not connected");
    }
  };

  return { connected, sendMessage };
};

export default useWebSocket;

