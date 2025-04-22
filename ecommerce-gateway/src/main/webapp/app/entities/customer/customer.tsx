import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './customer.reducer';

export const Customer = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const customerList = useAppSelector(state => state.ecommercegateway.customer.entities);
  const loading = useAppSelector(state => state.ecommercegateway.customer.loading);
  const totalItems = useAppSelector(state => state.ecommercegateway.customer.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="customer-heading" data-cy="CustomerHeading">
        <Translate contentKey="ecommerceGatewayApp.customer.home.title">Customers</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="ecommerceGatewayApp.customer.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/customer/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="ecommerceGatewayApp.customer.home.createLabel">Create new Customer</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {customerList && customerList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="ecommerceGatewayApp.customer.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('firstName')}>
                  <Translate contentKey="ecommerceGatewayApp.customer.firstName">First Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('firstName')} />
                </th>
                <th className="hand" onClick={sort('lastName')}>
                  <Translate contentKey="ecommerceGatewayApp.customer.lastName">Last Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastName')} />
                </th>
                <th className="hand" onClick={sort('dateOfBirth')}>
                  <Translate contentKey="ecommerceGatewayApp.customer.dateOfBirth">Date Of Birth</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('dateOfBirth')} />
                </th>
                <th className="hand" onClick={sort('phoneNumber')}>
                  <Translate contentKey="ecommerceGatewayApp.customer.phoneNumber">Phone Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('phoneNumber')} />
                </th>
                <th className="hand" onClick={sort('addressLine1')}>
                  <Translate contentKey="ecommerceGatewayApp.customer.addressLine1">Address Line 1</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('addressLine1')} />
                </th>
                <th className="hand" onClick={sort('addressLine2')}>
                  <Translate contentKey="ecommerceGatewayApp.customer.addressLine2">Address Line 2</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('addressLine2')} />
                </th>
                <th className="hand" onClick={sort('postalCode')}>
                  <Translate contentKey="ecommerceGatewayApp.customer.postalCode">Postal Code</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('postalCode')} />
                </th>
                <th className="hand" onClick={sort('city')}>
                  <Translate contentKey="ecommerceGatewayApp.customer.city">City</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('city')} />
                </th>
                <th className="hand" onClick={sort('state')}>
                  <Translate contentKey="ecommerceGatewayApp.customer.state">State</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('state')} />
                </th>
                <th className="hand" onClick={sort('country')}>
                  <Translate contentKey="ecommerceGatewayApp.customer.country">Country</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('country')} />
                </th>
                <th className="hand" onClick={sort('preferences')}>
                  <Translate contentKey="ecommerceGatewayApp.customer.preferences">Preferences</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('preferences')} />
                </th>
                <th className="hand" onClick={sort('loyaltyPoints')}>
                  <Translate contentKey="ecommerceGatewayApp.customer.loyaltyPoints">Loyalty Points</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('loyaltyPoints')} />
                </th>
                <th className="hand" onClick={sort('membershipStatus')}>
                  <Translate contentKey="ecommerceGatewayApp.customer.membershipStatus">Membership Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('membershipStatus')} />
                </th>
                <th className="hand" onClick={sort('createdDate')}>
                  <Translate contentKey="ecommerceGatewayApp.customer.createdDate">Created Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedDate')}>
                  <Translate contentKey="ecommerceGatewayApp.customer.lastModifiedDate">Last Modified Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedDate')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {customerList.map((customer, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/customer/${customer.id}`} color="link" size="sm">
                      {customer.id}
                    </Button>
                  </td>
                  <td>{customer.firstName}</td>
                  <td>{customer.lastName}</td>
                  <td>{customer.dateOfBirth ? <TextFormat type="date" value={customer.dateOfBirth} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{customer.phoneNumber}</td>
                  <td>{customer.addressLine1}</td>
                  <td>{customer.addressLine2}</td>
                  <td>{customer.postalCode}</td>
                  <td>{customer.city}</td>
                  <td>{customer.state}</td>
                  <td>{customer.country}</td>
                  <td>{customer.preferences}</td>
                  <td>{customer.loyaltyPoints}</td>
                  <td>
                    <Translate contentKey={`ecommerceGatewayApp.MembershipStatus.${customer.membershipStatus}`} />
                  </td>
                  <td>{customer.createdDate ? <TextFormat type="date" value={customer.createdDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>
                    {customer.lastModifiedDate ? (
                      <TextFormat type="date" value={customer.lastModifiedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/customer/${customer.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/customer/${customer.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/customer/${customer.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="ecommerceGatewayApp.customer.home.notFound">No Customers found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={customerList && customerList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default Customer;
