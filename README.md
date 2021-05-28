# eCommerce Application

This e-commerce project was developed to practice DevOps and Security skills. Proper authentication and authorization controls were added so users can only access their data, and that data can only be accessed in a secure way. The application was deployed using AWS EC2 instances, Jenkins and Tomcat server.

## API's

Some examples are as below:

To create a new user for example, you would send a POST request to:
http://localhost:8080/api/user/create with an example body like 

```
{
    "username": "test"
}
```


and this would return
```
{
    "id" 1,
    "username": "test"
}
```

To login in the app, for example, you would send a POST request to:
http://localhost:8080/login with an example body like 
```
POST /login 
{
    "username": "test",
    "password": "somepassword"
}
```

To create an order, you would send a POST request to:
http://localhost:8080/api/order/submit/{username} 



### Endpoints available:

```
"/api/cart/addToCart"
"/api/cart/removeFromCart"
"/api/item/{id}"
"/api/item/name/{name}"
"/api/order/submit/{username}"
"/api/order/history/{username}"
"/api/user/id/{id}"
"/api/user/{username}"
"/api/user/create"
```


