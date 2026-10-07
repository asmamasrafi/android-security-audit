# 🔐 Android Security Audit

Security assessment and remediation of an Android application developed in Kotlin, based on the OWASP Mobile Application Security Verification Standard (MASVS).

The project demonstrates a practical security workflow:

**Vulnerability Identification → Proof of Concept → Remediation → Validation**

---

## 📱 Project Overview

**Android Security Audit** is a security-focused assessment of an Android application used to calculate **Lean Body Mass (LBM)** based on the Boer formula.

The application provides:

- 👤 Local user registration and authentication
- ⚖️ Lean Body Mass calculation
- 📊 Calculation history
- 🔐 Personal user space
- 💾 Local SQLite data storage

Beyond its functional purpose, the application was used as a practical case study to identify and remediate common Android security weaknesses.

---

## 🎯 Security Objectives

The main objectives of the security assessment were to:

- Identify vulnerabilities affecting sensitive user data
- Analyze insecure data storage mechanisms
- Assess authentication and password security
- Identify sensitive information exposed through application logs
- Protect sensitive application screens
- Apply appropriate security remediation measures
- Validate the effectiveness of the implemented fixes

---

# 🛡️ Security Assessment

The assessment was conducted using a practical penetration-testing and secure-development workflow:

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
# 🔎 Security Vulnerability Assessment

The security assessment identified several weaknesses related to **authentication, local data storage, sensitive information exposure, and insecure logging**.

Each finding was analyzed, reproduced in a controlled environment, remediated, and validated against relevant **OWASP MASVS** security controls.

---

## 📊 Vulnerability Summary

| # | Vulnerability                               | OWASP MASVS   | Risk      |
| - | ------------------------------------------- | ------------- | --------- |
| 1 | Plaintext password storage                  | MASVS-STORAGE | 🔴 High   |
| 2 | Sensitive data exposure through screenshots | MASVS-UI      | 🟠 Medium |
| 3 | Weak password policy                        | MASVS-AUTH    | 🟠 Medium |
| 4 | Password exposure through Logcat            | MASVS-CODE    | 🔴 High   |

---

# 🔴 1. Plaintext Password Storage

## Vulnerability

During the initial security assessment, user passwords were stored directly in the application's local **SQLite database**.

An attacker with access to the application's local storage could potentially retrieve authentication credentials in readable form.

### Security Impact

* 🔑 Exposure of user credentials
* ⚠️ Increased risk of credential reuse attacks
* 💾 Sensitive authentication data accessible from local storage
* 🚨 Potential compromise of user accounts

## Proof of Concept

The application's SQLite database was inspected using Android development tools.

The vulnerable implementation stored the password directly:

```text
User
  ↓
Registration
  ↓
SQLite Database
  ↓
Plaintext Password
```

## Remediation

The application was modified so that passwords are transformed before being stored and during authentication.

```text
User Password
      ↓
   SHA-256
      ↓
Stored Hash
      ↓
Authentication Verification
```

> ⚠️ **Security note:** SHA-256 was used as part of this academic security exercise. For production authentication systems, a dedicated password hashing/KDF mechanism such as **Argon2id, bcrypt, scrypt, or PBKDF2 with a unique salt and appropriate work factor** should be used.

---

# 🟠 2. Sensitive Data Exposure Through Screenshots

## Vulnerability

Sensitive information displayed by the application could potentially remain visible through **screenshots and the Android recent-apps screen**.

This could expose user-related information while the application was running in the background.

### Potentially Exposed Information

* 👤 Personal information
* 📊 Health-related calculation data
* 📜 Calculation history
* 🔐 Sensitive application content

## Remediation

Android's `FLAG_SECURE` mechanism was implemented on sensitive activities:

* `MainActivity`
* `HistoryActivity`

This prevents standard screenshots and screen-capture mechanisms from capturing protected application content.

### Implementation

```kotlin
window.setFlags(
    WindowManager.LayoutParams.FLAG_SECURE,
    WindowManager.LayoutParams.FLAG_SECURE
)
```

---

# 🟠 3. Weak Password Policy

## Vulnerability

The initial authentication mechanism accepted passwords that did not meet a sufficiently strong password policy.

This could increase the risk of password guessing and common-password attacks.

## Remediation

A stronger password validation policy was introduced.

