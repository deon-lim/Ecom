import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import { Col, Row, Button } from 'reactstrap';
import { useAppSelector } from 'app/config/store';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <Row className="home-page align-items-center justify-content-center text-center">
      <Col md="8">
        <div className="hero-section">
          <h1 className="display-3 fw-bold mb-4">Welcome to SwiftCart</h1>
          <p className="lead mb-4">Discover amazing products tailored to your needs. Sign up and start shopping now!</p>
          {account?.login ? (
            <div className="user-greeting">
              <h4 className="text-success">Hi, {account.login}! Start exploring our store now.</h4>
              <Link to="/product">
                <Button color="primary" size="lg" className="mt-3">
                  Browse Products
                </Button>
              </Link>
            </div>
          ) : (
            <div className="auth-buttons">
              <Link to="/account/register">
                <Button color="success" size="lg" className="me-3">
                  Create Account
                </Button>
              </Link>
              <Link to="/login">
                <Button color="outline-primary" size="lg">
                  Login
                </Button>
              </Link>
            </div>
          )}
        </div>
        <div className="features-section mt-5">
          <h2 className="mb-4">Why Shop With Us?</h2>
          <Row>
            <Col md="4">
              <div className="feature-box">
                {/* <img src="/content/images/shipping.svg" alt="Fast Shipping" className="feature-icon" /> */}
                <h5>Fast Shipping</h5>
                <p>Next-day delivery available on most products.</p>
              </div>
            </Col>
            <Col md="4">
              <div className="feature-box">
                {/* <img src="/content/images/secure.svg" alt="Secure Payment" className="feature-icon" /> */}
                <h5>Secure Payment</h5>
                <p>Safe and encrypted checkout experience.</p>
              </div>
            </Col>
            <Col md="4">
              <div className="feature-box">
                {/* <img src="/content/images/support.svg" alt="24/7 Support" className="feature-icon" /> */}
                <h5>24/7 Support</h5>
                <p>We&apos;re here to help any time, any day.</p>
              </div>
            </Col>
          </Row>
        </div>
      </Col>
    </Row>
  );
};

export default Home;
