package fhnw.ws6c.plantagotchi.data.state

class GameState() {

    var playerState: PlayerState = PlayerState()
    var playerId: String = ""

    fun toHashMap(): HashMap<String, HashMap<String, Any>> {
        return hashMapOf(
                "playerState" to playerState.toHashMap()
        )
    }

    fun toJSON(): String {
        return "\"playerId\": ${playerId},\"playerState\":${playerState.toJSON()}"
    }
}