Passwords must contain:

| Requirement      | Rule             |
| ---------------- | ---------------- |
| Minimum length   | **8 characters** |
| Uppercase letter | **Required**     |
| Number           | **Required**     |

```text
Minimum length: 8 characters
Uppercase letter: Required
Number: Required
```

Passwords that do not satisfy these requirements are rejected during registration.

---

# 🔴 4. Password Exposure Through Logcat

## Vulnerability

During the security assessment, sensitive password information could be exposed through **Android Logcat** because of insecure debug logging.

A sensitive logging statement was identified:

```kotlin
Log.e("FAILLE_SECU", ...)
```

This created a risk of exposing authentication information through application logs.

## Security Impact

Sensitive information should never be written to application logs.

Potential consequences include:

* 🔐 Exposure of authentication information
* 📋 Sensitive data remaining in debugging logs
* ⚠️ Increased risk when logs are accessible in a development or testing environment

## Remediation

Sensitive logging statements were removed from the application.

The application was subsequently tested while monitoring Logcat to verify that password information was no longer exposed.

---

# 🧪 Security Testing & Validation

Each identified vulnerability was tested **before and after remediation**.

| Security Test               | Before              | After                 |
| --------------------------- | ------------------- | --------------------- |
| SQLite password storage     | ❌ Plaintext         | ✅ Transformed         |
| Screenshot protection       | ❌ Possible exposure | ✅ `FLAG_SECURE`       |
| Password validation         | ❌ Weak policy       | ✅ Stronger validation |
| Password exposure in Logcat | ❌ Sensitive logs    | ✅ Removed             |

The validation phase confirmed that the implemented controls addressed the identified weaknesses within the scope of the project.

---

# 🛠️ Tools & Techniques

## 💻 Development

* **Kotlin**
* **Android SDK**
* **XML**
* **Material Design**
* **ViewBinding**
* **SQLite**

## 🔐 Security Assessment

* **OWASP MASVS**
* **Android Logcat**
* **Android Device File Explorer**
* **SQLite database inspection**
* **Static code analysis**
* **Manual security testing**
* **Proof-of-Concept testing**
* **Security remediation**
* **Validation testing**

---

# 🏗️ Application Architecture

```text
┌─────────────────────────────────┐
│       Android Application       │
│                                 │
│     Kotlin + XML + ViewBinding  │
└────────────────┬────────────────┘
                 │
                 ▼
┌─────────────────────────────────┐
│        Application Logic        │
│                                 │
│  • Authentication               │
│  • LBM Calculation              │
│  • History Management           │
└────────────────┬────────────────┘
                 │
                 ▼
┌─────────────────────────────────┐
│             SQLite              │
│                                 │
│  • User Data                    │
│  • Authentication Data          │
│  • Calculation History           │
└─────────────────────────────────┘
```

---

# 🔐 Security Controls Implemented

The remediation phase introduced security controls across three main areas.

## 🔑 Authentication

* Stronger password validation
* Password transformation before local storage
* Secure password verification
* Rejection of weak passwords

## 💾 Data Protection

* Reduced exposure of sensitive local data
* Protection of sensitive application screens
* Improved handling of authentication information
* Removal of plaintext password storage

## 📋 Logging

* Removal of sensitive information from Logcat
* Elimination of password-related debug logs
* Validation of application logs after remediation

## 🛡️ Secure Development

* Security requirements mapped to OWASP MASVS
* Manual security testing
* Proof-of-Concept validation
* Remediation of identified vulnerabilities
* Regression testing after remediation

---

# 📂 Project Structure

```text
android-security-audit/
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
├── .gitignore
├── build.gradle
├── gradle.properties
├── gradlew
├── gradlew.bat
└── settings.gradle
```

---

# 📸 Security Evidence

The security assessment was supported by practical evidence collected during the vulnerability analysis and validation phases.

Evidence includes:

* 🗄️ SQLite database containing plaintext passwords before remediation
* 🔐 SQLite database showing transformed password values after remediation
* 🖼️ `FLAG_SECURE` implementation and validation
* 🔑 Weak password rejection tests
* 📋 Logcat output showing sensitive information before remediation
* ✅ Logcat validation confirming the removal of sensitive password information

> 📌 Screenshots and additional evidence can be added to the repository to document the assessment and remediation process.

