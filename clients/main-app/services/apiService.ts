import { Category, PageResponse, ProductDetail, ProductListItem } from "@/types/api.types";

const API_BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL;

async function handleResponse<T>(response: Response): Promise<T> {
  if (!response.ok) {
    const errorData = await response.json().catch(() => ({ message: "An unknown error occurred" }));
    throw new Error(errorData.message || `API call failed with status: ${response.status}`);
  }
  
  const result = await response.json();
  return result.data;
}

// get all categories
export async function getCategories(): Promise<PageResponse<Category>> {
  const response = await fetch(`${API_BASE_URL}/categories`);
  return handleResponse<PageResponse<Category>>(response);
}

// get featured products
export async function getFeaturedProducts(): Promise<PageResponse<ProductListItem>> {
  const response = await fetch(`${API_BASE_URL}/products?size=10&sort=id:desc`);
  return handleResponse<PageResponse<ProductListItem>>(response);
}

// get products by category slug with pagination
export async function getProductsByCategory(slug: string, page: number = 1): Promise<PageResponse<ProductListItem>> {
  const response = await fetch(`${API_BASE_URL}/products/category/${slug}?page=${page}&size=20`);
  return handleResponse<PageResponse<ProductListItem>>(response);
}

// get product detail by slug
export async function getProductDetailBySlug(slug: string): Promise<ProductDetail> {
  const response = await fetch(`${API_BASE_URL}/products/${slug}`);
  return handleResponse<ProductDetail>(response);
}