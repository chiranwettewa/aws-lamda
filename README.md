# Podiweda Backend - Spring Boot with AWS Cognito

Secure REST API with AWS Cognito JWT authentication and role-based access control.

## Features

- ✅ JWT token validation using AWS Cognito
- ✅ Spring Security OAuth2 Resource Server
- ✅ Role-based access control (RBAC)
- ✅ CORS configuration for React frontend
- ✅ Stateless authentication
- ✅ Production-ready security

## Prerequisites

- Java 17+
- Maven 3.6+
- AWS Cognito User Pool configured

## Configuration

### Environment Variables

Create `.env` file or set environment variables:

```bash
AWS_REGION=us-east-1
COGNITO_USER_POOL_ID=us-east-1_XXXXXXXXX
COGNITO_CLIENT_ID=7xxxxxxxxxxxxxxxxxxxxxx
FRONTEND_URL=http://localhost:3000
```

### application.properties

Located at `src/main/resources/application.properties`:

```properties
spring.security.oauth2.resourceserver.jwt.issuer-uri=https://cognito-idp.${aws.cognito.region}.amazonaws.com/${aws.cognito.userPoolId}
spring.security.oauth2.resourceserver.jwt.jwk-set-uri=https://cognito-idp.${aws.cognito.region}.amazonaws.com/${aws.cognito.userPoolId}/.well-known/jwks.json
```

## Build and Run

### Development

```bash
# Build
mvn clean install

# Run
mvn spring-boot:run

# Run with environment variables
AWS_REGION=us-east-1 \
COGNITO_USER_POOL_ID=us-east-1_XXXXXXXXX \
COGNITO_CLIENT_ID=7xxxxxxxxxxxxxxxxxxxxxx \
mvn spring-boot:run
```

### Production

```bash
# Build JAR
mvn clean package

# Run JAR
java -jar target/aws-lamda-1.0-SNAPSHOT.jar
```

## API Endpoints

### Public Endpoints

```bash
GET /health          # Health check
GET /ping            # Ping endpoint
```

### Protected Endpoints (Requires JWT)

```bash
GET /api/profile     # Get authenticated user profile
POST /api/data       # Create data (authenticated users)
```

### Role-Based Endpoints

```bash
GET /api/user/data   # Requires User or Admin group
GET /api/admin/data  # Requires Admin group only
```

## Security Configuration

### JWT Validation

The application validates JWT tokens from AWS Cognito:

1. Extracts token from `Authorization: Bearer <token>` header
2. Validates signature using JWKS from Cognito
3. Verifies issuer matches User Pool
4. Checks token expiration

### Role-Based Access Control

Uses `@PreAuthorize` annotation:

```java
@PreAuthorize("hasAuthority('SCOPE_Admin')")
public Map<String, String> getAdminData() {
    // Admin only logic
}
```

Cognito groups are mapped to Spring Security authorities as `SCOPE_<GroupName>`.

## Testing

### Test with cURL

```bash
# Get JWT token from frontend after login
TOKEN="eyJraWQiOiJ..."

# Test profile endpoint
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/profile

# Test admin endpoint
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/admin/data
```

### Test with Postman

1. Set request type: GET
2. URL: `http://localhost:8080/api/profile`
3. Headers:
   - Key: `Authorization`
   - Value: `Bearer <your-jwt-token>`
4. Send request

## Adding New Protected Endpoints

### Example: Create New Endpoint

```java
@RestController
@RequestMapping("/api")
public class MyController {

    @GetMapping("/my-endpoint")
    public Map<String, String> myEndpoint(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getClaim("cognito:username");
        String email = jwt.getClaim("email");
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Success");
        response.put("user", username);
        return response;
    }

    @PostMapping("/admin/create")
    @PreAuthorize("hasAuthority('SCOPE_Admin')")
    public Map<String, String> adminCreate(@RequestBody Map<String, Object> data) {
        // Admin only logic
        return Map.of("status", "created");
    }
}
```

## CORS Configuration

CORS is configured in `SecurityConfig.java`:

```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    configuration.setAllowCredentials(true);
    return source;
}
```

## Deployment

### Docker

```dockerfile
FROM openjdk:17-slim
WORKDIR /app
COPY target/aws-lamda-1.0-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
```

### AWS Elastic Beanstalk

```bash
# Create application
eb init -p java-17 podiweda-backend

# Deploy
eb create podiweda-backend-env
eb deploy
```

### AWS ECS

1. Build Docker image
2. Push to ECR
3. Create ECS task definition
4. Deploy to ECS cluster

## Troubleshooting

### JWT Validation Fails

**Check:**
- User Pool ID is correct
- Token hasn't expired
- JWKS endpoint is accessible
- Issuer URI matches User Pool

### CORS Errors

**Check:**
- Frontend URL in `FRONTEND_URL` environment variable
- CORS configuration in `SecurityConfig.java`
- Preflight OPTIONS requests are allowed

### 403 Forbidden

**Check:**
- User is in correct Cognito group
- JWT contains `cognito:groups` claim
- `@PreAuthorize` annotation uses correct authority

## Dependencies

Key dependencies in `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>
```

## License

MIT
