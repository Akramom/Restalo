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
MONGO_CLUSTER_URL='mongodb://admin:admin@localhost:27017/' MONGO_DATABASE='restalo' SENTRY_DSN='https://2394ba6d463c6084cdcf709801c55847@o4507098785972224.ingest.de.sentry.io/4507098849738832' mvn exec:java -D persistence='mongo'
```
 ##### persistence inMemory

```
mvn exec:java -D persistence='inmemory'
```
ou 
```
mvn exec:java

```
