## Backend for Frontends

### Query

```postgresql
query ($page: Int!, $size: Int!) {
  publicUsers(page: $page, size: $size) {
    list {
      id
      login
      avatar
    }
    page {
      page
      size
      totalPages
      totalCounts
      hasNext
    }
  }
}
-----------------------------------------
{
    "page": 0,
    "size": 20
}
```

### Mutation

```postgresql
mutation ($user: SubmittedUser!) {
  updateUser(user: $user) {
    id
    login
    avatar
  }
}
-----------------------------------------
{
  "user": {
    "id": "1",
    "login": "xxx"
  }
}
```

## LoadBalancer Debug

```java
RetryableFeignBlockingLoadBalancerClient.java:126
```