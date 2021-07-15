import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './mailing-list.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IMailingListDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const MailingListDetail = (props: IMailingListDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { mailingListEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="mailingListDetailsHeading">
          <Translate contentKey="mailinglistserviceApp.mailingList.detail.title">MailingList</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="mailinglistserviceApp.mailingList.id">Id</Translate>
            </span>
          </dt>
          <dd>{mailingListEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="mailinglistserviceApp.mailingList.name">Name</Translate>
            </span>
          </dt>
          <dd>{mailingListEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="mailinglistserviceApp.mailingList.description">Description</Translate>
            </span>
          </dt>
          <dd>{mailingListEntity.description}</dd>
          <dt>
            <span id="timeCreated">
              <Translate contentKey="mailinglistserviceApp.mailingList.timeCreated">Time Created</Translate>
            </span>
          </dt>
          <dd>
            {mailingListEntity.timeCreated ? (
              <TextFormat value={mailingListEntity.timeCreated} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="timeModified">
              <Translate contentKey="mailinglistserviceApp.mailingList.timeModified">Time Modified</Translate>
            </span>
          </dt>
          <dd>
            {mailingListEntity.timeModified ? (
              <TextFormat value={mailingListEntity.timeModified} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/mailing-list" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/mailing-list/${mailingListEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ mailingList }: IRootState) => ({
  mailingListEntity: mailingList.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(MailingListDetail);
