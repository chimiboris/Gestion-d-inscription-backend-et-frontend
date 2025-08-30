Système de Gestion Clinique — Guide FullStack (React + Spring Boot)
Login par défaut : • Login : admin • Mot de passe : admin 
?? Vue d'ensemble
Le Système de Gestion Clinique est une application web moderne développée en Angular (Frontend) et Spring Boot 3 (Backend, JDK 21). Elle couvre la gestion des utilisateurs, candidats, rendezvous, dossiers, contact d’urgence, parcours, pièces justificatives, rendez-vous, messagerie et notifications. Trois rôles sont supportés : Administrateur, User et Candidat.
?? Fonctionnalités principales
?? Authentification et Sécurité • Connexion sécurisée avec rôles (RBAC) • Routes protégées (Frontend) + filtres de sécurité (Backend) • Sessions via JWT (access + refresh) • Interface responsive (mobile, tablette, desktop)
?? Gestion MultiRôles 
• Administrateur : gestion système, utilisateurs, candidats, rendezvous, dossiers, contact d’urgence, parcours, pièces justificatives, rendez-vous, messagerie et notifications, tableaux de bord 
• User : gestion système, candidats, rendezvous, dossiers, contact d’urgence, parcours, pièces justificatives, rendez-vous, messagerie et notifications
• Candidat : Voir les notifications par mail, et consulter l’état de son dossier

?? Pile technologique
Frontend • Agular 17, Taiwins CSS
Backend 
• Spring Boot 3, JDK 21, Maven 
• Spring Security, JWT, Validation, Data JPA 
• PostgreSQL, WebSockets (STOMP) pour temps réel 
• Génération de PDF (factures, prescriptions) 
• Fichiers de configuration en application-dev.yml (pas properties) et application.yml qui se trouvent dans src/main/ressources

?? Installation et Démarrage
Prérequis 
• Node.js 20.19.2 
• JDK 21 (JAVA_HOME configuré) 
• Maven 3.9+ • PostgreSQL 14+ (base et utilisateur)

Installation — Frontend
Cloner le projet
git clone [URL_DU_REPO]
Installer et démarrer
- npm install 
- npm start ou ng serve

Accès
http://localhost:4200 ou http://localhost:9000  

Installation — Backend (Spring Boot 3, JDK 21)
Depuis la racine du backend
./mvnw 

Port de l'application
server.port=8080
 datasource:
    type: com.zaxxer.hikari.HikariDataSource
    url: jdbc:postgresql://localhost:5432/inscription
    username: postgres
    password: root

