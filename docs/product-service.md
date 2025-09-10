# Product Service API Documentation

## Overview

This document provides detailed information about the Product Service API, which is responsible for managing products, categories, and their variations.

## Data Model

The core of the service revolves around a few key entities:

*   **Product:** The main entity representing a product. It contains general information like name, description, slug, and attributes.
*   **Category:** Used to organize products. Categories can be nested to create a hierarchical structure.
*   **Product Variant:** Represents a specific version of a product, such as a different color or size. Each variant has its own SKU, price, and stock level.
*   **Product Option:** Defines a configurable aspect of a product, like "color" or "storage".
*   **Option Value:** Represents a specific choice for a product option, such as "Red" for the "color" option or "256GB" for the "storage" option.

---

## API Endpoints

### Category API

#### 1. Create Category

Creates a new category.

*   **Endpoint:** `POST /api/v1/categories`
*   **Request Body:**

    ```json
    {
      "name": "Electronics",
      "description": "All kinds of electronic devices",
      "slug": "electronics",
      "thumbnail": "http://example.com/image.jpg",
      "parentId": "parent-category-id"
    }
    ```

*   **Response (201 CREATED):**

    ```json
    {
      "status": "created",
      "message": "Category created successfully",
      "data": {
        "id": "category-id-1",
        "name": "Electronics",
        "description": "All kinds of electronic devices",
        "slug": "electronics",
        "thumbnail": "http://example.com/image.jpg",
        "level": 1,
        "parentId": null,
        "children": []
      }
    }
    ```

#### 2. Get All Categories

Retrieves a paginated and hierarchical list of all categories.

*   **Endpoint:** `GET /api/v1/categories`
*   **Response (200 OK):**

    ```json
    {
      "status": "ok",
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
          }
        ]
      }
    }
    ```

#### 3. Update Category

Updates the details of an existing category.

*   **Endpoint:** `PATCH /api/v1/categories/{id}`
*   **Request Body:**

    ```json
    {
      "name": "Electronics Updated",
      "description": "All kinds of updated electronic devices",
      "slug": "electronics-updated",
      "thumbnail": "http://example.com/updated-image.jpg",
      "parentId": "new-parent-category-id"
    }
    ```

*   **Response (200 OK):**

    ```json
    {
        "status": "ok",
        "message": "Category updated successfully",
        "data": {
            "id": "category-id-1",
            "name": "Electronics Updated",
            "description": "All kinds of updated electronic devices",
            "slug": "electronics-updated",
            "thumbnail": "http://example.com/updated-image.jpg",
            "level": 1,
            "parentId": "new-parent-category-id",
            "children": []
        }
    }
    ```

#### 4. Update Category Status

Enables or disables a category and all of its subcategories.

*   **Endpoint:** `PATCH /api/v1/categories/{id}/status`
*   **Query Parameters:**
    *   `active` (boolean, required): `true` to enable, `false` to disable.
*   **Response (200 OK):**

    ```json
    {
      "status": "ok",
      "message": "Category status updated successfully",
      "data": null
    }
    ```

### Product API

#### 1. Create Product

Creates a new product.

*   **Endpoint:** `POST /api/v1/products`
*   **Request Body:**

    ```json
    {
      "name": "Iphone 20",
      "description": "The latest iPhone model.",
      "categoryId": "cate-id-1",
      "slug": "iphone-20-pro-max",
      "status": "PUBLISHED",
      "thumbnail": "http://example.com/iphone20promax.jpg",
      "attributes": [
        {"key": "origin", "value": "USA"},
        {"key": "brand", "value": "Apple"},
        {"key": "release year", "value": "2024"},
        {"key": "resolution", "value": "Full HD"}
      ]
    }
    ```

*   **Response (200 OK):**

    ```json
    {
      "status": "ok",
      "message": "Product created successfully",
      "data": {
        "id": "product-id-1",
        "name": "Iphone 20",
        "slug": "iphone-20-pro-max",
        "description": "The latest iPhone model.",
        "thumbnail": "http://example.com/iphone20promax.jpg",
        "status": "PUBLISHED",
        "categories": [
             {
                "id": "cate-id-1",
                "name": "Phones",
                "thumbnail": "http://example.com/phone.jpg",
                "slug": "cate-phones",
                "level": 1,
                "parentId": null
             }
        ],
        "attributes": [
            {"key": "origin", "value": "USA"},
            {"key": "brand", "value": "Apple"},
            {"key": "release year", "value": "2024"},
            {"key": "resolution", "value": "Full HD"}
        ]
      }
    }
    ```

#### 2. Get Product Detail

Retrieves the full details of a single product, including all its variants and options.

