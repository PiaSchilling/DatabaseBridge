# 2.1 Konventionen 

[TOC]

------

## Dokumentation

### Architekturdokumentation

Terminologie und Gliederung nach dem deutschen [arc42-Template](https://www.arc42.de/overview/). Nicht alle Kapitel des Templates sind im Rahmen des Projektes relevant. Der Vollständigkeit halber werden trotzdem alle Kapitel aufgeführt und ggf. mit einem Hinweis versehen, dass dieses Kapitel zum Zeitpunkt des Projektes nicht relevant und dementsprechend nicht dokumentiert wurde. Die Möglichkeit diese Kapitel in Zukunft noch zu füllen bleibt somit erhalten.

## Git

### Commit message naming convention

Format: 
```
<type>(<issue-id>): <subject>
<BLANK LINE>
<body>
<BLANK LINE>
<footer>
```

Types:

- `feat`: (new feature for the user, not a new feature for build script)
- `fix`: (bug fix for the user, not a fix to a build script)
- `docs`: (changes to the documentation)
- `style`: (formatting, missing semi colons, etc; no production code change)
- `refactor`: (refactoring production code, eg. renaming a variable)
- `test`: (adding missing tests, refactoring tests; no production code change)
- `chore`: (updating grunt tasks etc; no production code change)

Issue-id: 

 `The id of the issue to which the commit belongs. If there is no issue, do: "feat(-): example commit message"  `

Subject:

`The subject contains succinct description of the change:`
`- use the imperative, present tense: “change” not “changed” nor “changes”`
`- don’t capitalize first letter`
`- no dot (.) at the end`

Body: 

`Just as in the subject, use the imperative, present tense: “change” not “changed” nor “changes”. The body should include the motivation for the change and contrast this with previous behavior.`

Footer: 

`Place to reference GitLab issues that this commit Closes`

Example : 

```
feat(#DW-1): add register button 

Implement button in the login screen to provide registration action to the user

closes #34
```

source: https://gist.github.com/joshbuchea/6f47e86d2510bce28f8e7f42ae84c716, https://ec.europa.eu/component-library/v1.15.0/eu/docs/conventions/git/ 

----

### Branch naming convention

Format:

```
<type>/DW-<issue-id>/<issue-title or branch description>
```

Types:

- Same as commit-message types

Example :

```
feat/DW-1/add-project-structure
```

### Git workflow

Der bei dem Projekt verwendete git Workflow is Git-flow. Siehe [hier](https://www.atlassian.com/de/git/tutorials/comparing-workflows/gitflow-workflow) für genauere Informationen, wie die konkrete Umsetzung dessen aussieht.



----



