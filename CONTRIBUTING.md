<!-- omit in toc -->
# Contribuer à restalo

Tout d'abord, merci d'avoir pris le temps de contribuer ! ❤️

Tous types de contributions sont encouragés et appréciés. Consultez la [Table des matières](#Table-des-matières) pour différents moyens d'aider et des détails sur la façon dont ce projet les gère. Veuillez vous assurer de lire la section pertinente avant de faire votre contribution. Cela facilitera beaucoup les choses pour nous, les mainteneurs, et rendra l'expérience plus fluide pour tous les intervenants. La communauté attend avec impatience vos contributions. 🎉

> Et si vous aimez le projet, mais que vous n'avez tout simplement pas le temps de contribuer, c'est bien. Il existe d'autres moyens faciles de soutenir le projet et de montrer votre appréciation, ce que nous serions également très heureux de voir :
 > - Mettez une étoile sur le projet
 > - Tweetez à ce sujet
 > - Faites référence à ce projet dans le readme de votre projet
 > - Mentionnez le projet lors de rencontres locales et parlez-en à vos amis/collègues

<!-- omit in toc -->
## Table des matières

- [Code de conduite](#Code-de-conduite)
- [Principes fondamantaux](#principes-fondamantaux)
- [Meilleures pratiques de développement](#Meilleures-pratiques-de-développement)
- [J'ai une question](#Jai-une-question)
- [Je veux contribuer](#Je-veux-contribuer)
    - [Signalement de bogues](#Signalement-de-bogues)
    - [Suggerer des ameliorations](#suggerer-des-ameliorations)
    - [votre premiere contribution](#votre-premiere-contribution)
    - [Conventions de nommage des branches](#Conventions-de-nommage-des-branches)
- [Rejoindre l'équipe du projet](#Rejoindre-léquipe-du-projet)

## Code de conduite

Ce projet et tous ceux qui y participent sont régis par le
[code de conduite de restalo](https://github.com/GLO2003-H24-eq26/Restalo/blob/main/CODE_OF_CONDUCT.md).
En participant, vous êtes censé respecter ce code. Veuillez signaler tout comportement inacceptable à <wjkof1@ulaval.ca>.

## Principes fondamantaux

Les principes fondamentaux suivants guideront notre approche de la collaboration et du développement :

* **Respect**: Traiter tous les membres de l'équipe avec respect, considération et courtoisie.
* **Communication**: Communiquer ouvertement, honnêtement et efficacement.
* **Collaboration**: Travailler ensemble de manière constructive pour atteindre des objectifs communs.
* **Qualité**: S'engager à produire un travail de haute qualité.
* **Responsabilité**: Être responsable de ses actions et contributions.

## Meilleures pratiques de développement

* **Suivre les conventions de codage**: Suivre les conventions de codage établies pour le projet afin de garantir la cohérence et la lisibilité du code.
* **Écrire du code propre et documenté**: Écrire du code clair, concis et bien documenté pour faciliter sa compréhension et sa maintenance.
* **Tester le code rigoureusement**: Tester le code de manière rigoureuse pour identifier et corriger les bogues.
* **Utiliser le contrôle de version**: Utiliser un système de contrôle de version pour suivre les modifications du code et faciliter la collaboration.
* **Passer en revue le code**: Passer en revue le code des autres pour identifier les améliorations potentielles et garantir la qualité du code.


## J'ai une question

> Si vous souhaitez poser une question, nous supposons que vous avez lu la  [Documentation](https://github.com/GLO2003-H24-eq26/Restalo).

Avant de poser une question, il est préférable de rechercher des [Issues](https://github.com/GLO2003-H24-eq26/Restalo/issues) existants qui pourraient vous aider. Au cas où vous auriez trouvé un problème approprié et que vous avez encore besoin de clarification, vous pouvez écrire votre question dans cet problème. Il est également conseillé de rechercher d'abord des réponses sur Internet.

Si vous ressentez toujours le besoin de poser une question et que vous avez besoin de clarification, nous vous recommandons ce qui suit :

- Ouvrez une [Issue](https://github.com/GLO2003-H24-eq26/Restalo/issues/new).
- Fournissez autant de contexte que possible sur ce avec quoi vous rencontrez des problèmes.
- Fournissez les versions du projet et de la plateforme (maven, java, etc), selon ce qui semble pertinent.

Nous nous occuperons ensuite du problème dès que possible.


## Je veux contribuer

> ### Legal Notice <!-- omit in toc -->
>Lorsque vous contribuez à ce projet, vous devez accepter que vous avez rédigé 100 % du contenu, que vous avez les droits nécessaires sur le contenu et que le contenu que vous contribuez peut être fourni sous la licence du projet.

### Signalement de bogues

<!-- omit in toc -->
#### Avant de soumettre un rapport de bogue

Un bon rapport de bogue ne devrait pas obliger les autres à vous demander plus d'informations. Par conséquent, nous vous demandons d'enquêter attentivement, de collecter des informations et de décrire le problème en détail dans votre rapport. Veuillez suivre les étapes suivantes à l'avance pour nous aider à corriger tout bogue potentiel le plus rapidement possible.

- Assurez-vous d'utiliser la dernière version.
- Déterminez si votre bogue est vraiment un bogue et non une erreur de votre part, par exemple en utilisant des composants/versions d'environnement incompatibles  (Assurez-vous d'avoir lu la [documentation](https://github.com/GLO2003-H24-eq26). Si vous recherchez un support, vous voudrez peut-être consulter [cette section](#J-ai-une-question)).
- Pour voir si d'autres utilisateurs ont rencontré (et potentiellement déjà résolu) le même problème que vous, vérifiez s'il existe déjà un rapport de bogue pour votre bogue ou erreur dans les [Issues ouvert](https://github.com/GLO2003-H24-eq26/Restalo/issues).
- Assurez-vous également de rechercher sur Internet (y compris Stack Overflow) pour voir si des utilisateurs en dehors de la communauté GitHub ont discuté du problème
- Collectez des informations sur le bogue:
    - Trace de la pile (Traceback)
    - OS, Plateforme et Version (Windows, Linux, macOS, x86, ARM)
    - Version de l'interpréteur, du compilateur, du SDK, de l'environnement d'exécution, du gestionnaire de paquets, selon ce qui semble pertinent.
    - Éventuellement votre entrée et la sortie
    - Pouvez-vous reproduire le problème de manière fiable ? Et pouvez-vous aussi le reproduire avec des versions plus anciennes ?

<!-- omit in toc -->
#### Comment soumettre un bon rapport de bogue ?

> Vous ne devez jamais signaler de problèmes liés à la sécurité, de vulnérabilités ou de bogues contenant des informations sensibles dans le suivi des problèmes, ou ailleurs publiquement. Au lieu de cela, les bogues sensibles doivent être envoyés par courriel à <wjkof1@ulaval.ca>.

Nous utilisons les Issues GitHub pour suivre les bogues et les erreurs. Si vous rencontrez un problème avec le projet :

- Ouvrez un [Issue](https://github.com/GLO2003-H24-eq26/Restalo/issues/new). (Comme nous ne pouvons pas être sûrs à ce stade s'il s'agit d'un bogue ou non, nous vous demandons de ne pas parler d'un bogue pour l'instant et de ne pas étiqueter le problème.)
- Expliquez le comportement que vous attendiez et le comportement réel.
- Veuillez fournir autant de contexte que possible et décrire les **étapes de reproduction** que quelqu'un d'autre peut suivre pour recréer le problème par lui-même. Cela inclut généralement votre code. Pour de bons rapports de bogues, vous devriez isoler le problème et créer un cas de test réduit.
- Fournissez les informations que vous avez collectées dans la section précédente.

Une fois que c'est fait:

- L'équipe du projet étiquetera le problème en conséquence.
- Un membre de l'équipe tentera de reproduire le problème avec les étapes que vous avez fournies. S'il n'y a pas d'étapes de reproduction ou pas de moyen évident de reproduire le problème, l'équipe vous demandera ces étapes et marquera le problème comme `needs-repro`. Les bogues avec l'étiquette `needs-repro` ne seront pas traités tant qu'ils ne seront pas reproduits.
- Si l'équipe parvient à reproduire le problème, il sera marqué `needs-fix`, ainsi que éventuellement d'autres étiquettes (comme `critical`), et le problème sera laissé pour [être implémenté par quelqu'un](#votre-premiere-contribution).
- If the team is able to reproduce the issue, it will be marked `needs-fix`, as well as possibly other tags (such as `critical`), and the issue will be left to be [implemented by someone](#your-first-code-contribution).

<!-- You might want to create an issue template for bugs and errors that can be used as a guide and that defines the structure of the information to be included. If you do so, reference it here in the description. -->


### Suggerer des ameliorations

Cette section vous guide pour soumettre une suggestion d'amélioration pour Restalo, **y compris des fonctionnalités complètement nouvelles et des améliorations mineures de fonctionnalités existantes**. Suivre ces directives aidera les mainteneurs et la communauté à comprendre votre suggestion et à trouver des suggestions connexes.

<!-- omit in toc -->
#### Avant de soumettre une amélioration

- Assurez-vous d'utiliser la dernière version.
- Lisez attentivement la [documentation](https://github.com/GLO2003-H24-eq26/Restalo) et découvrez si la fonctionnalité est déjà couverte, peut-être par une configuration individuelle.
- Effectuez une [recherche](https://github.com/GLO2003-H24-eq26/Restalo/issues) pour voir si l'amélioration a déjà été suggérée. Si c'est le cas, ajoutez un commentaire à l'issue existante au lieu d'en ouvrir une nouvelle.
-Découvrez si votre idée correspond à la portée et aux objectifs du projet. C'est à vous de faire valoir les mérites de cette fonctionnalité auprès des développeurs du projet. Gardez à l'esprit que nous voulons des fonctionnalités qui seront utiles à la majorité de nos utilisateurs et pas seulement à un petit sous-ensemble. Si vous visez seulement une minorité d'utilisateurs, envisagez d'écrire une bibliothèque d'extension/plugin.


<!-- omit in toc -->
#### Comment soumettre une bonne suggestion d'amélioration ?

Les suggestions d'amélioration sont suivies comme des [issues GitHub](https://github.com/GLO2003-H24-eq26/Restalo/issues).

- Utilisez un **titre clair et descriptif** pour l'issue afin d'identifier la suggestion.
- Fournissez une **description pas à pas de l'amélioration suggérée** aussi détaillée que possible.
- **Décrivez le comportement actuel et expliquez quel comportement vous auriez plutôt attendu** et pourquoi. À ce stade, vous pouvez également indiquer quels sont les autres alternatives qui ne fonctionnent pas pour vous.
- **Expliquez pourquoi cette amélioration serait utile** pour la plupart des utilisateurs de Restalo. Vous voudrez peut-être également mentionner les autres projets qui l'ont mieux résolu et qui pourraient servir d'inspiration.

<!-- You might want to create an issue template for enhancement suggestions that can be used as a guide and that defines the structure of the information to be included. If you do so, reference it here in the description. -->

### votre premiere contribution

1) Lire le [README](https://github.com/GLO2003-H24-eq26/Restalo/blob/main/README.md)

il fournit une vue d'ensemble du projet, des instructions d'installation et d'autres informations importantes.
2) Trouver un problème à résoudre: 

Parcourez la liste des [problemes](https://github.com/GLO2003-H24-eq26/Restalo/issues) ouverts et choisissez un problème que vous souhaitez résoudre.
3) Créer une branche

Créez une nouvelle branche pour votre travail et nommez-la de manière descriptive.

4) Effectuer des modifications

Effectuez les modifications nécessaires pour résoudre le problème.
5) Tester votre code

Testez soigneusement votre code pour vous assurer qu'il fonctionne comme prévu.
6) Envoyer une pull request

Soumettez une pull request contenant vos modifications pour examen.
7) Répondre aux commentaires

Répondez aux commentaires des réviseurs et apportez les modifications nécessaires.
8) Fusionner votre pull request
* Processus de validation des pull requests:

Toutes les pull requests seront examinées par les mainteneurs du projet avant d'être fusionnées dans le dépôt principal. Ce processus d'examen vise à garantir que les contributions répondent aux normes de qualité du projet.

Les mainteneurs peuvent commenter et demander des modifications sur votre pull request. Veuillez répondre à ces commentaires en temps opportun et apportez les modifications nécessaires.

Une fois que votre pull request a été approuvée par les mainteneurs, elle sera fusionnée dans le dépôt principal.

Une fois que votre pull request a été approuvée, elle sera fusionnée dans la branche principale du projet.

### Conventions de nommage des branches

Pour une meilleure organisation et une meilleure lisibilité du code, veuillez suivre ces conventions de nommage des branches :

* **feature/<nom-de-la-fonctionnalité>** : pour les nouvelles fonctionnalités
* **bugfix/<numéro-du-ticket>** : pour les correctifs de bugs
* **chore/<description-courte>** : pour les modifications de maintenance

## Rejoindre l'équipe du projet
Vous pouvez nous rejoindre sur note [canal discord](https://discord.com/channels/1195790252820926564/1195902411546574848)!



<!-- omit in toc -->
## Attribution
Ce guide est basé sur **contributing-gen**. [faite le votre](https://github.com/bttger/contributing-gen)!
