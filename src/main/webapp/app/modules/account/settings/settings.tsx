import React, { useEffect } from 'react';
import { Button, Col, Row } from 'reactstrap';
import { Translate, translate, ValidatedField, ValidatedForm, isEmail } from 'react-jhipster';
import { toast } from 'react-toastify';

import { locales, languages } from 'app/config/translation';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getSession } from 'app/shared/reducers/authentication';
import { saveAccountSettings, reset } from './settings.reducer';

export const SettingsPage = () => {
  const dispatch = useAppDispatch();
  const account = useAppSelector(state => state.authentication.account);
  const successMessage = useAppSelector(state => state.settings.successMessage);

  useEffect(() => {
    dispatch(getSession());
    return () => {
      dispatch(reset());
    };
  }, []);

  useEffect(() => {
    if (successMessage) {
      toast.success(translate(successMessage));
    }
  }, [successMessage]);

  const handleValidSubmit = values => {
    dispatch(
      saveAccountSettings({
        ...account,
        ...values,
      })
    );
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="settings-title">
            <Translate contentKey="settings.title" interpolate={{ username: account.login }}>
              User settings for {account.login}
            </Translate>
          </h2>
          <ValidatedForm id="settings-form" onSubmit={handleValidSubmit} defaultValues={account}>
            <ValidatedField
              type="number"
              name="identification"
              label={translate('userManagement.identification')}
              validate={{
                required: {
                  value: true,
                  message: translate('register.messages.validate.identification.required'),
                },
                maxLength: {
                  value: 10,
                  message: translate('entity.validation.maxlength', { max: 10 }),
                },
                minLength: {
                  value: 10,
                  message: translate('entity.validation.minlength', { min: 10 }),
                },
              }}
            />
            <ValidatedField
              name="firstName"
              label={translate('settings.form.firstname')}
              id="firstName"
              placeholder={translate('settings.form.firstname.placeholder')}
              validate={{
                required: { value: true, message: translate('settings.messages.validate.firstname.required') },
                minLength: { value: 1, message: translate('settings.messages.validate.firstname.minlength') },
                maxLength: { value: 50, message: translate('settings.messages.validate.firstname.maxlength') },
              }}
              data-cy="firstname"
            />
            <ValidatedField
              name="lastName"
              label={translate('settings.form.lastname')}
              id="lastName"
              placeholder={translate('settings.form.lastname.placeholder')}
              validate={{
                required: { value: true, message: translate('settings.messages.validate.lastname.required') },
                minLength: { value: 1, message: translate('settings.messages.validate.lastname.minlength') },
                maxLength: { value: 50, message: translate('settings.messages.validate.lastname.maxlength') },
              }}
              data-cy="lastname"
            />
            <ValidatedField
              name="email"
              label={translate('global.form.email.label')}
              placeholder={translate('global.form.email.placeholder')}
              type="email"
              validate={{
                required: { value: true, message: translate('global.messages.validate.email.required') },
                minLength: { value: 5, message: translate('global.messages.validate.email.minlength') },
                maxLength: { value: 254, message: translate('global.messages.validate.email.maxlength') },
                validate: v => isEmail(v) || translate('global.messages.validate.email.invalid'),
              }}
              data-cy="email"
            />
            <ValidatedField
              label={translate('employeeVaccineInventoryApp.applicationUser.birthday')}
              id="application-user-birthday"
              name="dayOfBirth"
              data-cy="dayOfBirth"
              type="date"
              validate={{
                required: { value: true, message: translate('global.messages.validate.dayOfBirth.required') },
              }}
            />
            <ValidatedField
              label={translate('employeeVaccineInventoryApp.applicationUser.address')}
              id="application-user-address"
              name="address"
              data-cy="address"
              type="text"
              validate={{
                required: { value: true, message: translate('global.messages.validate.address.required') },
              }}
            />
            <ValidatedField
              label={translate('employeeVaccineInventoryApp.applicationUser.cellphone')}
              id="application-user-cellphone"
              name="mobile"
              data-cy="mobile"
              type="text"
              validate={{
                required: { value: true, message: translate('global.messages.validate.mobile.required') },
                minLength: { value: 10, message: translate('entity.validation.minlength', { min: 10 }) },
                maxLength: { value: 10, message: translate('entity.validation.maxlength', { max: 10 }) },
              }}
            />
            <p>
              <Translate
                contentKey="settings.state"
                interpolate={{ status: account.vaccines && account.vaccines.length > 0 ? 'VACUNADO' : 'NO VACUNADO' }}
              >
                Status
              </Translate>
            </p>
            <Button color="primary" type="submit" data-cy="submit">
              <Translate contentKey="settings.form.button">Save</Translate>
            </Button>
          </ValidatedForm>
        </Col>
      </Row>
    </div>
  );
};

export default SettingsPage;
