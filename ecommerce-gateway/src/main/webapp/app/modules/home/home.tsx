import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import { Col, Row, Alert, Button } from 'reactstrap';

import { useAppSelector } from 'app/config/store';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <Row>
      <Col md="12" className="text-center my-5">
        <h1 className="display-3 font-weight-bold">🛒 Welcome to ShopEasy!</h1>
        <p className="lead">Your one-stop destination for quality products at the best prices.</p>
        {account?.login ? (
          <Alert color="success" className="mt-4">
            Hello <strong>{account.login}</strong>! Browse our latest deals below.
          </Alert>
        ) : (
          <div className="mt-4">
            <Alert color="warning">
              New here?&nbsp;
              <Link to="/account/register" className="alert-link">
                Create an account
              </Link>
              &nbsp;or&nbsp;
              <Link to="/login" className="alert-link">
                log in
              </Link>
              &nbsp;to start shopping!
            </Alert>
          </div>
        )}
      </Col>

      <Col md="12" className="my-5">
        <h2 className="text-center">Featured Categories</h2>
        <Row className="justify-content-center my-4">
          <Col md="3" className="text-center">
            <div className="category-tile p-4 border rounded shadow-sm">
              <h5>Electronics</h5>
              <p>Latest gadgets and accessories</p>
              <Button tag={Link} to="/product" color="primary">
                Shop Now
              </Button>
            </div>
          </Col>
          <Col md="3" className="text-center">
            <div className="category-tile p-4 border rounded shadow-sm">
              <h5>Fashion</h5>
              <p>Trendy outfits and more</p>
              <Button tag={Link} to="/product" color="primary">
                Explore
              </Button>
            </div>
          </Col>
          <Col md="3" className="text-center">
            <div className="category-tile p-4 border rounded shadow-sm">
              <h5>Home Essentials</h5>
              <p>Everyday must-haves</p>
              <Button tag={Link} to="/product" color="primary">
                View
              </Button>
            </div>
          </Col>
        </Row>
      </Col>

      <Col md="12" className="text-center mt-5">
        <h4>Need help?</h4>
        <p>Check out our FAQ or contact support for assistance.</p>
      </Col>
    </Row>
  );
};

export default Home;
