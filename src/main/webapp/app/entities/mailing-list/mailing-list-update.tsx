import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './mailing-list.reducer';
import { IMailingList } from 'app/shared/model/mailing-list.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IMailingListUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const MailingListUpdate = (props: IMailingListUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { mailingListEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/mailing-list' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.timeCreated = convertDateTimeToServer(values.timeCreated);
    values.timeModified = convertDateTimeToServer(values.timeModified);

    if (errors.length === 0) {
      const entity = {
        ...mailingListEntity,
        ...values,
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="mailinglistserviceApp.mailingList.home.createOrEditLabel" data-cy="MailingListCreateUpdateHeading">
            <Translate contentKey="mailinglistserviceApp.mailingList.home.createOrEditLabel">Create or edit a MailingList</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : mailingListEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="mailing-list-id">
                    <Translate contentKey="mailinglistserviceApp.mailingList.id">Id</Translate>
                  </Label>
                  <AvInput id="mailing-list-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="nameLabel" for="mailing-list-name">
                  <Translate contentKey="mailinglistserviceApp.mailingList.name">Name</Translate>
                </Label>
                <AvField
                  id="mailing-list-name"
                  data-cy="name"
                  type="text"
                  name="name"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 64, errorMessage: translate('entity.validation.maxlength', { max: 64 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="descriptionLabel" for="mailing-list-description">
                  <Translate contentKey="mailinglistserviceApp.mailingList.description">Description</Translate>
                </Label>
                <AvField
                  id="mailing-list-description"
                  data-cy="description"
                  type="text"
                  name="description"
                  validate={{
                    maxLength: { value: 255, errorMessage: translate('entity.validation.maxlength', { max: 255 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="timeCreatedLabel" for="mailing-list-timeCreated">
                  <Translate contentKey="mailinglistserviceApp.mailingList.timeCreated">Time Created</Translate>
                </Label>
                <AvInput
                  id="mailing-list-timeCreated"
                  data-cy="timeCreated"
                  type="datetime-local"
                  className="form-control"
                  name="timeCreated"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.mailingListEntity.timeCreated)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="timeModifiedLabel" for="mailing-list-timeModified">
                  <Translate contentKey="mailinglistserviceApp.mailingList.timeModified">Time Modified</Translate>
                </Label>
                <AvInput
                  id="mailing-list-timeModified"
                  data-cy="timeModified"
                  type="datetime-local"
                  className="form-control"
                  name="timeModified"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.mailingListEntity.timeModified)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/mailing-list" replace color="info">
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
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  mailingListEntity: storeState.mailingList.entity,
  loading: storeState.mailingList.loading,
  updating: storeState.mailingList.updating,
  updateSuccess: storeState.mailingList.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(MailingListUpdate);
