# 🛡️ Spring Security Authentication - Daily Progress (July 28, 2025)

This document summarizes the core Spring Security concepts learned and implemented today, including DAO authentication, BCrypt encoding, and a custom `UserDetailsService`.

---

## 🔐 1. DAO Authentication Provider

In Spring Security, `DaoAuthenticationProvider` is used to authenticate a user by retrieving user details from a custom `UserDetailsService`:

```java
DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
provider.setUserDetailsService(customUserDetailsService);
provider.setPasswordEncoder(passwordEncoder());
```

- It delegates user loading to your custom class (implements `UserDetailsService`).
- It compares the raw password (from login) with the encoded password in DB using the `PasswordEncoder`.

---

## 🔍 2. UserDetailsService Implementation

You define how to fetch user data (typically from a database) by implementing `UserDetailsService`:

```java
@Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = repo.findByUsername(username);
    return new CustomUserDetails(user);
}
```

- You **must return a `UserDetails` object** which Spring uses for authentication checks.
- Typically, you return a wrapper object (`CustomUserDetails`) around your `User` entity.

---

## 👤 3. Custom UserDetails Class

Spring requires a class that implements `UserDetails` to represent authenticated users:

```java
public class CustomUserDetails implements UserDetails {
    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // other overridden methods...
}
```

- This class bridges your entity (`User`) with what Spring Security expects.
- You can also return roles/authorities here.

---

## 🔑 4. BCrypt Password Encoding

Spring uses `BCryptPasswordEncoder` to securely store passwords.

```java
return new BCryptPasswordEncoder(10);
```

- `10` is the **strength**, meaning `2^10` rounds (1024).
- You can use `12`, `14`, etc., for stronger but slower encoding.
- BCrypt automatically handles salting internally.

Example usage:

```java
passwordEncoder.encode("myPassword");
```

---

## 🧠 5. How Spring Security uses all of this together

1. A login request hits the authentication filter.
2. The `AuthenticationManager` delegates to `DaoAuthenticationProvider`.
3. `DaoAuthenticationProvider` uses your `UserDetailsService` to load user data.
4. It checks password using your configured `BCryptPasswordEncoder`.
5. If successful, it stores the user in the SecurityContext.

---

## 🧪 6. Common Errors & Fixes

### ❌ `No property 'username' found for type 'user'`

**Fix:** Make sure your `User` entity has a field named exactly `username`.

```java
@Column(nullable = false, unique = true)
private String username;
```

---

### ❌ `Could not find UserDetailsService bean`

**Fix:** Annotate your `UserService` with `@Service` and ensure it's in a component-scanned package.

---

### ❌ `Not a managed type: class com.example.model.User`

**Fix:** Ensure your entity is annotated with `@Entity` and scanned by `@EnableJpaRepositories`.

---
