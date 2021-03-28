class ConnectionHandler {
    static #serverUrl = location.protocol + "//" + document.domain + ":" + 9090; // location.port;
    static #connectionOptions = {autoConnect: false, reconnection: false, secure: true};

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
        this.#socket = io(ConnectionHandler.#serverUrl, ConnectionHandler.#connectionOptions);
        if (this.#socket.disconnected) {
            this.#socket.open();
            this.#socket.on("connect", () => {
                console.log("connected")
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
                const json = JSON.parse(data)
                const rowIndex = json["rowIndex"];
                const colIndex = json["colIndex"];
                const fieldToken = json["fieldToken"];
                console.log("field to be marked (row, col, token): " + rowIndex, colIndex, fieldToken);
                $("#box_" + rowIndex + colIndex).prepend($('<img>', {
                    src: ConnectionHandler.#getImagePath(fieldToken),
                    style: "max-width:100%; max-height:100%;",
                    alt: fieldToken
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
