import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './vaccine.reducer';

export const VaccineDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const vaccineEntity = useAppSelector(state => state.vaccine.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="vaccineDetailsHeading">
          <Translate contentKey="employeeVaccineInventoryApp.vaccine.detail.title">Vaccine</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{vaccineEntity.id}</dd>
          <dt>
            <span id="vaccineType">
              <Translate contentKey="employeeVaccineInventoryApp.vaccine.vaccineType">Vaccine Type</Translate>
            </span>
          </dt>
          <dd>{vaccineEntity.vaccineType}</dd>
          <dt>
            <span id="vaccinationDate">
              <Translate contentKey="employeeVaccineInventoryApp.vaccine.vaccinationDate">Vaccination Date</Translate>
            </span>
          </dt>
          <dd>
            {vaccineEntity.vaccinationDate ? (
              <TextFormat value={vaccineEntity.vaccinationDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="doses">
              <Translate contentKey="employeeVaccineInventoryApp.vaccine.doses">Doses</Translate>
            </span>
          </dt>
          <dd>{vaccineEntity.doses}</dd>
          <dt>
            <Translate contentKey="employeeVaccineInventoryApp.vaccine.applicationUser">Application User</Translate>
          </dt>
          <dd>{vaccineEntity.applicationUser ? vaccineEntity.applicationUser.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/vaccine" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/vaccine/${vaccineEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default VaccineDetail;
