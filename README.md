# personal_accounting
Server-side of money manager for monitoring personal expenses.

accounting_service: for creating and getting users + sending transaction details to reporting_service via Kafka. 
Transaction type can be only "expense" or "earning".
Also used to Authenticate user by email + password or using Google login 

reporting_service: saving transaction details to Elasticsearch + getting reports by user id and range of dates/category/trnsaction types.

accounting db: stores users and info about their general accounting balance
