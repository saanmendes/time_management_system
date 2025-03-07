*DOCUMENTAÇÃO*

{

"project": "Time Management",

"architecture": "Hexagonal",

"layers": {

"application": {

"description": "Contains the business logic and use cases.",

"components": [
{

"name": "ReportUseCase",

"description": "Handles report-related business logic.",

"implementation": "ReportUseCaseImpl",

"dependencies": ["TaskRepository"]
}
]
},

"domain": {

"description": "Defines core models and business rules.",

"models": [
{

"name": "Report",

"fields": ["id", "description", "issueDate", "suggestion", "tasks"]
},

{
"name": "Task",

"fields": ["id", "email", "description", "initialDate", "endDate", "role", "priority", "category", "completed"]
}
]
},

"infrastructure": {

"description": "Handles data persistence and external dependencies.",

"components": [
{

"name": "ReportRepository",

"type": "JpaRepository",

"entity": "ReportEntity"
},

{
"name": "TaskRepository",

"type": "JpaRepository",

"entity": "TaskEntity",

"customQueries": [
{

"method": "findByInitialDateGreaterThanEqual",

"parameter": "LocalDateTime startDate"
}
]
}
]
},

"adapters":{

"description": "Provides input and output adapters for communication.",

"input": [
{

"name": "ReportMapper",

"description": "Converts between DTOs, entities, and domain models."
},

{
"name": "TaskMapper",

"description": "Converts between DTOs, entities, and domain models."
}
],

"output": [
{

"name": "RestTemplate",

"description": "Handles external HTTP requests."
}
}

documentação mais detalhada: https://docs.google.com/document/d/1V4GBNm24gtJya0BZ8piIyr4sgF4zl7ZvxYDwW5bopeE/edit?usp=sharing