import React from 'react';
import { useCart } from '../../context/CartContext'; // Import the useCart hook
import axios from 'axios'; // You will need axios for making API calls
import { useAppSelector } from 'app/config/store'; // Assuming this is used to get the account details

const Cart: React.FC = () => {
  const { cart, addToCart, removeFromCart, updateQuantity, calculateTotal, clearCart } = useCart();
  const account = useAppSelector(state => state.authentication.account); // Getting account details

  // Function to handle clearing the cart with confirmation
  const handleClearCart = () => {
    const confirmClear = window.confirm('Are you sure you want to clear the cart?');
    if (confirmClear) {
      clearCart();
    }
  };

  // Function to handle checkout
  const handleCheckout = async () => {
    if (cart.length === 0) {
      alert('Your cart is empty. Please add items to your cart before checking out.');
      return;
    }

    // Prepare the order data to send to backend
    const orderData = {
      customerId: account?.id,
      orderStatus: 'PENDING',
      totalAmount: calculateTotal(),
      createdOn: new Date().toISOString(), // Current timestamp
      order2Products: cart.map(product => ({
        productId: product.productId,
        productPrice: product.price,
        quantity: product.quantity,
      })),
    };

    try {
      const token = localStorage.getItem('authenticationToken');
      const response = await axios.post('http://localhost:8080/services/order2/api/order-2-s', orderData, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (response.status === 200) {
        // Update the stock in the product catalog after successful order
        await updateProductStock();

        alert('Checkout successful!');
        clearCart(); // Clear the cart after successful checkout
      }
    } catch (error) {
      console.error('Error during checkout:', error);
      alert('An error occurred during checkout. Please try again later.');
    }
  };

  // Function to update product stock after checkout
  const updateProductStock = async () => {
    try {
      // For each product in the cart, update the stock
      for (const product of cart) {
        const response = await axios.put(`http://localhost:8080/api/products/update-stock/${product.productId}`, null, {
          params: { quantity: product.quantity },
          headers: {
            Authorization: `Bearer ${localStorage.getItem('authenticationToken')}`,
          },
        });

        if (response.status !== 200) {
          alert(`Failed to update stock for product: ${product.productId}`);
        }
      }
    } catch (error) {
      console.error('Error updating product stock:', error);
      alert('An error occurred while updating product stock.');
    }
  };

  return (
    <div className="cart-container">
      <h2>Shopping Cart</h2>
      {cart.length === 0 ? (
        <p>Your cart is empty</p>
      ) : (
        <div>
          <table>
            <thead>
              <tr>
                <th>Product ID</th>
                <th>Name</th>
                <th>Price</th>
                <th>Quantity</th>
                <th>Total</th>
                <th>Remove</th>
              </tr>
            </thead>
            <tbody>
              {cart.map(product => (
                <tr key={product.productId}>
                  <td>{product.productId}</td>
                  <td>{product.name}</td>
                  <td>${product.price.toFixed(2)}</td>
                  <td>
                    <input
                      type="number"
                      value={product.quantity}
                      min="1"
                      onChange={e => {
                        const value = parseInt(e.target.value, 10);
                        if (!isNaN(value) && value > 0) {
                          updateQuantity(product.productId, value);
                        }
                      }}
                    />
                  </td>
                  <td>${(product.price * product.quantity).toFixed(2)}</td>
                  <td>
                    <button type="button" onClick={() => removeFromCart(product.productId)}>
                      Remove
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          <div className="cart-total">
            <h3>Total: ${calculateTotal().toFixed(2)}</h3>
            <button type="button" onClick={handleClearCart} style={{ marginTop: '10px' }}>
              Clear Cart
            </button>
            <button
              type="button"
              onClick={handleCheckout}
              style={{
                marginTop: '10px',
                marginLeft: '10px',
                backgroundColor: 'green',
                color: 'white',
              }}
            >
              Checkout
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default Cart;
