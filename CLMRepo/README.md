# NanoCLM - CLM Repository Microservice

## Scope
- App stores **Tenants** (uniqueName / title) and **Contacts** (id / title / key-value set / attribute set / comments set) in DB;
  **CRUD** operations are available on Tenants and Contacts via REST endpoint,
- It enables registering **Tenants** and adding **Contacts** via REST endpoint,
- Tenants' Contacts are stored in separate collections (collection _tenantUniqueName_)
- Every Tenant can access/edit only its own Contacts
- Deletion is not enabled: when deleting a Contact, it is moved to a "trash" collection (_tenantUniqueName_deleted_)

## Prerequisites
- MongoDB running locally, port 27017, no user/pass (see _resources/application-dev.properties_)

## Running
- build & run *or*
- docker-compose build & docker-compose up

## Usage
- http://127.0.0.1:8580/api/v1/swagger-ui.html
- http://127.0.0.1:8580/api/v1/v3/api-docs


- http://127.0.0.1:8280/api/v1/tenants
- http://127.0.0.1:8280/api/v1/contacts

