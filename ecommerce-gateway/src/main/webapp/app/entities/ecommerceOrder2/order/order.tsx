import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './order.css';

interface Order2Product {
  productId: string;
  productPrice: number | null;
  quantity: number;
}

interface Order2 {
  id: string;
  customerId: string;
  orderStatus: string;
  totalAmount: number | null;
  createdOn: string;
  order2Products: Order2Product[];
}

const Orders: React.FC = () => {
  const [orders, setOrders] = useState<Order2[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchOrders = async () => {
      try {
        const token = 'your-jwt-token-here'; // JWT token (use actual logic for fetching token)
        if (!token) {
          setError('No JWT token found!');
          return;
        }

        // Fetch the orders from your API
        const response = await axios.get('http://localhost:8080/services/order2/api/order-2-s', {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (Array.isArray(response.data)) {
          setOrders(response.data);
        } else {
          setError('Invalid data format received from server');
        }
      } catch (err) {
        setError('Failed to fetch orders');
      } finally {
        setLoading(false);
      }
    };

    fetchOrders();
  }, []);

  if (loading) return <div className="loading">Loading...</div>;
  if (error) return <div className="error">{error}</div>;

  return (
    <div className="orders-container">
      <h1>Orders</h1>
      <table>
        <thead>
          <tr>
            <th>Order ID</th>
            <th>Customer ID</th>
            <th>Order Status</th>
            <th>Total Amount</th>
            <th>Created On</th>
            <th>Products</th>
          </tr>
        </thead>
        <tbody>
          {orders.length > 0 ? (
            orders.map(order => (
              <React.Fragment key={order.id}>
                <tr>
                  <td>{order.id}</td>
                  <td>{order.customerId}</td>
                  <td>{order.orderStatus}</td>
                  <td>${(order.totalAmount ?? 0).toFixed(2)}</td>
                  <td>{new Date(order.createdOn).toLocaleString()}</td>
                  <td>
                    {order.order2Products.length > 0 ? (
                      <ul>
                        {order.order2Products.map(product => (
                          <li key={product.productId}>
                            <p>Product ID: {product.productId}</p>
                            <p>Price: ${(product.productPrice ?? 0).toFixed(2)}</p>
                            <p>Quantity: {product.quantity}</p>
                          </li>
                        ))}
                      </ul>
                    ) : (
                      <p>No products available</p>
                    )}
                  </td>
                </tr>
              </React.Fragment>
            ))
          ) : (
            <tr>
              <td colSpan={6}>No orders available</td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
};

export default Orders;
