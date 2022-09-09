import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './application-user.reducer';

export const ApplicationUserDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const applicationUserEntity = useAppSelector(state => state.applicationUser.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="applicationUserDetailsHeading">
          <Translate contentKey="employeeVaccineInventoryApp.applicationUser.detail.title">ApplicationUser</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{applicationUserEntity.id}</dd>
          <dt>
            <span id="identification">
              <Translate contentKey="employeeVaccineInventoryApp.applicationUser.identification">Identification</Translate>
            </span>
          </dt>
          <dd>{applicationUserEntity.identification}</dd>
          <dt>
            <span id="birthday">
              <Translate contentKey="employeeVaccineInventoryApp.applicationUser.birthday">Birthday</Translate>
            </span>
          </dt>
          <dd>
            {applicationUserEntity.birthday ? (
              <TextFormat value={applicationUserEntity.birthday} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="address">
              <Translate contentKey="employeeVaccineInventoryApp.applicationUser.address">Address</Translate>
            </span>
          </dt>
          <dd>{applicationUserEntity.address}</dd>
          <dt>
            <span id="cellphone">
              <Translate contentKey="employeeVaccineInventoryApp.applicationUser.cellphone">Cellphone</Translate>
            </span>
          </dt>
          <dd>{applicationUserEntity.cellphone}</dd>
          <dt>
            <Translate contentKey="employeeVaccineInventoryApp.applicationUser.internalUser">Internal User</Translate>
          </dt>
          <dd>{applicationUserEntity.internalUser ? applicationUserEntity.internalUser.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/application-user" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/application-user/${applicationUserEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ApplicationUserDetail;
