<p align="center">
<img src="https://github.com/GLO2003-H24-eq26/Restalo/actions/workflows/cd.yml/badge.svg" alt="Github Action"> 
<img src="https://github.com/GLO2003-H24-eq26/Restalo/actions/workflows/maven.yml/badge.svg alt="Github Action"> 

</p>
# Projet - Restalo

Le meilleur logiciel de réservation en restauration!

## Commencer
- faite un fork de ce repo (cliquez sur le bouton fork en haut à droite de cette page)
- Clonez votre fork jusqu’à votre machine locale

```markdown
git clone https://github.com/GLO2003-H24-eq26/Restalo.git
```
- Créer une branche

```markdown
git checkout -b branch-name
```

- Apportez vos modifications 
- faites un commit et un push

```markdown
git add .
git commit -m 'Commit message'
git push origin branch-name
```

- Créer une nouvelle pull request à partir de votre fork 
- Attendez l’examen de votre demande de tirage et l’approbation de la fusion !


## Requis

- Java 21
- Maven 3.x

## Commandes

### Formatage du code
```
mvn spotless:apply
```
### Compilation
```
mvn compile
```
### Exécution

##### demarrage d'un container pour la bse de donnée
```
docker compose up -d
```

 ##### persistence BD

```
MONGO_CLUSTER_URL='mongodb://user:password@localhost:27017/' MONGO_DATABASE='restalo' mvn exec:java -D persistence='mongo'

```
 ##### persistence inMemory

```
mvn exec:java -D persistence='inmemory'
```
ou 
```
mvn exec:java

```

##### execution avec une  image docker

```
docker run -p 8082:8082 -e PORT='8082' -e MONGO_CLUSTER_URL='mongodb://user:password@localhost:27017/' -e MONGO_DATABASE='restalo' image_id mvn exec:java

```
### Créer un rapport de bogue

Si vous voyez un message d’erreur ou si vous rencontrez un problème, veuillez [Créer un rapport de bogue](https://github.com/GLO2003-H24-eq26/Restalo/issues/new). Cet effort est tres apprécié.

### Envoyer une demande de fonctionnalité

Si vous avez une idée, ou s’il vous manque une fonctionnalité qui rendrait le développement plus facile et plus robuste, veuillez [soumettre une demande de fonctionnalité.](https://github.com/GLO2003-H24-eq26/Restalo/issues/new).

Si une demande de fonctionnalité similaire existe déjà, n’oubliez pas de laisser un « +1 ». Si vous ajoutez des informations supplémentaires telles que vos pensées et votre vision de la fonctionnalité, vos commentaires seront accueillis chaleureusement :)
## Contribution

Restalo est un projet open-source. Nous nous engageons à un processus de développement totalement transparent et apprécions hautement toutes les contributions. Que vous nous aidiez à corriger des bugs, que vous proposiez de nouvelles fonctionnalités, que vous amélioriez notre documentation ou que vous fassiez passer le mot, nous serions ravis de vous compter parmi les membres de la communauté
Veuillez vous référer à nos [directive de contribution](./CONTRIBUTING.md) et a notre  [code de de conduite](./CODE_OF_CONDUCT.md).

## License
[MIT licence](https://github.com/GLO2003-H24-eq26/Restalo/blob/main/LICENSE.txt)


