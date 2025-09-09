"use client";

import React, { useState, useEffect } from "react";
import { getProductsByCategory } from "@/services/apiService";
import { ProductListItem } from "@/types/api.types";
import { Card, CardBody, CardFooter } from "@heroui/card";
import { Image } from "@heroui/image";
import { Breadcrumbs, BreadcrumbItem } from "@heroui/breadcrumbs";
import { Button } from "@heroui/button";
import { Pagination } from "@heroui/pagination";
import { Spinner } from "@heroui/spinner";
import Link from "next/link";

// Props của component này nhận dữ liệu ban đầu từ Server Component
interface CategoryPageClientProps {
  initialProducts: ProductListItem[];
  totalPages: number;
  categoryName: string;
  categorySlug: string;
}

const formatCurrency = (value: number, currency: string) => {
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: currency }).format(value);
};

export default function CategoryPageClient({ initialProducts, totalPages, categoryName, categorySlug }: CategoryPageClientProps) {
  const [products, setProducts] = useState<ProductListItem[]>(initialProducts);
  const [currentPage, setCurrentPage] = useState(1);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (currentPage === 1) {
      setProducts(initialProducts);
      return;
    }

    const fetchMoreProducts = async () => {
      try {
        setIsLoading(true);
        setError(null);
        const response = await getProductsByCategory(categorySlug, currentPage);
        setProducts(response.items);
      } catch (err: any) {
        setError(err.message);
      } finally {
        setIsLoading(false);
      }
    };

    fetchMoreProducts();
  }, [currentPage, categorySlug, initialProducts]);


  return (
    <div className="container mx-auto py-6">
      <Breadcrumbs className="mb-4">
        <BreadcrumbItem href="/">Trang chủ</BreadcrumbItem>
        <BreadcrumbItem href={`/category/${categorySlug}`}>{categoryName}</BreadcrumbItem>
      </Breadcrumbs>

      <div className="bg-background p-4 sm:p-6 rounded-lg shadow-sm">
        <h1 className="text-2xl font-bold mb-4">
          Sản phẩm trong danh mục: <span className="text-primary">{categoryName}</span>
        </h1>

        <div className="flex flex-wrap items-center gap-2 bg-default-100/60 p-3 rounded-md mb-6">
          <span className="font-semibold mr-2">Sắp xếp theo</span>
          <Button size="sm" color="primary" variant="solid">Phổ biến</Button>
          <Button size="sm" variant="flat">Mới nhất</Button>
          <Button size="sm" variant="flat">Bán chạy</Button>
        </div>
        
        {isLoading ? (
             <div className="flex justify-center items-center h-96"><Spinner label="Đang tải..." /></div>
        ) : error ? (
            <div className="text-center text-danger p-8">Lỗi: {error}</div>
        ) : (
            <>
                <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 gap-4">
                {products.map((product) => (
                    <Link href={`/product/${product.slug}`} key={product.id} passHref>
                        <Card shadow="sm" isPressable className="h-full">
                            <CardBody className="overflow-visible p-0">
                                <Image
                                shadow="sm"
                                radius="lg"
                                width="100%"
                                alt={product.name}
                                className="w-full object-cover h-[200px]"
                                src={product.thumbnail}
                                />
                            </CardBody>
                            <CardFooter className="flex-col items-start p-3">
                                <p className="text-sm text-default-500 line-clamp-2 h-10">{product.name}</p>
                                <div className="w-full flex justify-between items-center mt-2">
                                <b className="text-primary text-md">{formatCurrency(product.defaultVariant.price, product.defaultVariant.currency)}</b>
                                </div>
                                <p className="text-xs text-default-400 mt-1">Đã bán {product.defaultVariant.stock}+</p>
                            </CardFooter>
                        </Card>
                    </Link>
                ))}
                </div>

                <div className="flex justify-center mt-8">
                <Pagination
                    isCompact
                    showControls
                    total={totalPages}
                    page={currentPage}
                    onChange={setCurrentPage}
                />
                </div>
            </>
        )}
      </div>
    </div>
  );
}