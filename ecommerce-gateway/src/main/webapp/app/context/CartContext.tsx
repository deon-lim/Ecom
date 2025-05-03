import React, { createContext, useState, useContext, ReactNode, useEffect } from 'react';
/* eslint-disable no-console */
interface CartProduct {
  productId: string;
  name: string;
  price: number;
  quantity: number;
}

interface CartContextType {
  cart: CartProduct[];
  addToCart: (product: CartProduct) => void;
  removeFromCart: (productId: string) => void;
  updateQuantity: (productId: string, quantity: number) => void;
  calculateTotal: () => number;
  clearCart: () => void; // ✅ Add this
}

const CartContext = createContext<CartContextType | undefined>(undefined);

export const useCart = () => {
  const context = useContext(CartContext);
  if (!context) {
    throw new Error('useCart must be used within a CartProvider');
  }
  return context;
};

export const CartProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [cart, setCart] = useState<CartProduct[]>(() => {
    const storedCart = localStorage.getItem('cart');
    console.log('Stored Cart:', storedCart);
    return storedCart ? JSON.parse(storedCart) : [];
  });

  useEffect(() => {
    console.log('Cart state:', cart);
    localStorage.setItem('cart', JSON.stringify(cart));
  }, [cart]);

  const addToCart = (product: CartProduct) => {
    setCart(prevCart => {
      const existingProduct = prevCart.find(item => item.productId === product.productId);
      if (existingProduct) {
        return prevCart.map(item =>
          item.productId === product.productId ? { ...item, quantity: item.quantity + product.quantity } : item,
        );
      }
      return [...prevCart, product];
    });
  };

  const removeFromCart = (productId: string) => {
    setCart(prevCart => prevCart.filter(item => item.productId !== productId));
  };

  const updateQuantity = (productId: string, quantity: number) => {
    setCart(prevCart => prevCart.map(item => (item.productId === productId ? { ...item, quantity } : item)));
  };

  const calculateTotal = () => {
    return cart.reduce((total, item) => total + item.price * item.quantity, 0);
  };

  const clearCart = () => {
    setCart([]);
  };

  return (
    <CartContext.Provider value={{ cart, addToCart, removeFromCart, updateQuantity, calculateTotal, clearCart }}>
      {children}
    </CartContext.Provider>
  );
};
