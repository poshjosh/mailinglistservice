import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './mailing-list-user.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IMailingListUserDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const MailingListUserDetail = (props: IMailingListUserDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { mailingListUserEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="mailingListUserDetailsHeading">
          <Translate contentKey="mailinglistserviceApp.mailingListUser.detail.title">MailingListUser</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="mailinglistserviceApp.mailingListUser.id">Id</Translate>
            </span>
          </dt>
          <dd>{mailingListUserEntity.id}</dd>
          <dt>
            <span id="username">
              <Translate contentKey="mailinglistserviceApp.mailingListUser.username">Username</Translate>
            </span>
          </dt>
          <dd>{mailingListUserEntity.username}</dd>
          <dt>
            <span id="emailAddress">
              <Translate contentKey="mailinglistserviceApp.mailingListUser.emailAddress">Email Address</Translate>
            </span>
          </dt>
          <dd>{mailingListUserEntity.emailAddress}</dd>
          <dt>
            <span id="lastName">
              <Translate contentKey="mailinglistserviceApp.mailingListUser.lastName">Last Name</Translate>
            </span>
          </dt>
          <dd>{mailingListUserEntity.lastName}</dd>
          <dt>
            <span id="firstName">
              <Translate contentKey="mailinglistserviceApp.mailingListUser.firstName">First Name</Translate>
            </span>
          </dt>
          <dd>{mailingListUserEntity.firstName}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="mailinglistserviceApp.mailingListUser.status">Status</Translate>
            </span>
          </dt>
          <dd>{mailingListUserEntity.status}</dd>
          <dt>
            <span id="timeCreated">
              <Translate contentKey="mailinglistserviceApp.mailingListUser.timeCreated">Time Created</Translate>
            </span>
          </dt>
          <dd>
            {mailingListUserEntity.timeCreated ? (
              <TextFormat value={mailingListUserEntity.timeCreated} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="timeModified">
              <Translate contentKey="mailinglistserviceApp.mailingListUser.timeModified">Time Modified</Translate>
            </span>
          </dt>
          <dd>
            {mailingListUserEntity.timeModified ? (
              <TextFormat value={mailingListUserEntity.timeModified} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/mailing-list-user" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/mailing-list-user/${mailingListUserEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ mailingListUser }: IRootState) => ({
  mailingListUserEntity: mailingListUser.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(MailingListUserDetail);
