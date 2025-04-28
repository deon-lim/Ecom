import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import CustomerDetail from './customer-detail';

import { getEntities, getEntity } from './customer.reducer';
import { Authority } from 'app/shared/constants/authority';
import { hasAnyAuthority } from 'app/shared/auth/private-route';

export const Customer = () => {
  const dispatch = useAppDispatch();
  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  // pull account info & authorities from state
  const account = useAppSelector(state => state.authentication.account);
  const authorities = account?.authorities;

  // list vs single-entity state
  const customerList = useAppSelector(state => state.gateway.customer.entities);
  const customerEntity = useAppSelector(state => state.gateway.customer.entity);
  const loading = useAppSelector(state => state.gateway.customer.loading);
  const totalItems = useAppSelector(state => state.gateway.customer.totalItems);

  const getAllEntities = () =>
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  // effect: on mount & pagination change
  useEffect(() => {
    if (hasAnyAuthority(authorities, [Authority.ADMIN])) {
      // admin: fetch full list
      sortEntities();
    } else if (account?.id) {
      // regular user: fetch only their own customer record
      dispatch(getEntity(account.id));
    }
    // note: we include account.id and authorities to re-run if user logs in/out or role changes
  }, [paginationState.activePage, paginationState.order, paginationState.sort, authorities, account?.id]);

  // update pagination from URL
  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const [sortField, sortOrder] = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortField,
        order: sortOrder,
      });
    }
  }, [pageLocation.search]);

  const sort = p => () =>
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    if (hasAnyAuthority(authorities, [Authority.ADMIN])) {
      sortEntities();
    }
  };

  const getSortIconByFieldName = fieldName => {
    if (paginationState.sort !== fieldName) {
      return faSort;
    }
    return paginationState.order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="customer-heading" data-cy="CustomerHeading">
        <Translate contentKey="gatewayApp.customer.home.title">Customers</Translate>
        {hasAnyAuthority(authorities, [Authority.ADMIN]) && (
          <div className="d-flex justify-content-end">
            <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
              <FontAwesomeIcon icon="sync" spin={loading} />{' '}
              <Translate contentKey="gatewayApp.customer.home.refreshListLabel">List</Translate>
            </Button>
            <Link to="/customer/new" className="btn btn-primary jh-create-entity" data-cy="entityCreateButton">
              <FontAwesomeIcon icon="plus" />
              &nbsp;
              <Translate contentKey="gatewayApp.customer.home.createLabel">Create new Customer</Translate>
            </Link>
          </div>
        )}
      </h2>

      {hasAnyAuthority(authorities, [Authority.ADMIN]) ? (
        <div className="table-responsive">
          {customerList && customerList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    <Translate contentKey="gatewayApp.customer.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                  </th>
                  <th className="hand" onClick={sort('firstName')}>
                    <Translate contentKey="gatewayApp.customer.firstName">First Name</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('firstName')} />
                  </th>
                  <th className="hand" onClick={sort('lastName')}>
                    <Translate contentKey="gatewayApp.customer.lastName">Last Name</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('lastName')} />
                  </th>
                  <th className="hand" onClick={sort('phoneNumber')}>
                    <Translate contentKey="gatewayApp.customer.phoneNumber">Phone Number</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('phoneNumber')} />
                  </th>
                  <th className="hand" onClick={sort('city')}>
                    <Translate contentKey="gatewayApp.customer.city">City</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('city')} />
                  </th>
                  <th className="hand" onClick={sort('userId')}>
                    <Translate contentKey="gatewayApp.customer.userId">User Id</Translate>{' '}
                    <FontAwesomeIcon icon={getSortIconByFieldName('userId')} />
                  </th>
                  <th>
                    <Translate contentKey="gatewayApp.customer.user">User</Translate> <FontAwesomeIcon icon="sort" />
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
                    <td>{customer.phoneNumber}</td>
                    <td>{customer.city}</td>
                    <td>{customer.userId}</td>
                    <td>{customer.user ? customer.user.login : ''}</td>
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
                <Translate contentKey="gatewayApp.customer.home.notFound">No Customers found</Translate>
              </div>
            )
          )}
          {totalItems ? (
            <div className={customerList && customerList.length > 0 ? '' : 'd-none'}>
              <div className="justify-content-center d-flex">
                <JhiItemCount
                  page={paginationState.activePage}
                  total={totalItems}
                  itemsPerPage={paginationState.itemsPerPage}
                  i18nEnabled
                />
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
      ) : (
        // non-admin users: show a single customer detail
        <CustomerDetail />
      )}
    </div>
  );
};

export default Customer;
