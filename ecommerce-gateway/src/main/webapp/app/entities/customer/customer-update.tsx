import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { MembershipStatus } from 'app/shared/model/enumerations/membership-status.model';
import { createEntity, getEntity, reset, updateEntity } from './customer.reducer';

export const CustomerUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const customerEntity = useAppSelector(state => state.ecommercegateway.customer.entity);
  const loading = useAppSelector(state => state.ecommercegateway.customer.loading);
  const updating = useAppSelector(state => state.ecommercegateway.customer.updating);
  const updateSuccess = useAppSelector(state => state.ecommercegateway.customer.updateSuccess);
  const membershipStatusValues = Object.keys(MembershipStatus);

  const handleClose = () => {
    navigate(`/customer${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.dateOfBirth = convertDateTimeToServer(values.dateOfBirth);
    if (values.loyaltyPoints !== undefined && typeof values.loyaltyPoints !== 'number') {
      values.loyaltyPoints = Number(values.loyaltyPoints);
    }
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...customerEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          dateOfBirth: displayDefaultDateTime(),
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          membershipStatus: 'Bronze',
          ...customerEntity,
          dateOfBirth: convertDateTimeFromServer(customerEntity.dateOfBirth),
          createdDate: convertDateTimeFromServer(customerEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(customerEntity.lastModifiedDate),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="ecommerceGatewayApp.customer.home.createOrEditLabel" data-cy="CustomerCreateUpdateHeading">
            <Translate contentKey="ecommerceGatewayApp.customer.home.createOrEditLabel">Create or edit a Customer</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="customer-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('ecommerceGatewayApp.customer.firstName')}
                id="customer-firstName"
                name="firstName"
                data-cy="firstName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('ecommerceGatewayApp.customer.lastName')}
                id="customer-lastName"
                name="lastName"
                data-cy="lastName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('ecommerceGatewayApp.customer.dateOfBirth')}
                id="customer-dateOfBirth"
                name="dateOfBirth"
                data-cy="dateOfBirth"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('ecommerceGatewayApp.customer.phoneNumber')}
                id="customer-phoneNumber"
                name="phoneNumber"
                data-cy="phoneNumber"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('ecommerceGatewayApp.customer.addressLine1')}
                id="customer-addressLine1"
                name="addressLine1"
                data-cy="addressLine1"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('ecommerceGatewayApp.customer.addressLine2')}
                id="customer-addressLine2"
                name="addressLine2"
                data-cy="addressLine2"
                type="text"
              />
              <ValidatedField
                label={translate('ecommerceGatewayApp.customer.postalCode')}
                id="customer-postalCode"
                name="postalCode"
                data-cy="postalCode"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('ecommerceGatewayApp.customer.city')}
                id="customer-city"
                name="city"
                data-cy="city"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('ecommerceGatewayApp.customer.state')}
                id="customer-state"
                name="state"
                data-cy="state"
                type="text"
              />
              <ValidatedField
                label={translate('ecommerceGatewayApp.customer.country')}
                id="customer-country"
                name="country"
                data-cy="country"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('ecommerceGatewayApp.customer.preferences')}
                id="customer-preferences"
                name="preferences"
                data-cy="preferences"
                type="text"
              />
              <ValidatedField
                label={translate('ecommerceGatewayApp.customer.loyaltyPoints')}
                id="customer-loyaltyPoints"
                name="loyaltyPoints"
                data-cy="loyaltyPoints"
                type="text"
              />
              <ValidatedField
                label={translate('ecommerceGatewayApp.customer.membershipStatus')}
                id="customer-membershipStatus"
                name="membershipStatus"
                data-cy="membershipStatus"
                type="select"
              >
                {membershipStatusValues.map(membershipStatus => (
                  <option value={membershipStatus} key={membershipStatus}>
                    {translate(`ecommerceGatewayApp.MembershipStatus.${membershipStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('ecommerceGatewayApp.customer.createdDate')}
                id="customer-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('ecommerceGatewayApp.customer.lastModifiedDate')}
                id="customer-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/customer" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default CustomerUpdate;
