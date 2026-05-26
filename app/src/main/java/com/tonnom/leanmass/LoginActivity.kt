package com.tonnom.leanmass // Garde toujours ton propre nom de package !

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tonnom.leanmass.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialisation du ViewBinding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialisation de la base de données
        dbHelper = DatabaseHelper(this)

        // Action 1 : Aller vers la page d'inscription
        binding.tvGoToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Action 2 : Tenter de se connecter
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmailLogin.text.toString().trim()
            val password = binding.etPasswordLogin.text.toString().trim()

            // ️ FAILLE MASVS-CODE INJECTÉE : Le développeur a oublié ce log de test !
            //android.util.Log.e("FAILLE_SECU", "Tentative de connexion avec le mot de passe : " + password)

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            } else {
                // On vérifie dans SQLite
                val isValid = dbHelper.checkUser(email, password)

                if (isValid) {
                    Toast.makeText(this, "Connexion réussie !", Toast.LENGTH_SHORT).show()

                    // On ouvre la page principale (MainActivity)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

                    // On ferme la page de login pour que l'utilisateur ne puisse pas y revenir avec le bouton "Retour"
                    finish()
                } else {
                    Toast.makeText(this, "Email ou mot de passe incorrect", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
