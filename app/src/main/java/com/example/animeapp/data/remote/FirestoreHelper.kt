package com.example.animeapp.data.remote

import android.util.Log
import com.example.animeapp.data.model.Anime
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object FirestoreHelper {
    private val db = FirebaseFirestore.getInstance()

    suspend fun saveReviewToServer(userId: String, anime: Anime) {
        try {
            val reviewData = hashMapOf(
                "mal_id" to anime.mal_id,
                "title" to anime.title,
                "score" to anime.score,
                "status" to anime.status,
                "userNote" to anime.userNote,
                "image_url" to anime.image_url // temp
            )

            db.collection("users").document(userId)
                .collection("reviews").document(anime.mal_id.toString())
                .set(reviewData).await()

            Log.d("Firestore", "Berhasil simpan ke server!")
        } catch (e: Exception) {
            Log.e("Firestore", "Gagal simpan: ${e.message}")
        }
    }

    suspend fun deleteReviewFromServer(userId: String, malId: Int?) {
        if (malId == null) return
        try {
            db.collection("users").document(userId)
                .collection("reviews").document(malId.toString())
                .delete().await()

            Log.d("Firestore", "Berhasil hapus dari server!")
        } catch (e: Exception) {
            Log.e("Firestore", "Gagal hapus: ${e.message}")
        }
    }
}