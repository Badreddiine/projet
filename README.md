# API REST de Gestion de Tâches Collaboratives

Cette application est une API REST sécurisée par JWT qui permet de gérer des utilisateurs, leurs rôles et des tâches qu'ils peuvent créer, assigner ou consulter selon leurs droits.

1. **User**
   - id, nom, email, mot de passe (hashé), rôle (USER ou ADMIN)

2. **Task**
   - id, titre, description, statut (À_FAIRE, EN_COURS, TERMINÉE), utilisateur assigné (ManyToOne)


### Authentification via JWT
- Connexion via email + mot de passe → token JWT
- Endpoints sécurisés selon le rôle

### Fonctions disponibles
- Créer un utilisateur avec rôle USER (non protégé)
- Connexion et génération du JWT (non protégé)
- Créer une tâche (USER ou ADMIN)
- Voir ses propres tâches (USER)
- Voir toutes les tâches (ADMIN uniquement)
- Mettre à jour le statut d'une tâche (uniquement par l'utilisateur assigné)

## Technologies utilisées

- Java + Spring Boot
- Spring Security avec JWT
- JPA/Hibernate + H2 Database
- Structure MVC (Controller, Service, Repository)
- Documentation Swagger
- Tests unitaires avec JUnit
- GitHub Actions pour CI
### Étapes d'installation

1. Cloner le dépôt

2. Compiler et exécuter l'application
   ```bash


3. L'application sera disponible à l'adresse http://localhost:8080

## Documentation API

La documentation Swagger de l'API est disponible à l'adresse http://localhost:8080/swagger-ui.html après le démarrage de l'application.

## Console H2

La console H2 est disponible à l'adresse http://localhost:8080/h2-console avec les paramètres suivants :
- JDBC URL: jdbc:h2:mem:taskdb
- Username: sa
- Password: password


### Authentification
- POST /api/auth/signup - Créer un nouvel utilisateur
- POST /api/auth/signin - Se connecter et obtenir un token JWT

### Gestion des tâches
- POST /api/tasks - Créer une nouvelle tâche
- GET /api/tasks - Obtenir toutes les tâches (ADMIN uniquement)
- GET /api/tasks/me - Obtenir les tâches de l'utilisateur connecté
- PUT /api/tasks/{id}/status - Mettre à jour le statut d'une tâche

## Tests

Pour exécuter les tests unitaires :
```bash
mvn test
```
