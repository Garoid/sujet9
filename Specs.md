Le but de cette extension est d'extraire le contenu d'un forum phpBB, et d'ajouter sur
l'interface un ensemble de boutons offrant les fonctionnalités suivantes:
- sauvegarder un message (sous un format XML, avec une partie méta-données et une
partie contenu)
- sauvegarder un fil de discussion
- sauvegarder les réponses avec une profondeur N à un message donné
- envoyer par mail comme fichier HTML: un message, un fil de discussion ou les
réponses avec une profondeur N à un message donné


Outils utilisés :

Langages utilisés :
Ajax pour l’appel et l’envoi de données à la servlet
Servlets java pour le traitement des données et le parsing en XML
XUL pour créer les boutons de l’interface




Création d’éléments ajoutés directement au DOM de la page sous forme d’icône à chaque discussion.
Sauvegarde du message au format XML : envoi du HTML avec AJAX à une servlet qui va parser le message en XML, l’enregistrer à un endroit spécifié par l’utilisateur préalablement.