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
  "isActive": true // optional by default is true
  "parentId": "parent-category-id" // optional
}
```

#### 2. Get all categories
GET `/api/v1/categories` (public endpoint)
**Parameters:**
- size (optional): number of categories per page, default is 10
- page (optional): page number, default is 1
- sort (optional): sorting criteria, e.g., "name:asc" or "createdAt:desc"
- search (optional): search term to filter categories by name

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

### Product
#### 1. Create Product
#### Flow:
1. before creating a product, you need to create categories first.
2. create options and option-value 
3. create product
4. create product variations with options and option-values

POST `/api/v1/products`

**Request Body:**
```json
{
  "name": "Iphone 20 Pro Max",
  "description": "Tui bán Iphone 20 Pro Max", // optional
  "category": ["cate-id-1", "cate-id-2"],
  "slug": "iphone-20-pro-max",
  "status": "PUBLISHED" // optional by default is "DRAFT",
  "attributes": [
    {"key": "origin", "value": "USA"},
    {"key":  "brand", "value": "Apple"},
    {"key":  "release year", "value": "2024"},
    {"key":  "resolution", "value": "Full HD"}
  ]
}
```
#### 2. Create Product Variation
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
  "isDefault": true // optional by default is false
  
}
```

#### 3. Get product details
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