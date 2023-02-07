# Nginx

### On Mac
```
$ brew install nginx

# 설정 정보 확인
$ brew info nginx
$ nginx -t

```

### Proxy 설정
```
# /nginx.conf 수정

http {
  ...
  
  server {
  
    # 8080 -> 80 변경
    listen       80; 
    server_name  localhost;
    
    ...
    
    location / {
      root   html;
      index  index.html index.htm;
    
      # proxy 설정
      proxy_pass http://localhost:8080;
      proxy_set_header X-Real-IP $remote_addr;
      proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
      proxy_set_header Host $http_host;
    }
    
    ...
    
  }
}
```

### Load Balancing
> document: https://docs.nginx.com/nginx/admin-guide/load-balancer/http-load-balancer/
```
http {
  upstream backend {
    server localhost:8080;
    server localhost:8081;
  }
  
  server {
    ...
    
    location / {
      ...
      
      proxy_pass http://backend;
      
      ...
      
    }
  }
}
```
