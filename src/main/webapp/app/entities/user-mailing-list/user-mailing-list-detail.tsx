import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './user-mailing-list.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IUserMailingListDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const UserMailingListDetail = (props: IUserMailingListDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { userMailingListEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userMailingListDetailsHeading">
          <Translate contentKey="mailinglistserviceApp.userMailingList.detail.title">UserMailingList</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="mailinglistserviceApp.userMailingList.id">Id</Translate>
            </span>
          </dt>
          <dd>{userMailingListEntity.id}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="mailinglistserviceApp.userMailingList.status">Status</Translate>
            </span>
          </dt>
          <dd>{userMailingListEntity.status}</dd>
          <dt>
            <span id="timeCreated">
              <Translate contentKey="mailinglistserviceApp.userMailingList.timeCreated">Time Created</Translate>
            </span>
          </dt>
          <dd>
            {userMailingListEntity.timeCreated ? (
              <TextFormat value={userMailingListEntity.timeCreated} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="timeModified">
              <Translate contentKey="mailinglistserviceApp.userMailingList.timeModified">Time Modified</Translate>
            </span>
          </dt>
          <dd>
            {userMailingListEntity.timeModified ? (
              <TextFormat value={userMailingListEntity.timeModified} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="mailinglistserviceApp.userMailingList.user">User</Translate>
          </dt>
          <dd>{userMailingListEntity.user ? userMailingListEntity.user.id : ''}</dd>
          <dt>
            <Translate contentKey="mailinglistserviceApp.userMailingList.mailingList">Mailing List</Translate>
          </dt>
          <dd>{userMailingListEntity.mailingList ? userMailingListEntity.mailingList.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/user-mailing-list" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-mailing-list/${userMailingListEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ userMailingList }: IRootState) => ({
  userMailingListEntity: userMailingList.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(UserMailingListDetail);
