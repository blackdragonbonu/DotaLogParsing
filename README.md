# Bayes Java Dota Challlenge

This is the [task](TASK.md).

### Deviation from Task
* Abilities were changed to not contain the hero name. This was done to match the description in game.


## Description
The Solution employs two sets of operations. One for ingesting the log file 
and the second for retrieving the ingested data. Both the processes are 
explained in detail below

### Ingestion Of Combat Log

Combat log is ingested from the rest controller to 
[Combat Log Service](src/main/java/gg/bayes/challenge/service/DotaCombatLogService.java).
The service then does the following 
* Parse the log
* Store a new match in the Match Table
* Store the parsed log in the CombatLog table 

The parsing is handled by the [Combat Log Parser](src/main/java/gg/bayes/challenge/service/DotaCombatLogParser.java).
The parsed data is then stored in a bulk insert operation.

#### Parsing Logs

Parsing logs is handled in 3 steps.
* Validation
  * Prevalidation :  validation before fields are extracted
  * PostExtraction : validation after fields are extracted but before being deemed as parsed.
* Tokenization 
* Field Extraction

### Retrieving information about the ingested Log

Combat log information is retrieved from the db making using SQL queries.
For the scope of the problem additional optimizations around storing views of 
the required information was not explored. Currently the solution makes of
retrieval of data from the db and have grouping and aggregation be done as
part of the SQL query.
The log ingestion is done by [Combat Log Service](src/main/java/gg/bayes/challenge/service/DotaCombatLogService.java).

## Addtional Optimizations that can be considered

For each game we could have the data that is to be retrieved in separate tables
to optimize for reads. A table for hero damage, a table for hero kills, a table 
for spell casts. Item purchase can be done through the Combat log table

To optimize ingestion performance an alternate solution of returning an immediate
response status and then have the ingestion happen asynchronously. Giving the caller
a status endpoint to check the status of ingestion.