### Chat App via WebSockets + Redis + Spring Boot

![Untitled](DocumentationImages/Screenshot-from-2025-04-30-00-34-30.png.png)

![Untitled](DocumentationImages/diagram-export-30-4-2025-8_00_25-pm.png)

Flow:

> **Client connects to the server using WebSocket**

- The WebSocket handshake happens via a specific endpoint (e.g.`http://localhost:8080/api/chatapp/ws`).
- Spring boot enables this by configuring `WebSocketMessageBrokerConfigurer`  
![Untitled](DocumentationImages/Pasted-image-20250430201208.png)

> **STOMP connection is established, and client subscribes to a topic**

![Untitled](DocumentationImages/Pasted-image-20250430201740.png)

> **Client sends message to the server**

![Untitled](DocumentationImages/Pasted-image-20250430202020.png)  
Messages sent to `/ws/chat/**` are routed to Spring controller methods using `@MessageMapping`.  
![Untitled](DocumentationImages/Pasted-image-20250430202143.png)

> **Spring Boot propagates the message to Redis channel**

Spring boot routes these messages through this controller, processes it, and sends it to the Redis channel.

![Untitled](DocumentationImages/Pasted-image-20250430202447.png)

> **Redis Listener will receive messages from these channels and propagate it further to the topic that it is hosting in-memory**

Configure a Redis Listener that will get the message from the channel and send it to the topic  
![Untitled](DocumentationImages/Pasted-image-20250430202941.png)  
Register the listener in Redis config  
![Untitled](DocumentationImages/Pasted-image-20250430202828.png)

> **Client receives message through these topics, and can present them in the UI**

![Untitled](DocumentationImages/Pasted-image-20250430203314.png)

![Untitled](DocumentationImages/Pasted-image-20250430203524.png)

> **Besides this, APIs are configured that are used to create chat rooms, join, leave and delete. These APIs rely on Redis heavily to store all the metadata and the chat messages are also stored in Redis.**

![Untitled](DocumentationImages/Redis-Implementation.png)
