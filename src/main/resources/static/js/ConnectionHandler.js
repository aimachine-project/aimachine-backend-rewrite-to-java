class ConnectionHandler {
    static #protocol = location.protocol.replace('http', 'ws')
    static #serverUrl = this.#protocol + "//" + document.domain + ":" + location.port + "/games/tictactoe"

    #socket = null;
    #gameId = null;
    #clientId = null;
    #movementAllowed = false;

    static #getImagePath(fieldToken) {
        switch (fieldToken) {
            case 1:
                return "../X.png";
            case -1:
                return "../O.png";
        }
    }

    connectToServer(onConnectedHandler) {
        this.#socket = new WebSocket(ConnectionHandler.#serverUrl)
        this.#socket.onopen = () => {
            console.log("connected")
            onConnectedHandler()
        }
        const client = this
        this.#socket.onmessage = function (event) {
            const json = JSON.parse(event.data)
            switch (json["eventType"]) {
                case "game_id":
                    client.#gameId = json["eventMessage"]
                    console.log("game id: " + client.#gameId)
                    break
                case "client_id":
                    client.#clientId = json["eventMessage"]
                    console.log("player id: " + client.#clientId)
                    break
                case "field_to_be_marked":
                    const data = JSON.parse(json["eventMessage"])
                    const rowIndex = data["rowIndex"]
                    const colIndex = data["colIndex"]
                    const fieldToken = data["fieldToken"]
                    console.log("field to be marked (row, col, token): " + rowIndex, colIndex, fieldToken)
                    $("#box_" + rowIndex + colIndex).prepend($('<img>', {
                        src: ConnectionHandler.#getImagePath(fieldToken),
                        style: "max-width:100%; max-height:100%;",
                        alt: fieldToken
                    }))
                    break
                case "movement_allowed":
                    client.#movementAllowed = json["eventMessage"] === client.#clientId
                    break
                case "server_message":
                    console.log(json["eventMessage"])
                    break
                default:
                    console.log("message not handled: " + event.data)
            }
        }
    }

    sendFieldClickedMessage(rowIndex, colIndex) {
        if (this.#socket !== null) {
            if (this.#movementAllowed === true) {
                this.#socket.send(JSON.stringify({
                    "eventType": "field_clicked",
                    "eventMessage": {
                        "gameId": this.#gameId,
                        "rowIndex": rowIndex,
                        "colIndex": colIndex
                    }
                }))
            }
        }
    }

    disconnect(onDisconnectedHandler) {
        if (this.#socket !== null && this.#socket.readyState !== WebSocket.CLOSED) {
            this.#socket.close()
            onDisconnectedHandler()
        }
    }
}
