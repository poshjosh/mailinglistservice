import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './mailing-list-user.reducer';
import { IMailingListUser } from 'app/shared/model/mailing-list-user.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IMailingListUserUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const MailingListUserUpdate = (props: IMailingListUserUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { mailingListUserEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/mailing-list-user' + props.location.search);
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
        ...mailingListUserEntity,
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
          <h2 id="mailinglistserviceApp.mailingListUser.home.createOrEditLabel" data-cy="MailingListUserCreateUpdateHeading">
            <Translate contentKey="mailinglistserviceApp.mailingListUser.home.createOrEditLabel">
              Create or edit a MailingListUser
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : mailingListUserEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="mailing-list-user-id">
                    <Translate contentKey="mailinglistserviceApp.mailingListUser.id">Id</Translate>
                  </Label>
                  <AvInput id="mailing-list-user-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="usernameLabel" for="mailing-list-user-username">
                  <Translate contentKey="mailinglistserviceApp.mailingListUser.username">Username</Translate>
                </Label>
                <AvField
                  id="mailing-list-user-username"
                  data-cy="username"
                  type="text"
                  name="username"
                  validate={{
                    maxLength: { value: 64, errorMessage: translate('entity.validation.maxlength', { max: 64 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="emailAddressLabel" for="mailing-list-user-emailAddress">
                  <Translate contentKey="mailinglistserviceApp.mailingListUser.emailAddress">Email Address</Translate>
                </Label>
                <AvField
                  id="mailing-list-user-emailAddress"
                  data-cy="emailAddress"
                  type="text"
                  name="emailAddress"
                  validate={{
                    maxLength: { value: 191, errorMessage: translate('entity.validation.maxlength', { max: 191 }) },
                    pattern: {
                      value: '^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$',
                      errorMessage: translate('entity.validation.pattern', { pattern: '^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$' }),
                    },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="lastNameLabel" for="mailing-list-user-lastName">
                  <Translate contentKey="mailinglistserviceApp.mailingListUser.lastName">Last Name</Translate>
                </Label>
                <AvField
                  id="mailing-list-user-lastName"
                  data-cy="lastName"
                  type="text"
                  name="lastName"
                  validate={{
                    maxLength: { value: 64, errorMessage: translate('entity.validation.maxlength', { max: 64 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="firstNameLabel" for="mailing-list-user-firstName">
                  <Translate contentKey="mailinglistserviceApp.mailingListUser.firstName">First Name</Translate>
                </Label>
                <AvField
                  id="mailing-list-user-firstName"
                  data-cy="firstName"
                  type="text"
                  name="firstName"
                  validate={{
                    maxLength: { value: 64, errorMessage: translate('entity.validation.maxlength', { max: 64 }) },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="statusLabel" for="mailing-list-user-status">
                  <Translate contentKey="mailinglistserviceApp.mailingListUser.status">Status</Translate>
                </Label>
                <AvInput
                  id="mailing-list-user-status"
                  data-cy="status"
                  type="select"
                  className="form-control"
                  name="status"
                  value={(!isNew && mailingListUserEntity.status) || 'Unverified'}
                >
                  <option value="Unverified">{translate('mailinglistserviceApp.MailingListUserStatus.Unverified')}</option>
                  <option value="Verified">{translate('mailinglistserviceApp.MailingListUserStatus.Verified')}</option>
                  <option value="Unsubscribed">{translate('mailinglistserviceApp.MailingListUserStatus.Unsubscribed')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="timeCreatedLabel" for="mailing-list-user-timeCreated">
                  <Translate contentKey="mailinglistserviceApp.mailingListUser.timeCreated">Time Created</Translate>
                </Label>
                <AvInput
                  id="mailing-list-user-timeCreated"
                  data-cy="timeCreated"
                  type="datetime-local"
                  className="form-control"
                  name="timeCreated"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.mailingListUserEntity.timeCreated)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="timeModifiedLabel" for="mailing-list-user-timeModified">
                  <Translate contentKey="mailinglistserviceApp.mailingListUser.timeModified">Time Modified</Translate>
                </Label>
                <AvInput
                  id="mailing-list-user-timeModified"
                  data-cy="timeModified"
                  type="datetime-local"
                  className="form-control"
                  name="timeModified"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.mailingListUserEntity.timeModified)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/mailing-list-user" replace color="info">
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
  mailingListUserEntity: storeState.mailingListUser.entity,
  loading: storeState.mailingListUser.loading,
  updating: storeState.mailingListUser.updating,
  updateSuccess: storeState.mailingListUser.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(MailingListUserUpdate);
