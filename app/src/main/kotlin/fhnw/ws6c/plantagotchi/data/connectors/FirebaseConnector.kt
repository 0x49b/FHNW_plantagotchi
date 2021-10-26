package fhnw.ws6c.plantagotchi.data.connectors

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import fhnw.ws6c.plantagotchi.AppPreferences
import fhnw.ws6c.plantagotchi.data.state.GameState
import com.google.firebase.firestore.QuerySnapshot

import androidx.annotation.NonNull

import com.google.android.gms.tasks.OnCompleteListener


class FirebaseConnector(var appPreferences: AppPreferences) {

    private val TAG = "FirebaseConnector"
    private val db = Firebase.firestore
    private val gameStateTable = "gameState"




    fun writeGameState(gameState: GameState) {

        if (appPreferences.contains("PLAYER_ID") &&
            appPreferences.player_id.isNotBlank() &&
            appPreferences.player_id.isNotEmpty()
        ) {
            db.collection(gameStateTable)
                .document(appPreferences.player_id)
                .set(gameState.toHashMap())
                .addOnSuccessListener {
                    Log.d(TAG, "GameState updated for ${appPreferences.player_id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        } else {

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

    }

    fun getGameState(): GameState {
        var gameState = GameState()

        return gameState
    }

    fun updateGameState(gameState: GameState){

        db.collection(gameStateTable)
            .document(appPreferences.player_id)
            .update(gameState.toHashMap() as Map<String, Any>)
            .addOnSuccessListener { documentReference ->
            }
            .addOnFailureListener { e ->
            }

    }

    fun updateGameStateProperty(propertyKey: String, propertyValue: String) {
        db.collection(gameStateTable).document(appPreferences.player_id)
            .update(propertyKey, propertyValue).addOnSuccessListener {
            Log.d(TAG, "Updated $propertyKey with $propertyValue for ${appPreferences.player_id}")
        }
    }

}