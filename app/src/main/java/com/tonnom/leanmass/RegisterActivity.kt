package com.tonnom.leanmass

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tonnom.leanmass.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Initialiser le ViewBinding pour lier le XML au Kotlin
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 2. Initialiser la base de données SQLite
        dbHelper = DatabaseHelper(this)

        // 3. Donner l'ordre au bouton "S'inscrire"
        binding.btnRegister.setOnClickListener {

            // On récupère le texte tapé dans les champs
            val email = binding.etEmailRegister.text.toString().trim()
            val password = binding.etPasswordRegister.text.toString().trim()

// On vérifie que les champs ne sont pas vides
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            } else {

                //  Correction MASVS-AUTH : Vérification de la robustesse du mot de passe
                val passwordPattern = "^(?=.*[A-Z])(?=.*[0-9]).{8,}$".toRegex()

                if (!passwordPattern.matches(password)) {
                    Toast.makeText(this, "Mot de passe trop faible ! (Min 8 caractères, 1 majuscule, 1 chiffre)", Toast.LENGTH_LONG).show()
                    return@setOnClickListener // On arrête le code ici, on ne va pas jusqu'à l'insertion
                }

                // On insère l'utilisateur dans la base de données SQLite (Seulement si le mot de passe est fort !)
                val result = dbHelper.insertUser(email, password)

                if (result != -1L) {
                    // Si l'insertion a réussi
                    Toast.makeText(this, "Inscription réussie !", Toast.LENGTH_SHORT).show()

                    // finish() ferme la page d'inscription et te ramène automatiquement à la page de Connexion
                    finish()
                } else {
                    // Si la base de données renvoie une erreur (ex: email déjà utilisé)
                    Toast.makeText(this, "Erreur ou email déjà existant", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}