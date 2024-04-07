### Exercices - TP3

## Pipeline CI
1. Avant le pipeline, nous nous assurions que tout fonctionne correctement en testant manuellement le code quelques fois avant la remise. En générale, nous investissions environ 4h à tout vérifier de façon continue;
2. Après cette implémentation, le temps dedié aux tests a grandement diminué. Nous vérifions à chaque push que tout les tests automatisés s'Executent avec succès, mais l'opération nécessite quelques secondes seulement.
3. Le premier point positif qu cette impléentation nous a procuré est un gain de temps. Il nous a aussi permis de s'assurer que des erreurs humaines ou d'innatention soient automatiquement detectés donc la qualité du code est améliorée et finalement cela nous permet de réduire les conflits d'intégrations;
4. Puisque nous prenons pour acquis l'automatisation, cela peut mener à un excès de confiance en négligeant les tests manuels qui pourraient s'avérer pertinents dans certaines situations et donc ne plus détecter certaines erreurs.

## Tests
1. Cela dépend de chaque membre de l'équipe, mais de façon générale, nous avons investis environ 70% du temps à implémenter le code fonctionnel et 30% à confectionner les tests. Cette proportion n'a pas forcément évolué avec le temps, mais plutôt en fonction de la compléxité des méthodes.
2. Puisque nous avons décidé que chaque issue assigné à un membre implique également la confection de ses tests, la taille des issues et le temps qui lui a été accordé ont considérablement augmenté. Selon cette même logique, la vérification des PRs necessitait plus de temps et d'attention des membres.
3. Dans notre cas, avoir des tests unitaires nou a permis d'avoir plus confiance envers le code. En effet, puisque nous testions chaque méthode et sa fonctionnalité en courvrant tous les cas possibles, nous avions l'assurance d'avoir un résultat qui reflète ce qui est recherché.
4. Nous nous sommes aperçu que dans certains cas, les tests ne couvraient pas l'échelle des cas possibles, et donc certaines errreurs n'étaient pas détectées. Aussi, un manque d'uniformité entre les tests a pu être soulevé, problème qui a été résolu par la suite. Finalement, une autre piste d'amélioration serait de mieux structurer les tests.

### Planification

## Github Project
 <img src="Resources/Project.png"/>

## Milestone
 <img src="Resources/Milestone3.png"/>

## Issues

# Issue1
  <img src="Resources/issueTP31.png"/>

# Issue2
  <img src="Resources/issueTP32.png"/>

# Issue3
  <img src="Resources/issueTP33.png"/>

## Pull requests

# Pull request1
  <img src="Resources/PR1.png"/>
  <img src="Resources/PR2.png"/>

# Pull requests2
  <img src="Resources/PR3.png"/>
  <img src="Resources/PR4.png"/>

# Pull request3
  <img src="Resources/PR5.png"/>

## Arbre des commits
  <img src="Resources/arbre_commit.png"/>

# Stories
## Un owner peu obtenir une liste de toutes les réservations de l'un de ses restaurants à une date donnée dans une plage horaire donnée
## Critères de succès
- Seulement le propriétaire du restaurant peut obtenir une liste des réservation
- Si le propriétaire donne une heure de début et de fin à sa plage horaire et une date, il reçoit une liste de toutes les réservations entre ces deux heures pour ce restaurant à cette date
- Si le propriétaire donne seulement une heure de début pour la plage horaire et une date, il reçoit une liste de toutes les réservations entre l'heure de début spécifiée et la fermeture du restaurant à cette date
- Si le propriétaire donne seulement une heure de fin pour la plage horaire et une date, il reçoit une liste de toutes les réservations entre l'ouverture du restaurant et l'heure de fin spécifiée à cette date
- Si le propriétaire donne seulement une date, il reçoit une liste de toutes les réservation pour cette date
- Si le propriétaire ne donne pas de date ni de plage horaire, il reçoit une liste de toutes les réservations à son restaurant

## Un client peut modifier la date, l'heure ou le nb de personnes de sa réservation
## Critères de succès
- Seul le client peut modifier sa réservation
- Modifier le group size doit toujours respecter les disponibilités du restaurant
- Modifier la date et l'heure de la réservation doit toujours respecter les disponibilités du restaurant

## Le système envoie un courriel au client lorsqu'il reste 24h avant sa réservation
## Critère de succès
- Le client reçoit un courriel pour lui rappeler sa réservation
- Le courriel s'envoi 24 heures avant la réservation

## Un owner peut modifier le nom, la capacité et les heures d'ouverture de l'un de ses restaurants
## Critère de succès
- Seul le propriétaire peut modifier son Restaurant
- Modifier le nom, la capacités ou les heures d’ouverture modifie la réservation dans la liste de réservations du restaurant
