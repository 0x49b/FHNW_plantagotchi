package fhnw.ws6c.plantagotchi.data.connectors

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.shopify.promises.Promise
import fhnw.ws6c.plantagotchi.AppPreferences
import fhnw.ws6c.plantagotchi.data.state.GameState
import java.lang.RuntimeException


class FirebaseConnector(var appPreferences: AppPreferences) {

    private val TAG = "FirebaseConnector"
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

    /*val createNewGameStatePromise = Promise<GameState, Exception>{ gameState ->
        db.collection(gameStateTable)
            .add(gameState.toHashMap())
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "GameState created for ${documentReference.id}")
                appPreferences.player_id = documentReference.id
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }*/

    val loadInitialGameState = Promise<GameState, Exception> {
        db.collection(gameStateTable)
            .document(appPreferences.player_id)
            .get()
            .addOnSuccessListener { result ->
                val gs = GameState()
                gs.playerState.lux = result.get("playerState.lux") as Double
                resolve(gs)
            }.addOnFailureListener { except ->
                reject(except)
            }
    }
}