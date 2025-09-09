"use client";

import { Category, ProductListItem } from "@/types/api.types";
import { Card, CardBody, CardFooter } from "@heroui/card";
import { Divider } from "@heroui/divider";
import { Image } from "@heroui/image";
import Link from "next/link";

interface HomePageClientProps {
  categories: Category[];
  products: ProductListItem[];
}

const formatCurrency = (value: number, currency: string) => {
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: currency }).format(value);
};

export default function HomePageClient({ categories, products }: HomePageClientProps) {
  return (
    <div className="flex flex-col gap-8 py-8 md:py-10">
      {/* KHU VỰC DANH MỤC SẢN PHẨM */}
      <section>
        <div className="bg-background p-4 rounded-lg shadow-sm">
          <h2 className="text-xl font-bold mb-4">Danh mục sản phẩm</h2>
          <div className="grid grid-cols-3 sm:grid-cols-4 md:grid-cols-6 lg:grid-cols-8 gap-2">
            {categories
              .filter(category => category.level === 1)
              .map((category) => (
                <Link href={`/category/${category.slug}`} key={category.id} passHref>
                  <Card shadow="none" isHoverable isPressable className="border-none text-center items-center h-full">
                    <CardBody className="overflow-visible p-0 items-center">
                      <Image
                        isZoomed
                        shadow="sm"
                        radius="lg"
                        width={80}
                        height={80}
                        alt={category.name}
                        className="w-full object-cover"
                        src={category.thumbnail}
                      />
                    </CardBody>
                    <CardFooter className="flex-col items-center p-2">
                      <p className="text-sm text-center text-default-600">{category.name}</p>
                    </CardFooter>
                  </Card>
                </Link>
              ))}
          </div>
        </div>
      </section>

      <Divider />

      <section className="flex flex-col items-center justify-center gap-4">
        <div className="text-center">
          <h1 className="text-3xl font-bold">Sản phẩm nổi bật</h1>
          <p className="text-lg text-default-500">Khám phá các sản phẩm mới nhất của chúng tôi</p>
        </div>
        <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 gap-4">
          {products.map((product) => (
             <Link href={`/product/${product.slug}`} key={product.id} passHref>
                <Card shadow="sm" isPressable>
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
      </section>
    </div>
  );
}