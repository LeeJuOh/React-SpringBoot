# Grids & Circle 커피 주문 관리 시스템
---
## 프로젝트 목적
사용자는 이메일, 주소, 우편번호를 입력하여 상품을 주문할 수 있습니다.

주문은 전날 오후 2시부터 당일 2시까지의 결제된 주문을 배송 준비 상태로 변경합니다.
- 따라서 같은 email로 여러번 주문을 받더라도 하나로 합쳐서 배송을 보냅니다.

관리자는 관리자 페이지(Thyemlefa)를 통해 상품을 등록, 변경, 삭제할 수 있습니다.
<img src=https://user-images.githubusercontent.com/56071126/135367097-034e179e-01dc-4bde-a23c-b910c10798b3.png width=50% height=50%>

## 개발 환경
Java 16

Maven 3.8.2

SpringBoot 2.5.4

Mysql 8.0.25

### 프론트
Node 16

React 17

Bootstrap 5

## ERD
<img src=https://user-images.githubusercontent.com/56071126/135367702-df7ee1a2-d7de-4b77-8737-ec0fb18e5c26.png width=50% height=50%>

## API
상품 조회 api
```json
GET http://localhost:8080/api/v1/products
```
response example
```json
[
    {
        "productId": "421ed621-3efc-4256-8ce0-268882abfc44",
        "productName": "sample product2",
        "category": "COFFEE_BEAN_PACKAGE",
        "price": 11000,
        "description": "sample",
        "createdAt": "2021-09-30T09:53:08.293079",
        "updatedAt": "2021-09-30T09:53:15.822802"
    },
    {
        "productId": "8bfbd588-028e-4d0b-b13c-a64870f3cc84",
        "productName": "sample product",
        "category": "COFFEE_BEAN_PACKAGE",
        "price": 10000,
        "description": "sample",
        "createdAt": "2021-09-30T09:52:48.224812",
        "updatedAt": "2021-09-30T09:52:48.224842"
    }
]
```

주문 생성 api
```json
POST http://localhost:8080/api/v1/products
```
request example
```json
{
  "email": "example@email.com",
  "address": "경기도 구리시",
  "postcode": "12345",
  "orderItems": [
    {
      "productId": "421ed621-3efc-4256-8ce0-268882abfc44",
      "category": "COFFEE_BEAN_PACKAGE",
      "price": 15000,
      "quantity": 3
    }
  ]
}
```