---

# 📚 OWASP MASVS Mapping

| OWASP MASVS Area  | Security Objective               | Implementation                              |
| ----------------- | -------------------------------- | ------------------------------------------- |
| **MASVS-STORAGE** | Secure local storage             | Password protection before database storage |
| **MASVS-UI**      | Sensitive information protection | `FLAG_SECURE`                               |
| **MASVS-AUTH**    | Authentication security          | Password strength validation                |
| **MASVS-CODE**    | Secure coding practices          | Removal of sensitive logging                |

---

# 📈 Security Assessment Methodology

The project followed a simplified **Mobile Application Security Assessment Lifecycle**:

### 01 — Application Analysis

Understanding the application's architecture, authentication workflow, local storage, and user-facing functionalities.

### 02 — Vulnerability Identification

Analyzing the application for:

* Insecure local storage
* Authentication weaknesses
* Sensitive UI exposure
* Insecure logging practices

### 03 — Proof of Concept

Reproducing the identified vulnerabilities in a controlled testing environment.

### 04 — Remediation

Implementing appropriate security controls directly within the Android application.

### 05 — Validation

Repeating the security tests after remediation to verify that the identified weaknesses had been addressed.

```text
┌────────────────────┐
│ Application        │
│ Analysis           │
└─────────┬──────────┘
          ↓
┌────────────────────┐
│ Vulnerability      │
│ Identification     │
└─────────┬──────────┘
          ↓
┌────────────────────┐
│ Proof of Concept   │
└─────────┬──────────┘
          ↓
┌────────────────────┐
│ Remediation        │
└─────────┬──────────┘
          ↓
┌────────────────────┐
│ Validation         │
└────────────────────┘
```

---

# 🎓 Learning Outcomes

This project provided hands-on experience in:

* 📱 Android application security
* 🔎 Mobile application security assessment
* 📚 OWASP MASVS
* 🔐 Authentication security
* 💾 Local data protection
* 🗄️ SQLite security
* 🛡️ Android security mechanisms
* 📋 Logcat analysis
* 🔍 Vulnerability assessment
* 🧪 Proof-of-Concept development
* 🛠️ Security remediation
* ✅ Security validation

Beyond identifying vulnerabilities, the project provided practical experience in the complete security workflow:

> **Identify → Analyze → Exploit → Remediate → Validate**

This reinforces the principle that security should be integrated throughout the **Software Development Lifecycle (SDLC)** rather than addressed only after implementation.

---

# 🚀 Future Security Improvements

For a more production-oriented implementation, the following improvements could be considered:

### 🔐 Authentication

* Replace SHA-256 with **Argon2id, bcrypt, scrypt, or PBKDF2**
* Use a unique cryptographic salt for password hashing
* Introduce stronger authentication mechanisms
* Consider multi-factor authentication where appropriate

### 💾 Data Protection

* Protect cryptographic keys using **Android Keystore**
* Encrypt sensitive locally stored information
* Strengthen database protection
* Minimize sensitive data stored on the device

### 🧪 Security Testing

* Add automated security tests
* Perform **Static Application Security Testing (SAST)**
* Perform **Dynamic Application Security Testing (DAST)**
* Introduce dependency and vulnerability scanning
* Extend the assessment to additional OWASP MASVS controls

### 🔄 DevSecOps

* Integrate security checks into CI/CD
* Automate vulnerability detection
* Introduce security gates before deployment
* Perform regular dependency and security reviews

---

# 📖 References

* [OWASP Mobile Application Security Verification Standard (MASVS)](https://mas.owasp.org/)
* [Android Security Documentation](https://developer.android.com/privacy-and-security/security)
* [Android Developers Documentation](https://developer.android.com/)

---

# 👩‍💻 Author

**Assma MASRAFI**

Cybersecurity Engineering Student — **ENSA Agadir**

### Areas of Interest

`Mobile Security` • `Application Security` • `SOC` • `Blue Team` • `GRC`

🔗 **GitHub:** [@asmamasrafi](https://github.com/asmamasrafi)

---

## ⭐ Project Focus

This project goes beyond vulnerability identification by demonstrating a complete security assessment lifecycle:

**🔎 Find → 🧠 Understand → 🧪 Demonstrate → 🛠️ Remediate → ✅ Validate**
