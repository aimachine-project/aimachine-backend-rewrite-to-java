package ai.aimachineserver

import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

class WebSocketServerHandler : TextWebSocketHandler() {

    private var rooms = mutableListOf<Room>()
    private var cnt = 0

    override fun afterConnectionEstablished(session: WebSocketSession) {
        println("client ${session.id} connected")
        cnt++
        if (cnt % 2 != 0) {
            val room = Room()
            room.sessions.add(session)
            rooms.add(Room())
        } else {
            rooms.last().sessions.add(session)
        }
        println("rooms count: ${rooms.count()}")
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        println("received message: ${message.payload}")
        session.sendMessage(TextMessage("Some important server message to the current session"))
        rooms.forEachIndexed { index, room ->
            room.sessions.forEach { it.sendMessage(TextMessage("Server message to room $index")) }
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        println("client ${session.id} disconnected")
    }

    private class Room(var sessions: MutableList<WebSocketSession> = mutableListOf())
}