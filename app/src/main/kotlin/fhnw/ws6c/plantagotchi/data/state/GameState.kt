package fhnw.ws6c.plantagotchi.data.state

class GameState() {

    var playerState: PlayerState = PlayerState()

    fun toHashMap(): HashMap<String, HashMap<String, Any>> {
        val gameState = hashMapOf(
            "playerState" to playerState.toHashMap()
        )
        return gameState
    }
}
