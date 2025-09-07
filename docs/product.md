# Product Documentation

## Overview

## Data Model



## API
### Category
#### 1. Create category
POST `/api/v1/categories` 

**Request Body:**
```json
{
  "name": "Electronics",
  "description": "All kinds of electronic devices", // optional
  "slug": "electronics",
  "thumbnail": "http://example.com/image.jpg", // optional
  "parentId": "parent-category-id" // optional
}
```

#### 2. Get list of categories
GET `/api/v1/categories` (public endpoint)
**Response:**

```json
{
  "status": "success",
  "message": "Category created successfully",
  "data": {
    "currentPage": 1,
    "totalPages": 1,
    "totalItems": 1,
    "items": [
      {
        "id": "category-id-1",
        "name": "Electronics",
        "description": "All kinds of electronic devices",
        "slug": "electronics",
        "thumbnail": "http://example.com/image.jpg",
        "level": 1,
        "parentId": null,
        "children": []
      }
    ]
  }
}
```

#### 2. Get all category tree
GET `/api/v1/categories` (public endpoint)

**Response:**

```json
{
  "status": "success",
  "message": "Categories retrieved successfully",
  "data": {
    "currentPage": 1,
    "totalPages": 5,
    "totalItems": 50,
    "items": [
      {
        "id": "category-id-1",
        "name": "Phones",
        "thumbnail": "http://example.com/phone.jpg",
        "slug": "cate-phones",
        "level": 1,
        "parentId": null,
        "children": [
          {
            "id": "category-id-2",
            "name": "Smartphones",
            "thumbnail": "http://example.com/phone.jpg",
            "slug": "smartphones",
            "level": 2,
            "parentId": "category-id-1",
            "children": null
          }
        ]
      }//... more categories
    ]
  }
}
```

#### 3. Disable/Enable category
PATCH `/api/v1/categories/{categoryId}/status?active=true|false`
**Request Parameters:**
- active: boolean (true to enable, false to disable)

#### 4. Delete category
DELETE `/api/v1/categories/{categoryId}`
nó liên quan đến Product và các category con nên quất sau


### Product
#### Flow:
1. before creating a product, you need to create categories first.
2. create options and option-value 
3. create product
4. create product variations with options and option-values

#### 1. Create Options and Option Values
POST `/api/v1/products/{id}/options`
**Request Body:**
```json
{
  "name": "Color",
  "values": ["Red", "Blue", "Green"]
}
```
**Response:**
```json
{
  "status": "success",
  "message": "Option created successfully",
  "data": {
    "id": "option-uuid-1",
    "name": "Color",
    "values": [
      {"id":  "val-uuid-red", "value": "Red"},
      {"id":  "val-uuid-blue", "value": "Blue"},
      {"id":  "val-uuid-green", "value": "Green"}
    ]
  }
}
```



#### 3. Create Product

POST `/api/v1/products`

**Request Body:**
```json
{
  "name": "Iphone 20 Pro Max",
  "description": "Tui bán Iphone 20 Pro Max", // optional
  "categoryId": "cate-id-1",
  "slug": "iphone-20-pro-max",
  "status": "PUBLISHED" // optional by default is "DRAFT",,
  "thumbnail": "http://example.com/iphone20promax.jpg",
  "attributes": [
    {"key": "origin", "value": "USA"},
    {"key":  "brand", "value": "Apple"},
    {"key":  "release year", "value": "2024"},
    {"key":  "resolution", "value": "Full HD"}
  ]
}
```
#### 4. Create Product Variation
POST `/api/v1/products/{productId}/variations`

**Request Body:**
```json
{
  "sku": "IP20PM256",
  "name": "Iphone 20 Pro Max 256GB",
  "description": "Tui bán Iphone 20 Pro Max 256GB", // optional
  "status": "PUBLISHED", // optional by default is "DRAFT"
  "price": 30000000, // in VND
  "currency": "VND", // optional by default is "VND"
  "stock": 20 // optional by default is 0,
  "image_url": "http://example.com/iphone20promax256.jpg",
  "isDefault": true // optional by default is false
  "optionValueIds": ["val-uuid-promax", "val-uuid-256gb"] // array of option value ids
}
```

#### 5. Get product details
GET `/api/v1/products/{productId}` (public endpoint)
**Response:**
```json
{
  "status": "success",
  "message": "Product retrieved successfully",
  "data": {
    "id": "product-id-1",
    "name": "Iphone 20 Pro Max",
    "description": "Tui bán Iphone 20 Pro Max",
    "slug": "iphone-20-pro-max",
    "attributes": [
      {"key": "origin", "value": "USA"},
      {"key":  "brand", "value": "Apple"},
      {"key":  "release year", "value": "2024"},
      {"key":  "resolution", "value": "Full HD"}
    ],
    "categories": [
      {
        "id": "cate-id-1",
        "name": "Phones",
        "thumbnail": "http://example.com/phone.jpg",
        "slug": "cate-phones",
        "level": 1,
        "parentId": null
      },
      {
        "id": "cate-id-2",
        "name": "Smartphones",
        "thumbnail": "http://example.com/phone.jpg",
        "slug": "smartphones",
        "level": 2,
        "parentId": "cate-id-1"
      }
    ],
    "variations": [
      {
        "id": "variation-id-1",
        "sku": "IP20PM256",
        "name": "Iphone 20 Pro Max 256GB",
        "description": "Tui bán Iphone 20 Pro Max 256GB",
        "status": "PUBLISHED",
        "price": 30000000,
        "currency": "VND",
        "stock": 20,
        "isDefault": true,
        "optionValueIds": ["val-uuid-promax", "val-uuid-256gb"]
      },
      {
        "id": "variation-id-2",
        "sku": "IP20PM256",
        "name": "Iphone 20 Pro 512GB",
        "description": "Tui bán Iphone 20 Pro 512GB",
        "status": "PUBLISHED",
        "price": 30000000,
        "currency": "VND",
        "stock": 20,
        "isDefault": true,
        "optionValueIds": ["val-uuid-pro", "val-uuid-512gb"]
      }
      //... more variations
    ],
    createdAt: "...",
    updatedAt: "...",
  }
}
```

#### 4. Get all products
GET `/api/v1/products` (public endpoint)
**Parameters:**
- size (optional): number of products per page, default is 10
- page (optional): page number, default is 1
- sort (optional): sorting criteria, e.g., "name:asc" or "createdAt:desc"
- search (optional): search term to filter products by name

**Response:**
```json
{
  "status": "success",
  "message": "Products retrieved successfully",
  "data": {
    "currentPage": 1,
    "totalPages": 5,
    "totalItems": 50,
    "items": [
      {
        "id": "product-id-1",
        "name": "Iphone 20 Pro Max",
        "description": "Tui bán Iphone 20 Pro Max",
        "slug": "iphone-20-pro-max",
        "price": 30000000 // price of default variation
      }
      //... more products
    ]
  }
}
```

