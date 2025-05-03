import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './order.css';
import { jwtDecode } from 'jwt-decode';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { useAppSelector } from 'app/config/store';
import { Authority } from 'app/shared/constants/authority';

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

function getSubFromToken(token: string): string | null {
  try {
    const decoded = jwtDecode<{ sub: string }>(token);
    return decoded.sub;
  } catch (error) {
    console.error('Failed to decode JWT token:', error);
    return null;
  }
}

const Orders: React.FC = () => {
  const [orders, setOrders] = useState<Order2[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const account = useAppSelector(state => state.authentication.account);

  useEffect(() => {
    const fetchOrders = async () => {
      try {
        const token = localStorage.getItem('authenticationToken');
        const sub = token ? getSubFromToken(token) : null;

        const response = await axios.get('http://localhost:8080/services/order2/api/order-2-s', {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (Array.isArray(response.data)) {
          const allOrders = response.data;
          const isAdmin = hasAnyAuthority(account?.authorities ?? [], [Authority.ADMIN]);

          const filteredOrders = isAdmin ? allOrders : allOrders.filter(order => order.customerId === account?.id);

          setOrders(filteredOrders);
        } else {
          setError('Invalid data format received from server');
        }
      } catch (err) {
        setError('Failed to fetch orders');
      } finally {
        setLoading(false);
      }
    };

    if (account) {
      fetchOrders();
    }
  }, [account]);

  const handleDelete = async (orderId: string) => {
    const confirmDelete = window.confirm('Are you sure you want to delete this order?');
    if (!confirmDelete) return;

    try {
      const token = localStorage.getItem('authenticationToken');
      await axios.delete(`http://localhost:8080/services/order2/api/order-2-s/${orderId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      setOrders(prevOrders => prevOrders.filter(order => order.id !== orderId));
      alert('Order deleted successfully.');
    } catch (err) {
      console.error('Failed to delete order:', err);
      alert('Failed to delete order.');
    }
  };

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
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {orders.length > 0 ? (
            orders.map(order => (
              <tr key={order.id}>
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
                <td>
                  <button
                    onClick={() => handleDelete(order.id)}
                    style={{ color: 'white', backgroundColor: 'red', padding: '5px 10px', border: 'none' }}
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan={7}>No orders available</td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
};

export default Orders;
