# NanoCLM - CLM Search Microservice

**_DEPRECATED_** - Merged with the CLMRepo

## Scope
- Contact (id / title / key-value set / attribute set / comments set) **searching**;
- Search result **filtering**
- **Predefined** / **stored** (named) searches and filters (tenant-public or private)
- User **search-related preferences**

## Prerequisites
- MongoDB (dev on 4.4.2) running locally, port 27017, no user/pass (see _resources/application-dev.properties_)

## Running
- build & run *or*
- docker-compose build & docker-compose up

## Usage
- http://127.0.0.1:8680/api/v1/swagger-ui.html
- http://127.0.0.1:8680/api/v1/v3/api-docs
