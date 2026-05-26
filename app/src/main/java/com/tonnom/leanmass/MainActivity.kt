package com.tonnom.leanmass

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tonnom.leanmass.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Définition des normes (Le cahier des charges demande >= 38 pour l'homme et >= 24 pour la femme)
    private val NORM_MALE = 38.0
    private val NORM_FEMALE = 24.0
    private lateinit var dbHelper: DatabaseHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //  Protection MASVS-UI : Interdit les captures d'écran et masque l'app en arrière-plan
        window.setFlags(android.view.WindowManager.LayoutParams.FLAG_SECURE, android.view.WindowManager.LayoutParams.FLAG_SECURE)

        // Initialisation de ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbHelper = DatabaseHelper(this)

        // Que se passe-t-il quand on clique sur le bouton "Calculer" ?
        binding.btnCalculate.setOnClickListener {

            // 1. On récupère ce que l'utilisateur a tapé
            val weightStr = binding.etWeight.text.toString().trim()
            val heightStr = binding.etHeight.text.toString().trim()

            // 2. Vérification : est-ce que les champs sont vides ?
            if (weightStr.isEmpty() || heightStr.isEmpty()) {
                Toast.makeText(this, "Veuillez entrer votre poids et votre taille", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // On arrête le code ici
            }

            // On convertit le texte en nombres décimaux
            val weight = weightStr.toDoubleOrNull()
            val height = heightStr.toDoubleOrNull()

            if (weight == null || height == null) {
                Toast.makeText(this, "Valeurs invalides", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 3. Préparation des variables pour le résultat
            var lbm = 0.0
            var isSatisfactory = false
            var message = ""

            // 4. Calcul selon la méthode de Boer
            if (binding.rbMale.isChecked) {
                // Formule Homme
                lbm = (0.407 * weight) + (0.267 * height) - 19.2
                isSatisfactory = lbm >= NORM_MALE
                message = "Seuil homme : 38 kg"
            } else {
                // Formule Femme
                lbm = (0.252 * weight) + (0.473 * height) - 48.3
                isSatisfactory = lbm >= NORM_FEMALE
                message = "Seuil femme : ${NORM_FEMALE.toInt()} kg"
            }

            // On arrondit le résultat à 2 chiffres après la virgule
            val lbmFormatted = String.format("%.2f", lbm)

            // 5. Mise à jour de l'interface graphique

            // On rend la carte visible
            binding.cvResult.visibility = View.VISIBLE
            binding.tvResultValue.text = "$lbmFormatted kg"

            if (isSatisfactory) {
                // Résultat Satisfaisant : Carte Verte avec icône contente
                binding.cvResult.setCardBackgroundColor(Color.parseColor("#E8F5E9")) // Fond vert clair
                binding.cvResult.strokeColor = Color.parseColor("#C8E6C9") // Bordure verte
                binding.tvResultIcon.text = "😊"
                binding.tvResultMessage.text = "$message - Satisfaisant"
            } else {
                // Résultat Insatisfaisant : Carte Rouge avec icône triste
                binding.cvResult.setCardBackgroundColor(Color.parseColor("#FDECE8")) // Fond rouge clair
                binding.cvResult.strokeColor = Color.parseColor("#FAD4C9") // Bordure rouge
                binding.tvResultIcon.text = "😞"
                binding.tvResultMessage.text = "$message - Insatisfaisant"
            }

            // --- SAUVEGARDE DANS L'HISTORIQUE ---
            val genderToSave = if (binding.rbMale.isChecked) "Homme" else "Femme"
            val statusToSave = if (isSatisfactory) "Satisfaisant" else "Insatisfaisant"

            val resultId = dbHelper.insertCalculation(
                gender = genderToSave,
                weight = weight,
                height = height,
                lbm = lbmFormatted,
                status = statusToSave
            )

            if (resultId != -1L) {
                Toast.makeText(this, "Calcul sauvegardé !", Toast.LENGTH_SHORT).show()
            }

            // Action pour ouvrir la page Historique
            binding.btnViewHistory.setOnClickListener {
                val intent = android.content.Intent(this, HistoryActivity::class.java)
                startActivity(intent)
            }
        }
    }
}