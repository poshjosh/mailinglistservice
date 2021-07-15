import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IMailingListUser } from 'app/shared/model/mailing-list-user.model';
import { getEntities as getMailingListUsers } from 'app/entities/mailing-list-user/mailing-list-user.reducer';
import { IMailingList } from 'app/shared/model/mailing-list.model';
import { getEntities as getMailingLists } from 'app/entities/mailing-list/mailing-list.reducer';
import { getEntity, updateEntity, createEntity, reset } from './user-mailing-list.reducer';
import { IUserMailingList } from 'app/shared/model/user-mailing-list.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IUserMailingListUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const UserMailingListUpdate = (props: IUserMailingListUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { userMailingListEntity, mailingListUsers, mailingLists, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/user-mailing-list' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getMailingListUsers();
    props.getMailingLists();
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
        ...userMailingListEntity,
        ...values,
        user: mailingListUsers.find(it => it.id.toString() === values.userId.toString()),
        mailingList: mailingLists.find(it => it.id.toString() === values.mailingListId.toString()),
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
          <h2 id="mailinglistserviceApp.userMailingList.home.createOrEditLabel" data-cy="UserMailingListCreateUpdateHeading">
            <Translate contentKey="mailinglistserviceApp.userMailingList.home.createOrEditLabel">
              Create or edit a UserMailingList
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : userMailingListEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="user-mailing-list-id">
                    <Translate contentKey="mailinglistserviceApp.userMailingList.id">Id</Translate>
                  </Label>
                  <AvInput id="user-mailing-list-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="statusLabel" for="user-mailing-list-status">
                  <Translate contentKey="mailinglistserviceApp.userMailingList.status">Status</Translate>
                </Label>
                <AvInput
                  id="user-mailing-list-status"
                  data-cy="status"
                  type="select"
                  className="form-control"
                  name="status"
                  value={(!isNew && userMailingListEntity.status) || 'Unverified'}
                >
                  <option value="Unverified">{translate('mailinglistserviceApp.MailingListUserStatus.Unverified')}</option>
                  <option value="Verified">{translate('mailinglistserviceApp.MailingListUserStatus.Verified')}</option>
                  <option value="Unsubscribed">{translate('mailinglistserviceApp.MailingListUserStatus.Unsubscribed')}</option>
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label id="timeCreatedLabel" for="user-mailing-list-timeCreated">
                  <Translate contentKey="mailinglistserviceApp.userMailingList.timeCreated">Time Created</Translate>
                </Label>
                <AvInput
                  id="user-mailing-list-timeCreated"
                  data-cy="timeCreated"
                  type="datetime-local"
                  className="form-control"
                  name="timeCreated"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.userMailingListEntity.timeCreated)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="timeModifiedLabel" for="user-mailing-list-timeModified">
                  <Translate contentKey="mailinglistserviceApp.userMailingList.timeModified">Time Modified</Translate>
                </Label>
                <AvInput
                  id="user-mailing-list-timeModified"
                  data-cy="timeModified"
                  type="datetime-local"
                  className="form-control"
                  name="timeModified"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.userMailingListEntity.timeModified)}
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="user-mailing-list-user">
                  <Translate contentKey="mailinglistserviceApp.userMailingList.user">User</Translate>
                </Label>
                <AvInput id="user-mailing-list-user" data-cy="user" type="select" className="form-control" name="userId" required>
                  <option value="" key="0" />
                  {mailingListUsers
                    ? mailingListUsers.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
                <AvFeedback>
                  <Translate contentKey="entity.validation.required">This field is required.</Translate>
                </AvFeedback>
              </AvGroup>
              <AvGroup>
                <Label for="user-mailing-list-mailingList">
                  <Translate contentKey="mailinglistserviceApp.userMailingList.mailingList">Mailing List</Translate>
                </Label>
                <AvInput
                  id="user-mailing-list-mailingList"
                  data-cy="mailingList"
                  type="select"
                  className="form-control"
                  name="mailingListId"
                  required
                >
                  <option value="" key="0" />
                  {mailingLists
                    ? mailingLists.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
                <AvFeedback>
                  <Translate contentKey="entity.validation.required">This field is required.</Translate>
                </AvFeedback>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/user-mailing-list" replace color="info">
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
  mailingListUsers: storeState.mailingListUser.entities,
  mailingLists: storeState.mailingList.entities,
  userMailingListEntity: storeState.userMailingList.entity,
  loading: storeState.userMailingList.loading,
  updating: storeState.userMailingList.updating,
  updateSuccess: storeState.userMailingList.updateSuccess,
});

const mapDispatchToProps = {
  getMailingListUsers,
  getMailingLists,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(UserMailingListUpdate);
