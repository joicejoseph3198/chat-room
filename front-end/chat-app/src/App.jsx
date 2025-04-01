import { useState } from 'react';
import './App.css'
import useWebSocket from './hooks/useWebSocket'

function App() {
  const [message, setMessage] = useState('');
  const roomId = 'general';

  const {connected, sendMessage } = useWebSocket("http://localhost:8080/api/chatapp/ws","general")

  const handleSendMessage = () => {
    if (message.trim()) {
      sendMessage(`/ws/chat/${roomId}`, {
        participant: "general_user",
        message: message,
        timestamp: Date.now(),
        type: "CHAT"
      });
      setMessage(''); // Clear input
    }
  };

  return (
    <>
        <p>ChatApp</p>

        <input
        type="text"
        value={message}
        onChange={(e) => setMessage(e.target.value)}
        placeholder="Type a message..."
       />
      <button onClick={handleSendMessage}>Send</button>
    </>
  )
}

export default App
