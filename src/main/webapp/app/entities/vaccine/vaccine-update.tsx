import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IApplicationUser } from 'app/shared/model/application-user.model';
import { getEntities as getApplicationUsers } from 'app/entities/application-user/application-user.reducer';
import { IVaccine } from 'app/shared/model/vaccine.model';
import { VaccineType } from 'app/shared/model/enumerations/vaccine-type.model';
import { getEntity, updateEntity, createEntity, reset } from './vaccine.reducer';

export const VaccineUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const applicationUsers = useAppSelector(state => state.applicationUser.entities);
  const vaccineEntity = useAppSelector(state => state.vaccine.entity);
  const loading = useAppSelector(state => state.vaccine.loading);
  const updating = useAppSelector(state => state.vaccine.updating);
  const updateSuccess = useAppSelector(state => state.vaccine.updateSuccess);
  const vaccineTypeValues = Object.keys(VaccineType);

  const handleClose = () => {
    navigate('/vaccine');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getApplicationUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...vaccineEntity,
      ...values,
      applicationUser: applicationUsers.find(it => it.id.toString() === values.applicationUser.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          vaccineType: 'SPUTNIK',
          ...vaccineEntity,
          applicationUser: vaccineEntity?.applicationUser?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="employeeVaccineInventoryApp.vaccine.home.createOrEditLabel" data-cy="VaccineCreateUpdateHeading">
            <Translate contentKey="employeeVaccineInventoryApp.vaccine.home.createOrEditLabel">Create or edit a Vaccine</Translate>
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
                  id="vaccine-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('employeeVaccineInventoryApp.vaccine.vaccineType')}
                id="vaccine-vaccineType"
                name="vaccineType"
                data-cy="vaccineType"
                type="select"
              >
                {vaccineTypeValues.map(vaccineType => (
                  <option value={vaccineType} key={vaccineType}>
                    {translate('employeeVaccineInventoryApp.VaccineType.' + vaccineType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('employeeVaccineInventoryApp.vaccine.vaccinationDate')}
                id="vaccine-vaccinationDate"
                name="vaccinationDate"
                data-cy="vaccinationDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('employeeVaccineInventoryApp.vaccine.doses')}
                id="vaccine-doses"
                name="doses"
                data-cy="doses"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                id="vaccine-applicationUser"
                name="applicationUser"
                data-cy="applicationUser"
                label={translate('employeeVaccineInventoryApp.vaccine.applicationUser')}
                type="select"
                required
              >
                <option value="" key="0" />
                {applicationUsers
                  ? applicationUsers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/vaccine" replace color="info">
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

export default VaccineUpdate;
