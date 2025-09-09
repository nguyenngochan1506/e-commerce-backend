import { getProductDetailBySlug } from "@/services/apiService";
import ProductDetailPageClient from "@/components/ProductDetailPageClient";
import { notFound } from "next/navigation";

interface ProductDetailPageProps {
  params: {
    slug: string;
  };
}

export default async function ProductDetailPage({ params: { slug } }: ProductDetailPageProps) {
  
  try {
    const product = await getProductDetailBySlug(slug);
    
    if (!product) {
      notFound();
    }

    return <ProductDetailPageClient product={product} />;

  } catch (error) {
    console.error(`Error fetching product with slug "${slug}":`, error);
    notFound();
  }
}