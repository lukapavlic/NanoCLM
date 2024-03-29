# NanoCLM - CLM Repository/Backend

## Scope
- App stores **Tenants** (uniqueName / title) and **Contacts** (id / title / key-value set / attribute set / comments set) in DB;
  **CRUD** operations are available on Tenants and Contacts via REST endpoint,
- It enables registering **Tenants** and adding **Contacts** via REST endpoint,
- Tenants' Contacts are stored in separate collections (collection _tenantUniqueName_)
- Every Tenant can access/edit only its own Contacts
- Deletion is not enabled: when deleting a Contact, it is moved to a "trash" collection (_tenantUniqueName_deleted_)
- All events are published to a ActiveMQ topic
- Contact **searching**;
- Search result **filtering**
- **Predefined** / **stored** (named) searches and filters (tenant-public or private)
- User **search-related preferences**

## Prerequisites
- MongoDB (dev on 4.4.2 / 7.0.2 ARM on Mac) running locally, port 27017, no user/pass (see _resources/application-dev.properties_)
- Apache ActiveMQ (dev on 5.17.1 / 5.18.3 on Mac) running locally, port 61616, no user/pass (see _resources/application-dev.properties_)

When ObjectMessage in use (deprecated at current implementation), make sure to run 
Apache ActiveMQ with param (see https://activemq.apache.org/objectmessage.html for details):
```
-Dorg.apache.activemq.SERIALIZABLE_PACKAGES=si.um.feri.nanoclm.backend.events,java.time
```

## Running
- build & run *or*
- docker-compose build & docker-compose up

## Usage
- http://127.0.0.1:8580/api/v1/swagger-ui.html
- http://127.0.0.1:8580/api/v1/v3/api-docs


- http://127.0.0.1:8580/api/v1/tenants
- http://127.0.0.1:8580/api/v1/contacts
  
- http://127.0.0.1:8161


- Initial test data: tenant "II", user "testuser"
