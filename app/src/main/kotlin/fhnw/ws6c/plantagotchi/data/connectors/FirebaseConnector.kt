package fhnw.ws6c.plantagotchi.data.connectors

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.shopify.promises.Promise
import fhnw.ws6c.plantagotchi.AppPreferences
import fhnw.ws6c.plantagotchi.data.state.GameState


class FirebaseConnector(var appPreferences: AppPreferences) {

    private val TAG = "PlantaGotchiFirebaseConnector"
    private val db = Firebase.firestore
    private val gameStateTable = "gameState"


    //Todo needs refactoring
    fun createNewGameState(gameState: GameState) {
        db.collection(gameStateTable)
            .add(gameState.toHashMap())
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "GameState created for ${documentReference.id}")
                appPreferences.player_id = documentReference.id
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    fun updateGameState(gameState: GameState) {
        db.collection(gameStateTable)
            .document(appPreferences.player_id)
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

                Log.d(TAG, "Loaded GameState from Firebase: ${result.id}")
                gs.playerId = result.id
                gs.playerState.lux = result.getDouble("playerState.lux")!!
                gs.playerState.love = result.getDouble("playerState.love")!!
                gs.playerState.fertilizer = result.getDouble("playerState.fertilizer")!!
                gs.playerState.water = result.getDouble("playerState.water")!!
                gs.playerState.coins = result.get("playerState.coins").toString().toInt()

                resolve(gs)
            }.addOnFailureListener { except ->
                reject(except)
            }
    }
}