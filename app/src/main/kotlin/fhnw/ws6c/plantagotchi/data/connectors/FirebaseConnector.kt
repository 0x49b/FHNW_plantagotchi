package fhnw.ws6c.plantagotchi.data.connectors

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.shopify.promises.Promise
import fhnw.ws6c.plantagotchi.AppPreferences
import fhnw.ws6c.plantagotchi.data.state.GameState


class FirebaseConnector(var appPreferences: AppPreferences) {

    private val TAG = "FirebaseConnector"
    private val db = Firebase.firestore
    private val gameStateTable = "gameState"

    fun createNewGameState(gameState: GameState) {
        db.collection(gameStateTable)
                .add(gameState.toHashMap())
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "GameState created for ${documentReference.id}")
                    gameState.playerId = documentReference.id
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
    }

    fun updateGameState(gameState: GameState) {
        db.collection(gameStateTable)
                .document(gameState.playerId)
                .update(gameState.toHashMap() as Map<String, Any>)
    }

    val createGameState = Promise<GameState, Exception> {

        db.collection(gameStateTable)
                .add(GameState())
                .addOnSuccessListener { result ->
                    val gs = GameState()
                    gs.playerId = result.id
                    resolve(gs)
                }
                .addOnFailureListener { except -> reject(except) }
    }

    val loadInitialGameState = Promise<GameState, Exception> {
        db.collection(gameStateTable)
                .document(appPreferences.player_id)
                .get()
                .addOnSuccessListener { result ->
                    val gs = GameState()
                    gs.playerId = appPreferences.player_id
                    gs.playerState.lux = result.get("playerState.lux") as Double
                    gs.playerState.love = result.get("playerState.love") as Double
                    gs.playerState.water = result.get("playerState.water") as Double
                    gs.playerState.fertilizer = result.get("playerState.fertilizer") as Double
                    resolve(gs)
                }.addOnFailureListener { except -> reject(except) }
    }
}