import { getProductsByCategory } from "@/services/apiService";
import CategoryPageClient from "@/components/CategoryPageClient";
import { capitalize } from "lodash";

interface CategoryPageProps {
  params: {
    slug: string;
  };
}

export default async function CategoryPage({ params: { slug } }: CategoryPageProps) {

  const initialData = await getProductsByCategory(slug, 1).catch(err => {
    console.error(`Failed to fetch products for category ${slug}:`, err);
    return {
      items: [],
      totalPages: 0,
      totalItems: 0,
      currentPage: 1,
    };
  });

  const categoryName = capitalize(slug.replace(/-/g, " "));

  return (
    <CategoryPageClient
      initialProducts={initialData.items}
      totalPages={initialData.totalPages}
      categoryName={categoryName}
      categorySlug={slug}
    />
  );
}