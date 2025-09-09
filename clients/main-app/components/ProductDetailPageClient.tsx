"use client";

import React, { useState, useMemo, useEffect } from 'react';
import { Image } from "@heroui/image";
import { Button } from "@heroui/button";
import { Breadcrumbs, BreadcrumbItem } from "@heroui/breadcrumbs";
import { ProductDetail, ProductOption } from '@/types/api.types';

interface ProductDetailPageClientProps {
  product: ProductDetail;
}

const formatCurrency = (value: number, currency: string) => {
  return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: currency }).format(value);
};

export default function ProductDetailPageClient({ product }: ProductDetailPageClientProps) {
  
  const [selectedOptions, setSelectedOptions] = useState<Record<string, string>>({});
  const [quantity, setQuantity] = useState(1);

  useEffect(() => {
    const defaultVariant = product.variants.find(v => v.isDefault) || product.variants.find(v => v.stock > 0) || product.variants[0];
    if (defaultVariant) {
      const initialSelections: Record<string, string> = {};
      product.options.forEach(option => {
        const valueInVariant = option.values.find(val => defaultVariant.optionValueIds.includes(val.id));
        if (valueInVariant) {
          initialSelections[option.id] = valueInVariant.id;
        }
      });
      setSelectedOptions(initialSelections);
    }
  }, [product]);

  const handleOptionSelect = (optionId: string, valueId: string) => {
    // Nếu người dùng click lại vào option đã chọn, bỏ chọn nó
    const newSelectedOptions = { ...selectedOptions };
    if (newSelectedOptions[optionId] === valueId) {
      delete newSelectedOptions[optionId];
    } else {
      newSelectedOptions[optionId] = valueId;
    }
    setSelectedOptions(newSelectedOptions);
    setQuantity(1);
  };

  // --- LOGIC MỚI: TÌM BIẾN THỂ KHỚP CHÍNH XÁC ---
  const selectedVariant = useMemo(() => {
    const selectedIds = Object.values(selectedOptions);
    // Chỉ tìm biến thể khi người dùng đã chọn đủ số lượng options
    if (selectedIds.length !== product.options.length) return null;

    // Sắp xếp để đảm bảo thứ tự không ảnh hưởng đến việc so sánh
    const selectedIdsSorted = [...selectedIds].sort();

    return product.variants.find(variant => {
      const variantIdsSorted = [...variant.optionValueIds].sort();
      // So sánh hai mảng đã được sắp xếp
      return JSON.stringify(variantIdsSorted) === JSON.stringify(selectedIdsSorted);
    });
  }, [selectedOptions, product.variants, product.options]);

  // --- LOGIC MỚI: VÔ HIỆU HÓA CÁC NÚT BẤM KHÔNG HỢP LỆ ---
  const isOptionDisabled = (optionId: string, valueId: string): boolean => {
    // Lấy ra các lựa chọn hiện tại nhưng không bao gồm lựa chọn của nhóm đang xét
    const otherSelectedIds = Object.entries(selectedOptions)
      .filter(([key]) => key !== optionId)
      .map(([, val]) => val);
    
    // Tập hợp các ID cần kiểm tra: các lựa chọn ở nhóm khác + lựa chọn của nút đang xét
    const idsToTest = [...otherSelectedIds, valueId];

    // Tìm xem có bất kỳ biến thể nào chứa TẤT CẢ các ID trong tập hợp cần kiểm tra không
    const isPossible = product.variants.some(variant => {
      return idsToTest.every(id => variant.optionValueIds.includes(id));
    });

    return !isPossible;
  };


  const handleQuantityChange = (amount: number) => {
    setQuantity(prev => {
        const newQuantity = prev + amount;
        if (newQuantity < 1) return 1;
        if (selectedVariant && newQuantity > selectedVariant.stock) return selectedVariant.stock;
        return newQuantity;
    });
  }

  return (
    <div className="container mx-auto p-4">
       <Breadcrumbs className="mb-4">
        {product.categories.map((cat, index) => (
          <BreadcrumbItem 
            key={cat.id} 
            href={index === 0 ? '/' : `/category/${cat.slug}`}
          >
            {cat.name}
          </BreadcrumbItem>
        ))}
      </Breadcrumbs>
      
      <div className="bg-background p-6 rounded-lg shadow-sm">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
          <div>
            <Image
              isZoomed
              width="100%"
              src={selectedVariant?.imageUrl || product.thumbnail}
              alt={product.name}
              className="w-full h-auto object-cover rounded-lg"
            />
          </div>

          <div>
            <h1 className="text-2xl font-bold mb-2">{selectedVariant?.name || product.name}</h1>
            
            <div className="bg-default-100/60 p-4 rounded-lg my-4">
              <h2 className="text-3xl font-bold text-primary">
                {selectedVariant 
                  ? formatCurrency(selectedVariant.price, selectedVariant.currency) 
                  : "Vui lòng chọn đầy đủ tùy chọn"}
              </h2>
            </div>
           
            {product.options.map(option => (
              <div key={option.id} className="mb-4">
                <h3 className="font-semibold mb-2 capitalize">{option.name}:</h3>
                <div className="flex flex-wrap gap-2">
                  {option.values.map(value => {
                    const isSelected = selectedOptions[option.id] === value.id;
                    const isDisabled = isOptionDisabled(option.id, value.id);
                    return (
                        <Button
                            key={value.id}
                            variant={isSelected ? 'solid' : 'bordered'}
                            color="primary"
                            onPress={() => handleOptionSelect(option.id, value.id)}
                            isDisabled={isDisabled}
                        >
                            {value.value}
                        </Button>
                    );
                    })}
                </div>
              </div>
            ))}

            <div className="flex items-center gap-4 my-6">
              <h3 className="font-semibold">Số lượng:</h3>
              <div className="flex items-center border rounded-md">
                <Button isIconOnly variant="light" size="sm" onPress={() => handleQuantityChange(-1)} isDisabled={!selectedVariant}>-</Button>
                <span className="w-10 text-center">{quantity}</span>
                <Button isIconOnly variant="light" size="sm" onPress={() => handleQuantityChange(1)} isDisabled={!selectedVariant}>+</Button>
              </div>
              <span className="text-default-500 text-sm">
                {selectedVariant ? `${selectedVariant.stock} sản phẩm có sẵn` : ''}
              </span>
            </div>

            <div className="flex gap-4">
              <Button color="primary" variant="bordered" size="lg" isDisabled={!selectedVariant}>Thêm vào giỏ hàng</Button>
              <Button color="primary" size="lg" isDisabled={!selectedVariant || selectedVariant.stock === 0}>
                {selectedVariant && selectedVariant.stock > 0 ? 'Mua ngay' : (selectedVariant ? 'Hết hàng' : 'Chọn phiên bản')}
              </Button>
            </div>
          </div>
        </div>
      </div>
      
       <div className="bg-background p-6 rounded-lg shadow-sm mt-8">
            <h2 className="text-xl font-bold mb-4">Mô tả sản phẩm</h2>
            <p className="text-default-700 whitespace-pre-line">{product.description}</p>
        </div>

        <div className="bg-background p-6 rounded-lg shadow-sm mt-8">
            <h2 className="text-xl font-bold mb-4">Thông số kỹ thuật</h2>
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-x-8 gap-y-2">
                 {product.attributes.map(attr => (
                    <div key={attr.key} className="flex border-b py-2 first:border-t">
                        <span className="w-1/3 text-default-500">{attr.key}</span>
                        <span className="w-2/3 font-medium">{String(attr.value)}</span>
                    </div>
                ))}
            </div>
        </div>
    </div>
  );
}