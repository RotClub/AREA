# Documentation du Projet

## Contexte du Projet
Le projet permet aux utilisateurs de connecter leurs comptes à divers services (jeux vidéo, plateformes musicales, messagerie, etc.) et de créer des automatisations entre ces services. Grâce à ces automatisations, ils peuvent par exemple être notifiés lorsqu’un ami se connecte à un jeu ou lancer une playlist musicale lorsqu'une certaine action est effectuée dans un jeu.

### Objectif Principal
- **Espionner et ping ses amis** : Le site permet d'espionner les activités de vos amis sur différentes plateformes de jeux ou services, puis d'exécuter des actions en réponse à ces activités via des automatisations.
- **Automatisation personnalisée** : Créez des scénarios où des actions sont exécutées automatiquement en fonction de certains événements détectés sur les comptes connectés.

## Liste des Technologies

Le projet repose sur un ensemble de technologies pour assurer une interface fluide et performante, tant sur le web que sur mobile :
- **Frontend Web** : Développé en **Svelte** pour une expérience utilisateur interactive et performante.
- **Backend Web** : Utilise **JavaScript** pour gérer les API, la logique d'automatisation, et les services de notification.
- **Mobile** : L'application mobile est développée en **Kotlin** pour Android, avec un peu de **Swift** pour la version iOS.
- **Technologies supplémentaires** : 
  - **TypeScript**, **CSS**, et **HTML** sont utilisés pour harmoniser le code et la mise en forme du site web.
  - Utilisation de **Docker** pour la gestion des environnements de développement et de production.

## Liste des Fonctionnalités

### Connexion aux Services
Les utilisateurs peuvent connecter leurs comptes à divers services via le site ou l'application. Voici les plateformes compatibles :
- **Jeux vidéo** :
  - Steam
  - Epic Games
  - Battle.net
  - Game Pass
  - Gameforge
  - Ubisoft
  - Origin
  - Riot Games (League of Legends, Valorant, etc.)
  
- **Plateformes de Messagerie** :
  - Discord
  - TeamSpeak

- **Services de Streaming Musical** :
  - Spotify
  - Deezer
  - iTunes

### Fonctionnalités Automatisées
Les utilisateurs peuvent créer des scénarios d'automatisation qui réagissent en fonction des activités sur les services connectés. Voici quelques exemples de scénarios possibles :

#### Exemples d’Automatisation pour les Jeux Vidéo
1. **Steam / Epic Games / Battle.net** :
   - **Automatisation** : Envoie un message personnalisé à un ami lorsqu’il se connecte à un jeu spécifique.
   - **Exemple** : 
     - *Scénario* : Kevin se connecte à **Counter-Strike: Global Offensive** via Steam.
     - *Action* : Le système envoie un message sur Discord : "Kevin vient de lancer CS:GO, c'est l'heure de jouer !"
  
2. **Riot Games (League of Legends / Valorant)** :
   - **Automatisation** : Lance une playlist Spotify automatiquement lorsque vous êtes en file d'attente pour un match.
   - **Exemple** : 
     - *Scénario* : Vous entrez en queue sur **League of Legends**.
     - *Action* : Votre playlist "Gaming Mode" sur Spotify démarre automatiquement.

#### Exemples pour les Plateformes de Messagerie
1. **Discord / TeamSpeak** :
   - **Automatisation** : Envoie une notification à un groupe spécifique lorsque quelqu'un rejoint une session de jeu.
   - **Exemple** : 
     - *Scénario* : Kevin rejoint une partie sur **Valorant**.
     - *Action* : Un message est envoyé dans le salon Discord de votre équipe : "Kevin est en ligne sur Valorant, qui veut le rejoindre ?"

#### Exemples pour les Services de Streaming Musical
1. **Spotify / Deezer / iTunes** :
   - **Automatisation** : Jouer une playlist musicale spécifique lorsqu'une action est déclenchée dans un jeu.
   - **Exemple** : 
     - *Scénario* : Lorsque vous vous connectez à **Battle.net** pour jouer à **World of Warcraft**.
     - *Action* : Votre playlist "Adventure" sur Spotify se lance automatiquement pour accompagner votre exploration.

## Gestion de Compte Utilisateur
Les utilisateurs peuvent créer un compte pour :
- **Gérer leurs automatisations** : Créer, modifier et supprimer des règles d’automatisation.
- **Sauvegarder les connexions aux services** : Chaque utilisateur peut lier ses différents comptes (Steam, Spotify, Discord, etc.) au site pour faciliter les interactions et les automatisations.
- **Suivi des événements** : Historique des événements déclenchés par les amis et les actions exécutées par le système (par exemple, quand un ami se connecte ou quand une playlist a été lancée).

### Exemple de Workflow d'Inscription
1. L’utilisateur s’inscrit en fournissant son email et un mot de passe.
2. Une fois inscrit, il est redirigé vers une page où il peut connecter ses comptes à différents services (Steam, Discord, Spotify, etc.).
3. Après avoir lié ses comptes, l'utilisateur peut créer des règles d’automatisation (par exemple, envoyer un message Discord lorsqu’un ami se connecte à Steam).

---

## Conclusion
Le projet propose une plateforme d'automatisation innovante, permettant aux utilisateurs de créer des interactions personnalisées entre leurs jeux, services de messagerie, et plateformes musicales. Avec un compte utilisateur, ils peuvent centraliser leurs connexions et automatiser de nombreuses actions en fonction de leur activité ou de celle de leurs amis sur les services connectés.
