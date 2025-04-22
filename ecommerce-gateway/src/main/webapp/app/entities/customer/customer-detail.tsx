import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './customer.reducer';

export const CustomerDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const customerEntity = useAppSelector(state => state.ecommercegateway.customer.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="customerDetailsHeading">
          <Translate contentKey="ecommerceGatewayApp.customer.detail.title">Customer</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{customerEntity.id}</dd>
          <dt>
            <span id="firstName">
              <Translate contentKey="ecommerceGatewayApp.customer.firstName">First Name</Translate>
            </span>
          </dt>
          <dd>{customerEntity.firstName}</dd>
          <dt>
            <span id="lastName">
              <Translate contentKey="ecommerceGatewayApp.customer.lastName">Last Name</Translate>
            </span>
          </dt>
          <dd>{customerEntity.lastName}</dd>
          <dt>
            <span id="dateOfBirth">
              <Translate contentKey="ecommerceGatewayApp.customer.dateOfBirth">Date Of Birth</Translate>
            </span>
          </dt>
          <dd>
            {customerEntity.dateOfBirth ? <TextFormat value={customerEntity.dateOfBirth} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="phoneNumber">
              <Translate contentKey="ecommerceGatewayApp.customer.phoneNumber">Phone Number</Translate>
            </span>
          </dt>
          <dd>{customerEntity.phoneNumber}</dd>
          <dt>
            <span id="addressLine1">
              <Translate contentKey="ecommerceGatewayApp.customer.addressLine1">Address Line 1</Translate>
            </span>
          </dt>
          <dd>{customerEntity.addressLine1}</dd>
          <dt>
            <span id="addressLine2">
              <Translate contentKey="ecommerceGatewayApp.customer.addressLine2">Address Line 2</Translate>
            </span>
          </dt>
          <dd>{customerEntity.addressLine2}</dd>
          <dt>
            <span id="postalCode">
              <Translate contentKey="ecommerceGatewayApp.customer.postalCode">Postal Code</Translate>
            </span>
          </dt>
          <dd>{customerEntity.postalCode}</dd>
          <dt>
            <span id="city">
              <Translate contentKey="ecommerceGatewayApp.customer.city">City</Translate>
            </span>
          </dt>
          <dd>{customerEntity.city}</dd>
          <dt>
            <span id="state">
              <Translate contentKey="ecommerceGatewayApp.customer.state">State</Translate>
            </span>
          </dt>
          <dd>{customerEntity.state}</dd>
          <dt>
            <span id="country">
              <Translate contentKey="ecommerceGatewayApp.customer.country">Country</Translate>
            </span>
          </dt>
          <dd>{customerEntity.country}</dd>
          <dt>
            <span id="preferences">
              <Translate contentKey="ecommerceGatewayApp.customer.preferences">Preferences</Translate>
            </span>
          </dt>
          <dd>{customerEntity.preferences}</dd>
          <dt>
            <span id="loyaltyPoints">
              <Translate contentKey="ecommerceGatewayApp.customer.loyaltyPoints">Loyalty Points</Translate>
            </span>
          </dt>
          <dd>{customerEntity.loyaltyPoints}</dd>
          <dt>
            <span id="membershipStatus">
              <Translate contentKey="ecommerceGatewayApp.customer.membershipStatus">Membership Status</Translate>
            </span>
          </dt>
          <dd>{customerEntity.membershipStatus}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="ecommerceGatewayApp.customer.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {customerEntity.createdDate ? <TextFormat value={customerEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="ecommerceGatewayApp.customer.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {customerEntity.lastModifiedDate ? (
              <TextFormat value={customerEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/customer" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/customer/${customerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CustomerDetail;
