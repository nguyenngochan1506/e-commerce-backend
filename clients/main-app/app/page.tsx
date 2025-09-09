import { getCategories, getFeaturedProducts } from "@/services/apiService";
import HomePageClient from "@/components/HomePageClient"; 

export default async function Home() {
  const [categoryResponse, productResponse] = await Promise.all([
    getCategories(),
    getFeaturedProducts()
  ]).catch(err => {
    console.error("Failed to fetch initial data:", err);
    return [{ items: [] }, { items: [] }]; 
  });

  const categories = categoryResponse.items || [];
  const products = productResponse.items || [];

  return <HomePageClient categories={categories} products={products} />;
}