*   **Endpoint:** `GET /api/v1/products/{id}`
*   **Response (200 OK):**

    ```json
    {
      "status": "ok",
      "message": "Product retrieved successfully",
      "data": {
        "id": "product-id-1",
        "name": "Iphone 20",
        "description": "The latest iPhone model.",
        "slug": "iphone-20-pro-max",
        "attributes": [
          {"key": "origin", "value": "USA"},
          {"key": "brand", "value": "Apple"}
        ],
        "categories": [
          {
            "id": "cate-id-1",
            "name": "Phones",
            "slug": "cate-phones",
            "level": 1,
            "parentId": null
          }
        ],
        "options": [
          {
            "name": "version",
            "values": [
              {"id": "val-uuid-promax", "value": "Pro Max"},
              {"id": "val-uuid-pro", "value": "Pro"}
            ]
          },
          {
            "name": "storage",
            "values": [
              {"id": "val-uuid-256gb", "value": "256GB"},
              {"id": "val-uuid-512gb", "value": "512GB"}
            ]
          }
        ],
        "variants": [
          {
            "id": "variation-id-1",
            "sku": "IP20PM256",
            "name": "Iphone 20 Pro Max 256GB",
            "status": "PUBLISHED",
            "price": 30000000,
            "currency": "VND",
            "stock": 20,
            "isDefault": true,
            "optionValueIds": ["val-uuid-promax", "val-uuid-256gb"]
          }
        ]
      }
    }
    ```

#### 3. Get List of Products

Retrieves a paginated list of products.

*   **Endpoint:** `GET /api/v1/products`
*   **Query Parameters:**
    *   `page` (integer, optional, default: 1): The page number to retrieve.
    *   `size` (integer, optional, default: 20): The number of products per page.
    *   `sort` (string, optional, default: "id:desc"): The sorting criteria (e.g., "name:asc").
    *   `search` (string, optional): A search term to filter products by.
*   **Response (200 OK):**

    ```json
    {
      "status": "ok",
      "message": "Products retrieved successfully",
      "data": {
        "currentPage": 1,
        "totalPages": 5,
        "totalItems": 50,
        "items": [
          {
            "id": "product-id-1",
            "name": "Iphone 20",
            "slug": "iphone-20-pro-max",
            "defaultVariant": {
              "id": "variation-id-1",
              "sku": "IP20PM256",
              "name": "Iphone 20 Pro Max 256GB",
              "price": 30000000,
              "currency": "VND",
              "stock": 20
            }
          }
        ]
      }
    }
    ```

#### 4. Get Products by Category

Retrieves a paginated list of products belonging to a specific category.

*   **Endpoint:** `GET /api/v1/products/category/{categorySlug}`
*   **Query Parameters:**
    *   `page` (integer, optional, default: 1): The page number to retrieve.
    *   `size` (integer, optional, default: 20): The number of products per page.
    *   `sort` (string, optional, default: "id:desc"): The sorting criteria.
    *   `search` (string, optional): A search term to filter products.
*   **Response (200 OK):** (Same structure as "Get List of Products")

#### 5. Update Product

Updates the details of an existing product.

*   **Endpoint:** `PATCH /api/v1/products/{id}`
*   **Request Body:**

    ```json
    {
        "name": "iPhone 20 Pro",
        "description": "An updated description for the latest iPhone.",
        "slug": "iphone-20-pro-updated",
        "status": "DRAFT",
        "thumbnail": "http://example.com/new-iphone-thumbnail.jpg",
        "categoryId": "new-cate-id-2",
        "attributes": [
            {"key": "color", "value": "Titanium"}
        ]
    }
    ```

*   **Response (200 OK):** (Same structure as the response for "Create Product")

#### 6. Create Product Variant

Adds a new variant to an existing product.

*   **Endpoint:** `POST /api/v1/products/{id}/variants`
*   **Request Body:**

    ```json
    {
      "sku": "IP20PM256",
      "name": "Iphone 20 Pro Max 256GB",
      "price": 30000000,
      "options": [
        {"name": "version", "value": "Pro Max"},
        {"name": "storage", "value": "256GB"}
      ],
      "imageUrl": "http://example.com/iphone20promax256.jpg",
      "weight": 300.5,
      "dimensions": "160.8 x 78.1 x 7.4 mm",
      "unit": "piece",
      "status": "PUBLISHED",
      "currency": "VND",
      "stock": 100,
      "isDefault": true
    }
    ```

*   **Response (201 CREATED):**

    ```json
    {
      "status": "created",
      "message": "Product variant created successfully",
      "data": {
        "id": "variant-id-1",
        "sku": "IP20PM256",
        "name": "Iphone 20 Pro Max 256GB",
        "imageUrl": "http://example.com/iphone20promax256.jpg",
        "weight": 300.5,
        "dimensions": "160.8 x 78.1 x 7.4 mm",
        "unit": "piece",
        "stock": 100,
        "price": 30000000,
        "currency": "VND",
        "status": "PUBLISHED",
        "optionValueIds": ["val-uuid-promax", "val-uuid-256gb"]
      }
    }
    ```

#### 7. Update Product Variant

Updates the details of an existing product variant.

*   **Endpoint:** `PATCH /api/v1/products/{productId}/variants/{variantId}`
*   **Request Body:**

    ```json
    {
        "sku": "IP20PM256-NEW",
        "name": "iPhone 20 Pro Max 256GB - New Edition",
        "price": 31000000,
        "imageUrl": "http://example.com/new-iphone-variant.jpg",
        "weight": 301.0,
        "dimensions": "160.8 x 78.1 x 7.5 mm",
        "unit": "item",
        "status": "DRAFT",
        "currency": "USD",
        "stock": 50,
        "isDefault": false
    }
    ```

*   **Response (200 OK):** (Same structure as the response for "Create Product Variant")