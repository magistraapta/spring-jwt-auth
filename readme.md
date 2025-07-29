# Authentication JWT in Spring Boot
Authentication is one of the most important thing in backend system, to build robust backend application we need robust Authentication.

## Spring Security Basic
Spring Security is a powerful authentication and authorization framework for Java applications. Here are the key concepts:

### Core Components

1. **Authentication**
   - Process of verifying who a user is
   - Handled by `AuthenticationManager` interface
   - Uses `UserDetailsService` to load user data
   - Stores authenticated user in `SecurityContext`

2. **Authorization** 
   - Process of determining if a user has access to a resource
   - Based on granted authorities/roles
   - Configured using `.antMatchers()` or `@PreAuthorize`

3. **SecurityFilterChain**
   - Chain of filters that process the request
   - Includes authentication, authorization, exception handling etc.
   - Configured in `SecurityConfig` class



