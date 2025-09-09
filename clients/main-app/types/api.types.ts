// CategoryResponseDto
export interface Category {
  id: string;
  name: string;
  description?: string;
  slug: string;
  thumbnail?: string;
  parentId: string | null;
  level: number;
  children?: Category[];
}

//ProductOptionValueResponseDto
export interface ProductOptionValue {
    id: string;
    value: string;
}

//ProductOptionResponse
export interface ProductOption {
    id: string;
    name: string;
    values: ProductOptionValue[];
}

// Tương ứng với ProductVariantResponseDto
export interface ProductVariant {
  id: string;
  sku: string;
  name: string;
  imageUrl: string;
  price: number;
  currency: "VND" | "USD";
  stock: number;
  weight: number; // in grams
  dimensions: string;
  unit: string; // e.g., "piece", "box"
  isDefault: boolean;
  optionValueIds: string[]; 
}

//ProductAttributes
export interface ProductAttributes {
    key: string;
    value: string | number | boolean | null;
}

//ProductResponseDto{
export interface Product {
  id: string;
  name: string;
  slug: string;
  description?: string ;
  thumbnail: string;
  categories: Category[];
  attributes: ProductAttributes[];
}

// ProductListResponseDto
export interface ProductListItem extends Product {
  defaultVariant: ProductVariant;
}

//ProductDetailResponse
export interface ProductDetail extends Product{
    variants: ProductVariant[];
    options: ProductOption[];
}

export interface PageResponse<T> {
  currentPage: number;
  totalPages: number;
  totalItems: number;
  items: T[];
}
