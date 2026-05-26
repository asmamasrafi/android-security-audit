package com.tonnom.leanmass

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "LeanMassDB.db"
        private const val DATABASE_VERSION = 3

        // Table Utilisateurs
        const val TABLE_USERS = "users"
        const val COLUMN_ID = "id"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PASSWORD = "password"

        // NOUVELLE Table Historique
        const val TABLE_HISTORY = "history"
        const val COLUMN_HIST_ID = "hist_id"
        const val COLUMN_GENDER = "gender"
        const val COLUMN_WEIGHT = "weight"
        const val COLUMN_HEIGHT = "height"
        const val COLUMN_LBM = "lbm"
        const val COLUMN_STATUS = "status"
        const val COLUMN_DATE = "date"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // 1. Création table Users
        val createTableUsers = ("CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_EMAIL + " TEXT UNIQUE,"
                + COLUMN_PASSWORD + " TEXT" + ")")
        db.execSQL(createTableUsers)

        // 2. Création table History
        val createTableHistory = ("CREATE TABLE " + TABLE_HISTORY + "("
                + COLUMN_HIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_GENDER + " TEXT,"
                + COLUMN_WEIGHT + " REAL,"
                + COLUMN_HEIGHT + " REAL,"
                + COLUMN_LBM + " TEXT,"
                + COLUMN_STATUS + " TEXT,"
                + COLUMN_DATE + " TEXT" + ")")
        db.execSQL(createTableHistory)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_HISTORY")
        onCreate(db)
    }

    // --- FONCTIONS UTILISATEURS (Existantes) ---

    fun insertUser(email: String, pass: String): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_EMAIL, email)

        // On hache le mot de passe avant de l'insérer
        val hashedPassword = hashPassword(pass)
        values.put(COLUMN_PASSWORD, hashedPassword)

        return db.insert(TABLE_USERS, null, values)
    }

    fun checkUser(email: String, pass: String): Boolean {
        val db = this.readableDatabase
        val columns = arrayOf(COLUMN_ID)

        // On hache le mot de passe tapé pour le comparer à celui de la base
        val hashedPassword = hashPassword(pass)

        val selection = "$COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?"
        val selectionArgs = arrayOf(email, hashedPassword)
        val cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null)
        val count = cursor.count
        cursor.close()
        return count > 0
    }
    // --- NOUVELLE FONCTION POUR L'HISTORIQUE ---

    fun insertCalculation(gender: String, weight: Double, height: Double, lbm: String, status: String): Long {
        val db = this.writableDatabase
        val values = ContentValues()

        // Génération de la date actuelle (ex: 25/05/2026 14:30)
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val currentDate = sdf.format(Date())

        values.put(COLUMN_GENDER, gender)
        values.put(COLUMN_WEIGHT, weight)
        values.put(COLUMN_HEIGHT, height)
        values.put(COLUMN_LBM, lbm)
        values.put(COLUMN_STATUS, status)
        values.put(COLUMN_DATE, currentDate)

        return db.insert(TABLE_HISTORY, null, values)
    }

    // Fonction pour récupérer tout l'historique (du plus récent au plus ancien)
    fun getAllCalculations(): android.database.Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_HISTORY ORDER BY $COLUMN_HIST_ID DESC", null)
    }

    // Fonction pour vider l'historique
    fun deleteAllCalculations() {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM $TABLE_HISTORY")
    }


    // Fonction de sécurité : Hachage du mot de passe en SHA-256
    private fun hashPassword(password: String): String {
        val bytes = java.security.MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

}