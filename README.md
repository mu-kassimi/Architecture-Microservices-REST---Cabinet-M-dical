# TP3 - Architecture Microservices REST - Cabinet Médical

## 📋 Vue d'ensemble

Ce projet implémente une architecture microservices pour la gestion d'un cabinet médical, en évolution par rapport à l'architecture SOA du TP2.

### Différences clés vs TP2 (SOA)

| Aspect | TP2 (SOA) | TP3 (Microservices) |
|--------|-----------|---------------------|
| **Données** | Repository partagé (`cabinet-repo`) | Base de données par service |
| **Point d'entrée** | ESB (Apache Camel) | API Gateway (Spring Cloud Gateway) |
| **Communication** | Messages asynchrones | REST synchrone |
| **Autonomie** | Services partagent les entités | Chaque service possède ses entités |

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Clients Externes                          │
│                (Browser, Mobile, etc.)                       │
└──────────────────────┬──────────────────────────────────────┘
                       │
                       ▼
         ┌─────────────────────────┐
         │   API Gateway :8080     │
         │  (Point d'entrée unique) │
         └─────────────────────────┘
                       │
       ┌───────────────┼───────────────┬──────────────┬────────────┐
       ▼               ▼               ▼              ▼            ▼
┌──────────┐    ┌──────────┐    ┌──────────┐   ┌──────────┐  ┌──────────┐
│ Patient  │    │ Médecin  │    │Rendez-   │   │Consulta- │  │ Dossier  │
│ Service  │    │ Service  │    │vous      │   │tion      │  │ Service  │
│  :8082   │    │  :8083   │    │Service   │   │Service   │  │  :8086   │
│          │    │          │    │  :8084   │   │  :8085   │  │(Composite)│
└────┬─────┘    └────┬─────┘    └────┬─────┘   └────┬─────┘  └──────────┘
     │               │               │              │               │
┌────▼─────┐    ┌───▼──────┐   ┌───▼──────┐  ┌───▼──────┐        │
│patientDB │    │medecinDB │   │rendezvous│  │consulta- │        │
│   (H2)   │    │   (H2)   │   │DB (H2)   │  │tionDB(H2)│        │
└──────────┘    └──────────┘   └──────────┘  └──────────┘        │
                                     │              │             │
                                     └──────────────┴─────────────┘
                                     Communication REST inter-services
```

## 📦 Modules du Projet

### Structure Maven Multi-modules

```
cabinetMedicalTp3MS/
├── pom.xml (parent)
├── api-gateway/
├── patient-service/
├── medecin-service/
├── rendezvous-service/
├── consultation-service/
└── dossier-service/
```

### Description des Modules

| Module | Port | Rôle | Base de Données |
|--------|------|------|-----------------|
| **api-gateway** | 8080 | Point d'entrée externe, routage | - |
| **patient-service** | 8082 | Gestion des patients (CRUD) | patientDB |
| **medecin-service** | 8083 | Gestion des médecins (CRUD) | medecinDB |
| **rendezvous-service** | 8084 | Gestion des rendez-vous + vérifications REST | rendezvousDB |
| **consultation-service** | 8085 | Gestion des consultations | consultationDB |
| **dossier-service** | 8086 | Agrégation (service composite) | Aucune (agrégation REST) |

## 🚀 Démarrage Rapide

### Prérequis

- Java 21
- Maven 3.8+
- IDE (IntelliJ IDEA, Eclipse, VS Code)

### Installation

```bash
# Cloner le projet
git clone <votre-repo>
cd cabinetMedicalTp3MS

# Compiler tous les modules
mvn clean install

# Démarrer les services (dans des terminaux séparés)
cd patient-service && mvn spring-boot:run
cd medecin-service && mvn spring-boot:run
cd rendezvous-service && mvn spring-boot:run
cd consultation-service && mvn spring-boot:run
cd dossier-service && mvn spring-boot:run
cd api-gateway && mvn spring-boot:run
```

### Ordre de Démarrage Recommandé

1. **patient-service** (8082)
2. **medecin-service** (8083)
3. **rendezvous-service** (8084) - dépend de patient et médecin
4. **consultation-service** (8085) - dépend de rendez-vous
5. **dossier-service** (8086) - dépend de tous les autres
6. **api-gateway** (8080) - en dernier

## 📡 API Endpoints

### Accès via API Gateway (Port 8080)

Tous les appels externes passent par `http://localhost:8080/api/...`

#### Patients

```http
GET    /api/patients           # Liste tous les patients
GET    /api/patients/{id}      # Obtenir un patient
POST   /api/patients           # Créer un patient
PUT    /api/patients/{id}      # Modifier un patient
DELETE /api/patients/{id}      # Supprimer un patient
```

**Exemple de création de patient :**
```json
POST /api/patients
{
  "nom": "Alami",
  "prenom": "Mohammed",
  "dateNaissance": "1985-05-15",
  "telephone": "0612345678"
}
```

#### Médecins

```http
GET    /api/medecins           # Liste tous les médecins
GET    /api/medecins/{id}      # Obtenir un médecin
POST   /api/medecins           # Créer un médecin
PUT    /api/medecins/{id}      # Modifier un médecin
DELETE /api/medecins/{id}      # Supprimer un médecin
```

**Exemple de création de médecin :**
```json
POST /api/medecins
{
  "nom": "Bennani",
  "prenom": "Fatima",
  "email": "f.bennani@cabinet.ma",
  "specialite": "Cardiologie"
}
```

#### Rendez-vous

```http
GET    /api/rendezvous                    # Liste tous les rendez-vous
GET    /api/rendezvous/{id}               # Obtenir un rendez-vous
GET    /api/rendezvous/patient/{id}       # Rendez-vous d'un patient
GET    /api/rendezvous/medecin/{id}       # Rendez-vous d'un médecin
POST   /api/rendezvous                    # Créer un rendez-vous
PUT    /api/rendezvous/{id}               # Modifier un rendez-vous
PATCH  /api/rendezvous/{id}/statut        # Changer le statut
DELETE /api/rendezvous/{id}               # Supprimer un rendez-vous
```

**Exemple de création de rendez-vous :**
```json
POST /api/rendezvous
{
  "patientId": 1,
  "medecinId": 1,
  "dateRendezVous": "2026-03-15T10:00:00",
  "statut": "PLANIFIE"
}
```

**Statuts possibles :** `PLANIFIE`, `ANNULE`, `TERMINE`

#### Consultations

```http
GET    /api/consultations                    # Liste toutes les consultations
GET    /api/consultations/{id}               # Obtenir une consultation
GET    /api/consultations/rendezvous/{id}    # Consultations d'un rendez-vous
POST   /api/consultations                    # Créer une consultation
PUT    /api/consultations/{id}               # Modifier une consultation
DELETE /api/consultations/{id}               # Supprimer une consultation
```

**Exemple de création de consultation :**
```json
POST /api/consultations
{
  "rendezVousId": 1,
  "dateConsultation": "2026-03-15T10:30:00",
  "rapport": "Patient présente des symptômes de grippe. Prescription d'antibiotiques et repos recommandé."
}
```

#### Dossier Patient (Service Composite)

```http
GET /api/dossiers/patient/{patientId}    # Dossier complet d'un patient
```

**Réponse :**
```json
{
  "patient": {
    "id": 1,
    "nom": "Alami",
    "prenom": "Mohammed",
    "dateNaissance": "1985-05-15",
    "telephone": "0612345678"
  },
  "rendezvous": [
    {
      "id": 1,
      "dateRendezVous": "2026-03-15T10:00:00",
      "statut": "TERMINE",
      "medecinNom": "Dr. Bennani"
    }
  ],
  "consultations": [
    {
      "id": 1,
      "dateConsultation": "2026-03-15T10:30:00",
      "rapport": "Patient présente des symptômes..."
    }
  ]
}
```

## 🔒 Règles de Gestion

### Patient Service

| Règle | Message d'erreur |
|-------|------------------|
| Nom obligatoire | "Le nom du patient est obligatoire." |
| Téléphone obligatoire | "Le téléphone du patient est obligatoire." |
| Date de naissance pas future | "La date de naissance ne peut pas être future" |
| Patient inexistant | "Patient introuvable : id = X." |

### Médecin Service

| Règle | Message d'erreur |
|-------|------------------|
| Nom obligatoire | "Le nom du médecin est obligatoire." |
| Email obligatoire | "L'email du médecin est obligatoire." |
| Email valide (contient @) | "Email du médecin invalide." |
| Spécialité obligatoire | "La spécialité du médecin est obligatoire." |
| Médecin inexistant | "Médecin introuvable : id = X." |

### Rendez-vous Service

| Règle | Message d'erreur |
|-------|------------------|
| Date future uniquement | "La date du rendez-vous doit être future." |
| Patient doit exister (vérif REST) | "Patient introuvable." |
| Médecin doit exister (vérif REST) | "Médecin introuvable" |
| Statuts valides | "Statut invalide. Valeurs possibles : PLANIFIE, ANNULE, TERMINE." |
| Statut par défaut | PLANIFIE |

### Consultation Service

| Règle | Message d'erreur |
|-------|------------------|
| Rendez-vous doit exister (vérif REST) | "Rendez-vous introuvable." |
| Date consultation obligatoire | "La date de consultation est obligatoire." |
| Date ≥ date RDV | "Date de consultation invalide." |
| Rapport min 10 caractères | "Rapport de consultation insuffisant." |

## 🛠️ Configuration des Services

### Ports des Services

```properties
api-gateway:         8080
patient-service:     8082
medecin-service:     8083
rendezvous-service:  8084
consultation-service: 8085
dossier-service:     8086
```

### Bases de Données H2

Chaque service a sa propre base H2 en mémoire :

```properties
patient-service:     jdbc:h2:mem:patientDB
medecin-service:     jdbc:h2:mem:medecinDB
rendezvous-service:  jdbc:h2:mem:rendezvousDB
consultation-service: jdbc:h2:mem:consultationDB
```

**Console H2 accessible via :** `http://localhost:{port}/h2-console`

## 🧪 Tests avec curl ou Postman

### Scénario de Test Complet

```bash
# 1. Créer un patient
curl -X POST http://localhost:8080/api/patients \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Alami",
    "prenom": "Mohammed",
    "dateNaissance": "1985-05-15",
    "telephone": "0612345678"
  }'

# 2. Créer un médecin
curl -X POST http://localhost:8080/api/medecins \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Bennani",
    "prenom": "Fatima",
    "email": "f.bennani@cabinet.ma",
    "specialite": "Cardiologie"
  }'

# 3. Créer un rendez-vous
curl -X POST http://localhost:8080/api/rendezvous \
  -H "Content-Type: application/json" \
  -d '{
    "patientId": 1,
    "medecinId": 1,
    "dateRendezVous": "2026-03-15T10:00:00",
    "statut": "PLANIFIE"
  }'

# 4. Créer une consultation
curl -X POST http://localhost:8080/api/consultations \
  -H "Content-Type: application/json" \
  -d '{
    "rendezVousId": 1,
    "dateConsultation": "2026-03-15T10:30:00",
    "rapport": "Patient présente des symptômes de grippe. Prescription recommandée."
  }'

# 5. Consulter le dossier complet du patient
curl http://localhost:8080/api/dossiers/patient/1
```

## 📁 Structure des Packages

### Exemple : patient-service

```
patient-service/
└── src/main/java/ma/fsr/ms/patientservice/
    ├── PatientServiceApplication.java
    ├── model/
    │   └── Patient.java
    ├── repository/
    │   └── PatientRepository.java
    ├── service/
    │   └── PatientService.java
    ├── web/
    │   └── PatientController.java
    └── exception/
        ├── PatientNotFoundException.java
        ├── BusinessException.java
        └── GlobalExceptionHandler.java
```

## 🔄 Communication Inter-Services

### Appels REST

Le **rendezvous-service** vérifie l'existence du patient et du médecin :

```java
// Vérification via REST avant création du rendez-vous
Patient patient = patientClient.getPatientById(rendezVous.getPatientId());
Medecin medecin = medecinClient.getMedecinById(rendezVous.getMedecinId());
```

Le **dossier-service** agrège les données de plusieurs services :

```java
// Agrégation des données
Patient patient = patientClient.getPatientById(patientId);
List<RendezVous> rdvs = rendezVousClient.getByPatient(patientId);
List<Consultation> consults = consultationClient.getByPatient(patientId);
```

### Options de Client REST

**Option A : RestTemplate/RestClient**
```java
@Bean
public RestTemplate restTemplate() {
    return new RestTemplate();
}
```

**Option B : OpenFeign** (recommandé)
```java
@FeignClient(name = "patient-service", url = "${patient.service.url}")
public interface PatientFeignClient {
    @GetMapping("/internal/api/v1/patients/{id}")
    Patient getById(@PathVariable Long id);
}
```

## 🎯 Points Clés de l'Architecture Microservices

### ✅ Principes Respectés

1. **Base de données par service** : Chaque service a sa propre DB
2. **Autonomie** : Chaque service possède ses propres entités
3. **API Gateway** : Point d'entrée unique pour les clients
4. **Communication REST** : Protocole synchrone entre services
5. **Service Composite** : Dossier-service agrège les données

### ❌ Interdictions

- ❌ Pas de module `cabinet-repo` partagé
- ❌ Pas d'entités JPA partagées entre services
- ❌ Pas d'accès direct aux bases de données d'autres services

## 📊 Mapping API Gateway

| API Externe | API Interne | Service |
|-------------|-------------|---------|
| `/api/patients/**` | `/internal/api/v1/patients/**` | patient-service:8082 |
| `/api/medecins/**` | `/internal/api/v1/medecins/**` | medecin-service:8083 |
| `/api/rendezvous/**` | `/internal/api/v1/rendezvous/**` | rendezvous-service:8084 |
| `/api/consultations/**` | `/internal/api/v1/consultations/**` | consultation-service:8085 |
| `/api/dossiers/**` | `/internal/api/v1/dossiers/**` | dossier-service:8086 |

## 🐛 Dépannage

### Les services ne démarrent pas

- Vérifier que les ports ne sont pas déjà utilisés
- S'assurer que Java 21 est installé
- Vérifier les dépendances Maven

### Erreur "Service introuvable"

- Vérifier que tous les services sont démarrés
- Vérifier les URLs configurées dans `application.properties`
- Consulter les logs des services

### Erreur de validation

- Vérifier le format JSON des requêtes
- S'assurer que les règles métier sont respectées
- Consulter le message d'erreur retourné

## 📝 Livrables du TP

1. **Code source complet** (projet Maven multi-modules)
2. **Rapport PDF ou README** contenant :
   - Liste des endpoints REST
   - Captures d'écran des tests (GET, POST, etc.)
   - Explication de l'architecture
3. **Archive ZIP** ou **lien GitHub**

## 📚 Technologies Utilisées

- **Java 21**
- **Spring Boot 3.5.9**
- **Spring Cloud Gateway 2025.0.0**
- **Spring Data JPA**
- **H2 Database**
- **Lombok**
- **Maven**
- **OpenFeign** (optionnel)

## 👨‍💻 Auteur

Mustapha kassimi
Master IPS - Systèmes Distribués Basés sur les Microservices  
Faculté des Sciences de Rabat

---
