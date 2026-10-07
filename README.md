# 🔐 LeanMass Calculator — Android Security Audit

Android application for calculating Lean Body Mass (LBM), developed in Kotlin and used as a practical project for studying **Android application security** and applying the **OWASP MASVS** methodology.

The project focuses on identifying, exploiting in a controlled environment, remediating and validating common mobile application security vulnerabilities.

---

## 🎯 Project Overview

**LeanMass Calculator** is an Android application that allows users to:

- Create an account and authenticate locally
- Calculate Lean Body Mass (LBM)
- Store calculation history
- Access a personal space containing their data

The application was also used as a practical security case study to understand how sensitive data can be exposed through insecure Android development practices.

---

## 🛡️ Security Audit

The application was intentionally analyzed with several security weaknesses in order to demonstrate a complete security workflow:

**Vulnerability identification → Proof of Concept → Remediation → Validation**

The audit was performed with reference to the **OWASP Mobile Application Security Verification Standard (MASVS)**.

### Identified Vulnerabilities

| Vulnerability | OWASP MASVS | Risk |
|---|---|---|
| Plaintext password storage | MASVS-STORAGE-1 | 🔴 High |
| Sensitive data visible in screenshots / recent apps | MASVS-UI-1 | 🟠 Medium |
| Weak password policy | MASVS-AUTH-1 | 🟠 Medium |
| Passwords exposed through Logcat | MASVS-CODE-2 | 🔴 High |

---

## 🔎 1. Plaintext Password Storage

### Vulnerability

User passwords were initially stored directly in the local SQLite database without protection.

This allowed sensitive authentication data to be retrieved from the application's local database.

### Remediation

Password storage was modified so that passwords are transformed using **SHA-256** before being stored and during authentication.

> ⚠️ SHA-256 was used as part of this academic security exercise. For a production authentication system, a dedicated password hashing/KDF mechanism such as **Argon2id, bcrypt, scrypt or PBKDF2 with a unique salt** would be preferable.

---

## 📱 2. Sensitive Data Exposure Through Screenshots

### Vulnerability

Sensitive information displayed by the application could potentially remain visible in screenshots or the Android recent-apps screen.

### Remediation

Android's `FLAG_SECURE` protection was added to sensitive activities:

- `MainActivity`
- `HistoryActivity`

This prevents the application content from being captured through standard screenshots and reduces exposure in the recent-apps view.

---

## 🔑 3. Weak Password Policy

### Vulnerability

The original application accepted weak passwords that did not provide sufficient protection against common password attacks.

### Remediation

A stronger password policy was implemented.

The password must contain:

- At least 8 characters
- At least one uppercase letter
- At least one number

Invalid passwords are rejected during registration.

---

## 📝 4. Password Exposure in Logcat

### Vulnerability

Password-related information could be exposed through Android Logcat because of debug logging statements.

Example:

```kotlin
Log.e("FAILLE_SECU", ...)

## 🛠️ Security Remediation

Following the security assessment, the identified vulnerabilities were addressed through targeted remediation measures.

### Implemented Remediations

* Removed sensitive logging statements from the application.
* Prevented password information from being written to Android Logcat.
* Improved local authentication data protection.
* Strengthened password validation rules.
* Enabled screenshot protection on sensitive screens using `FLAG_SECURE`.

These changes were followed by validation tests to ensure that the implemented security controls were effective.

---

## 🧪 Security Testing Methodology

The security assessment followed a structured workflow:

```text
Application Analysis
        ↓
Vulnerability Identification
        ↓
Proof of Concept
        ↓
Security Remediation
        ↓
