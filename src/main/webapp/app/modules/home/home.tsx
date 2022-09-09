import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Row, Col, Alert } from 'reactstrap';

import { useAppSelector } from 'app/config/store';
import PrivateRoute from 'app/shared/auth/private-route';
import Settings from 'app/modules/account/settings/settings';
import { AUTHORITIES } from 'app/config/constants';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <Row>
      <Col md="9">
        {account?.login && (
          <h2>
            <Translate contentKey="home.title" interpolate={{ username: account.login }}>
              Welcome {account.login}!
            </Translate>
          </h2>
        )}
      </Col>
    </Row>
  );
};

export default Home;
