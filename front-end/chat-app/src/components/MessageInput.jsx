import React, { useState } from "react";

const MessageInput = ({ onSend }) => {
  const [message, setMessage] = useState("");

  const handleSend = () => {
    if (!message.trim()) return;
    onSend(message.trim());
    setMessage("");
  };

  const handleKeyDown = (e) => {
    if (e.key === "Enter" && !e.shiftKey) {
      e.preventDefault();
      handleSend();
    }
  };

  return (
    <div className="flex items-center gap-2 border-t pt-2 mt-2">
      <textarea
        value={message}
        onChange={(e) => setMessage(e.target.value)}
        onKeyDown={handleKeyDown}
        rows={1}
        placeholder="Enter message"
        className="flex-1 resize-none bg-gray-100 rounded-md px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-orange-500"
      />
      <button
        onClick={handleSend}
        className="px-3 py-2 bg-orange-500 text-white text-sm rounded-md hover:bg-orange-600 transition"
      >
        Send
      </button>
    </div>
  );
};

export default MessageInput;