Validation Testing
```

### 🔧 Tools & Techniques

* **Android Studio**
* **Kotlin**
* **SQLite**
* **Android Device File Explorer**
* **Android Logcat**
* **Android Security Mechanisms**
* **OWASP MASVS**

---

## 🏗️ Technical Stack

### 📱 Mobile Application

* **Kotlin**
* **Android SDK**
* **XML**
* **Material Design**
* **ViewBinding**

### 💾 Data Storage

* **SQLite**

### 🔐 Security

* **OWASP MASVS**
* **SHA-256**
* **Android `FLAG_SECURE`**
* **Password Policy Validation**
* **Secure Logging Practices**

---

## 📂 Project Structure

```text
LeanMass-Calculator/
│
├── app/
│   └── src/
│       └── main/
│           ├── java/
│           ├── res/
│           └── AndroidManifest.xml
│
├── gradle/
│   └── wrapper/
│
├── build.gradle
├── gradle.properties
├── gradlew
├── gradlew.bat
└── settings.gradle
```

---

## 🔎 Security Validation

After remediation, the implemented security controls were validated through practical security tests.

### 💾 Local Authentication Data

**Before remediation:**

```text
User Password
      ↓
SQLite Database
      ↓
Plaintext Password
```

The password was directly readable in the local database.

**After remediation:**

```text
User Password
      ↓
SHA-256 Transformation
      ↓
Stored Hash
      ↓
Authentication Verification
```

The application no longer stores the password in plaintext.

> **Note:** For production-grade password storage, a password-specific hashing algorithm such as Argon2id, bcrypt, scrypt, or PBKDF2 with a unique salt should be preferred over a simple SHA-256 hash.

### 🖼️ Screenshot Protection

`FLAG_SECURE` was implemented on sensitive activities and tested to verify that application content could not be captured through standard Android screenshots.

### 🔑 Password Policy

Weak passwords were tested against the updated validation rules and correctly rejected when they did not meet the required criteria.

### 📋 Logcat Security

Sensitive logging statements were removed and Logcat was monitored during application execution to verify that password information was no longer exposed.

---

## 📊 OWASP MASVS Mapping

| Security Area     | Implementation                                         |
| ----------------- | ------------------------------------------------------ |
| **MASVS-STORAGE** | Protection of locally stored authentication data       |
| **MASVS-UI**      | `FLAG_SECURE` for sensitive screens                    |
| **MASVS-AUTH**    | Stronger password validation                           |
| **MASVS-CODE**    | Removal of sensitive information from application logs |

---

## 🎓 Learning Outcomes

This project provided hands-on experience in:

* 📱 Android application security assessment
* 🔎 Vulnerability identification
* 💾 Local database security
* 🔐 Authentication security
* 🛡️ Secure coding practices
* 🔒 Sensitive data protection
* 📋 Log analysis
* 📚 OWASP MASVS
* 🛠️ Security remediation
* 🧪 Security validation and testing

The project demonstrates the importance of integrating security throughout the application lifecycle — from **development and assessment to remediation and validation**.

---

## 🚀 Future Security Improvements

Several additional security enhancements could further strengthen the application:

* Replace SHA-256 with a password-specific hashing algorithm such as **Argon2id**, **bcrypt**, **scrypt**, or **PBKDF2 with a unique salt**.
* Protect cryptographic keys using the **Android Keystore**.
* Encrypt sensitive locally stored information.
* Implement stronger authentication mechanisms.
* Introduce automated security testing.
* Perform static analysis using Android security tools.
* Conduct dynamic application security testing.
* Expand the implementation to cover additional **OWASP MASVS** controls.

---

## 📚 References

* [OWASP Mobile Application Security Verification Standard (MASVS)](https://mas.owasp.org/)
* [Android Security](https://developer.android.com/privacy-and-security/security)
* [Android Developers Documentation](https://developer.android.com/)

---

## 👩‍💻 Author

**Assma MASRAFI**

Cybersecurity Engineering Student — **ENSA Agadir**

### Areas of Interest

`Mobile Security` • `Application Security` • `SOC` • `Blue Team` • `GRC`

🔗 **GitHub:** [asmamasrafi](https://github.com/asmamasrafi)
