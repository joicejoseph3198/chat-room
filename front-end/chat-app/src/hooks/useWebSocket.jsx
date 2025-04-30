
import {useEffect, useRef, useState } from 'react';
import sockjs from "sockjs-client/dist/sockjs"
import { Stomp } from "@stomp/stompjs";

const useWebSocket = (url, roomId, currentUser, onMessageReceived) => {
  const stompClientRef = useRef(null);
  const subscriptionRef = useRef(null);
  const [connected, setConnected] = useState(false);
  const connectionAttemptRef = useRef(0);

  useEffect(() => {
    // Skip if already connected or missing required params
    if (!url || !roomId || !currentUser) return;

    // Clear previous connection if exists
    if (subscriptionRef.current) {
      subscriptionRef.current.unsubscribe();
      subscriptionRef.current = null;
    }

    if (stompClientRef.current) {
      stompClientRef.current.deactivate();
      stompClientRef.current = null;
    }

    // Create new connection
    const socket = new sockjs(url);
    const client = Stomp.over(socket);
    stompClientRef.current = client;

    client.connect({}, () => {
      connectionAttemptRef.current = 0;
      setConnected(true);

      // Only subscribe if not already subscribed
      if (!subscriptionRef.current) {
        subscriptionRef.current = client.subscribe(
          `/topic/chatroom/${roomId}`,
          (message) => {
            try {
              const parsedMessage = JSON.parse(message.body);
              onMessageReceived(parsedMessage);
            } catch (error) {
              console.error('Message parsing error:', error);
            }
          }
        );
      }
    }, (error) => {
      console.error('Connection error:', error);
      setConnected(false);
    });

    return () => {
      if (subscriptionRef.current) {
        subscriptionRef.current.unsubscribe();
        subscriptionRef.current = null;
      }

      if (stompClientRef.current) {
        stompClientRef.current.deactivate();
        stompClientRef.current = null;
      }

      setConnected(false);
    };
  }, [url, roomId, currentUser]);

  const sendMessage = (destination, message) => {
    if (stompClientRef.current && stompClientRef.current.connected) {
      stompClientRef.current.send(destination, {}, JSON.stringify(message));
    }
  };

  return { connected, sendMessage };
};

export default useWebSocket;

