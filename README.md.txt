Syst�me de Gestion Clinique � Guide FullStack (React + Spring Boot)
Login par d�faut : � Login :�admin�� Mot de passe : admin�
?? Vue d'ensemble
Le Syst�me de Gestion Clinique est une application web moderne d�velopp�e en Angular (Frontend) et Spring Boot 3 (Backend, JDK 21). Elle couvre la gestion des utilisateurs, candidats, rendezvous, dossiers, contact d�urgence, parcours, pi�ces justificatives, rendez-vous, messagerie et notifications. Trois r�les sont support�s : Administrateur, User et Candidat.
?? Fonctionnalit�s principales
?? Authentification et S�curit� � Connexion s�curis�e avec r�les (RBAC) � Routes prot�g�es (Frontend) + filtres de s�curit� (Backend) � Sessions via JWT (access + refresh) � Interface responsive (mobile, tablette, desktop)
?? Gestion MultiR�les 
� Administrateur : gestion syst�me, utilisateurs, candidats, rendezvous, dossiers, contact d�urgence, parcours, pi�ces justificatives, rendez-vous, messagerie et notifications, tableaux de bord 
� User : gestion syst�me, candidats, rendezvous, dossiers, contact d�urgence, parcours, pi�ces justificatives, rendez-vous, messagerie et notifications
� Candidat : Voir les notifications par mail, et consulter l��tat de son dossier

?? Pile technologique
Frontend � Agular 17, Taiwins CSS
Backend 
� Spring Boot 3, JDK 21, Maven 
� Spring Security, JWT, Validation, Data JPA 
� PostgreSQL, WebSockets (STOMP) pour temps r�el 
� G�n�ration de PDF (factures, prescriptions) 
� Fichiers de configuration en application-dev.yml (pas properties) et application.yml qui se trouvent dans src/main/ressources

?? Installation et D�marrage
Pr�requis 
� Node.js 20.19.2 
� JDK 21 (JAVA_HOME configur�) 
� Maven 3.9+ � PostgreSQL 14+ (base et utilisateur)

Installation � Frontend
Cloner le projet
git clone [URL_DU_REPO]
Installer et d�marrer
- npm install 
- npm start ou ng serve

Acc�s
http://localhost:4200 ou http://localhost:9000 �

Installation � Backend (Spring Boot 3, JDK 21)
Depuis la racine du backend
./mvnw 

Port de l'application
server.port=8080
�datasource:
� � type: com.zaxxer.hikari.HikariDataSource
� � url: jdbc:postgresql://localhost:5432/inscription
� � username: postgres
� � password: root

