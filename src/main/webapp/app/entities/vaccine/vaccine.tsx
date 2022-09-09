import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IVaccine } from 'app/shared/model/vaccine.model';
import { getEntities } from './vaccine.reducer';

export const Vaccine = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const vaccineList = useAppSelector(state => state.vaccine.entities);
  const loading = useAppSelector(state => state.vaccine.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="vaccine-heading" data-cy="VaccineHeading">
        <Translate contentKey="employeeVaccineInventoryApp.vaccine.home.title">Vaccines</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="employeeVaccineInventoryApp.vaccine.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/vaccine/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="employeeVaccineInventoryApp.vaccine.home.createLabel">Create new Vaccine</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {vaccineList && vaccineList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="employeeVaccineInventoryApp.vaccine.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="employeeVaccineInventoryApp.vaccine.vaccineType">Vaccine Type</Translate>
                </th>
                <th>
                  <Translate contentKey="employeeVaccineInventoryApp.vaccine.vaccinationDate">Vaccination Date</Translate>
                </th>
                <th>
                  <Translate contentKey="employeeVaccineInventoryApp.vaccine.doses">Doses</Translate>
                </th>
                <th>
                  <Translate contentKey="employeeVaccineInventoryApp.vaccine.applicationUser">Application User</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {vaccineList.map((vaccine, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/vaccine/${vaccine.id}`} color="link" size="sm">
                      {vaccine.id}
                    </Button>
                  </td>
                  <td>
                    <Translate contentKey={`employeeVaccineInventoryApp.VaccineType.${vaccine.vaccineType}`} />
                  </td>
                  <td>
                    {vaccine.vaccinationDate ? (
                      <TextFormat type="date" value={vaccine.vaccinationDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{vaccine.doses}</td>
                  <td>
                    {vaccine.applicationUser ? (
                      <Link to={`/application-user/${vaccine.applicationUser.id}`}>{vaccine.applicationUser.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/vaccine/${vaccine.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/vaccine/${vaccine.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/vaccine/${vaccine.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="employeeVaccineInventoryApp.vaccine.home.notFound">No Vaccines found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Vaccine;
