# NanoCLM
The tinyest CRM as a service

## Scope
- CLMRepo microservice handles **Tenants** (uniqueName / title) and **Contacts** (id / title / key-value set / attribute set / comments set); REST enabled
- Security is managed outside, user_id (unique string) is required
- CLMSearch microservice handles predefined queries and custom user-related queries and settings
- CLMLog microservice handles audit logs on Tenants and Contacts (action / old value / new value / responsible person / timestamp ); only create/update/delete operations are logged
- CLMGui is a simple search-based SPA web client

## Plot
- A Contact is a set of key-values, attributes (flags) and comments
- Contact is seen and manageable only-and-only by a owning Tenant
- Grouping, searching, filtering of Contacts is done exclusively via search-based interface

