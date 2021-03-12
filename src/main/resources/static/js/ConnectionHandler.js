class ConnectionHandler {
    static #serverUrl = location.protocol + "//" + document.domain + ":" + location.port;
    static #connectionOptions = {autoConnect: false, reconnection: false};

    #socket = null;
    #gameId = null;
    #clientId = null;
    #movementAllowed = false;

    connectToServer(onConnectedHandler) {
        this.#socket = io(ConnectionHandler.#serverUrl, ConnectionHandler.#connectionOptions);
        if (this.#socket.disconnected) {
            this.#socket.open();
            this.#socket.on("connect", () => {
                onConnectedHandler();
            });
            this.#socket.on("game_id", (response) => {
                this.#gameId = response;
                console.log("game id: " + this.#gameId);
            });
            this.#socket.on("client_id", (data) => {
                this.#clientId = data;
                console.log("player id: " + this.#clientId);
            });
            this.#socket.on("field_to_be_marked", (data) => {
                let rowIndex = data["rowIndex"];
                let colIndex = data["colIndex"];
                let fieldToken = data["fieldToken"];
                console.log("field to be marked (row, col, token): " + rowIndex, colIndex, fieldToken);
                $("#box_" + rowIndex + colIndex).prepend($('<img>', {
                    src: ConnectionHandler.#getImagePath(fieldToken),
                    style: "max-width:100%; max-height:100%;"
                }));
            });
            this.#socket.on("server_message", (data) => {
                console.log("server message: " + data);
            });
            this.#socket.on("movement_allowed", (data) => {
                this.#movementAllowed = data === this.#clientId;
            });
        }
    }

    static #getImagePath(fieldToken) {
        switch (fieldToken) {
            case 1:
                return "/static/X.png";
            case -1:
                return "/static/O.png";
        }
    }

    sendFieldClickedMessage(rowIndex, colIndex) {
        if (this.#socket !== null && this.#socket.connected) {
            if (this.#movementAllowed === true) {
                this.#socket.emit("field_clicked", {
                    "gameId": this.#gameId,
                    "rowIndex": rowIndex,
                    "colIndex": colIndex
                });
            }
        }
    }

    disconnect(onDisconnectedHandler) {
        if (this.#socket !== null) {
            if (this.#socket.connected) {
                this.#socket.disconnect();
                onDisconnectedHandler();
            }
        }
    }
}
