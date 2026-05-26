package com.tonnom.leanmass

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import com.tonnom.leanmass.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Protection MASVS-UI : Interdit les captures d'écran et masque l'app en arrière-plan
        window.setFlags(android.view.WindowManager.LayoutParams.FLAG_SECURE, android.view.WindowManager.LayoutParams.FLAG_SECURE)

        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbHelper = DatabaseHelper(this)

        // 1. On charge l'historique dès l'ouverture de la page
        chargerHistorique()

        // 2. Action du bouton "Vider"
        binding.tvDeleteAll.setOnClickListener {
            dbHelper.deleteAllCalculations()
            Toast.makeText(this, "Historique supprimé", Toast.LENGTH_SHORT).show()
            chargerHistorique() // On recharge la liste (qui sera donc vide)
        }
    }

    private fun chargerHistorique() {
        // On nettoie l'écran au cas où il y aurait déjà des choses
        binding.llHistoryContainer.removeAllViews()

        val cursor = dbHelper.getAllCalculations()

        if (cursor.count == 0) {
            // S'il n'y a aucun calcul, on peut afficher un petit message sympa
            val tvEmpty = TextView(this)
            tvEmpty.text = "Aucun calcul enregistré pour le moment."
            tvEmpty.textSize = 16f
            tvEmpty.setTextColor(Color.GRAY)
            tvEmpty.textAlignment = View.TEXT_ALIGNMENT_CENTER
            binding.llHistoryContainer.addView(tvEmpty)
        } else {
            // S'il y a des données, on les parcourt une par une
            if (cursor.moveToFirst()) {
                do {
                    // On récupère les colonnes
                    val gender = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_GENDER))
                    val weight = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_WEIGHT))
                    val height = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_HEIGHT))
                    val lbm = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LBM))
                    val status = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STATUS))
                    val date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE))

                    // On "gonfle" (photocopie) notre fichier item_history.xml
                    val cardView = layoutInflater.inflate(R.layout.item_history, binding.llHistoryContainer, false)

                    // On fait la liaison avec les éléments de cette petite carte
                    val cvBackground = cardView.findViewById<MaterialCardView>(R.id.cvHistoryCard)
                    val tvIcon = cardView.findViewById<TextView>(R.id.tvHistIcon)
                    val tvLbm = cardView.findViewById<TextView>(R.id.tvHistLbm)
                    val tvDetails = cardView.findViewById<TextView>(R.id.tvHistDetails)
                    val tvDate = cardView.findViewById<TextView>(R.id.tvHistDate)

                    // On remplit avec les vraies données
                    tvLbm.text = "$lbm"
                    tvDetails.text = "$gender • $weight kg • $height cm"

                    // On coupe la date pour ne garder que le jour (ex: "25/05/2026")
                    tvDate.text = date.substringBefore(" ")

                    // On gère les couleurs selon le statut
                    if (status == "Satisfaisant") {
                        cvBackground.setCardBackgroundColor(Color.parseColor("#F1F8F4")) // Vert très très clair
                        tvIcon.text = "😊"
                    } else {
                        cvBackground.setCardBackgroundColor(Color.parseColor("#FDEDEA")) // Rouge très très clair
                        tvIcon.text = "😞"
                    }

                    // On ajoute cette carte complétée dans le grand conteneur de la page
                    binding.llHistoryContainer.addView(cardView)

                } while (cursor.moveToNext())
            }
        }
        cursor.close()
    }
}