# Projet - Restalo

Le meilleur logiciel de réservation en restauration!

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
