import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IMailingListUser, defaultValue } from 'app/shared/model/mailing-list-user.model';

export const ACTION_TYPES = {
  FETCH_MAILINGLISTUSER_LIST: 'mailingListUser/FETCH_MAILINGLISTUSER_LIST',
  FETCH_MAILINGLISTUSER: 'mailingListUser/FETCH_MAILINGLISTUSER',
  CREATE_MAILINGLISTUSER: 'mailingListUser/CREATE_MAILINGLISTUSER',
  UPDATE_MAILINGLISTUSER: 'mailingListUser/UPDATE_MAILINGLISTUSER',
  PARTIAL_UPDATE_MAILINGLISTUSER: 'mailingListUser/PARTIAL_UPDATE_MAILINGLISTUSER',
  DELETE_MAILINGLISTUSER: 'mailingListUser/DELETE_MAILINGLISTUSER',
  RESET: 'mailingListUser/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IMailingListUser>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type MailingListUserState = Readonly<typeof initialState>;

// Reducer

export default (state: MailingListUserState = initialState, action): MailingListUserState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_MAILINGLISTUSER_LIST):
    case REQUEST(ACTION_TYPES.FETCH_MAILINGLISTUSER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_MAILINGLISTUSER):
    case REQUEST(ACTION_TYPES.UPDATE_MAILINGLISTUSER):
    case REQUEST(ACTION_TYPES.DELETE_MAILINGLISTUSER):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_MAILINGLISTUSER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_MAILINGLISTUSER_LIST):
    case FAILURE(ACTION_TYPES.FETCH_MAILINGLISTUSER):
    case FAILURE(ACTION_TYPES.CREATE_MAILINGLISTUSER):
    case FAILURE(ACTION_TYPES.UPDATE_MAILINGLISTUSER):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_MAILINGLISTUSER):
    case FAILURE(ACTION_TYPES.DELETE_MAILINGLISTUSER):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_MAILINGLISTUSER_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_MAILINGLISTUSER):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_MAILINGLISTUSER):
    case SUCCESS(ACTION_TYPES.UPDATE_MAILINGLISTUSER):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_MAILINGLISTUSER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_MAILINGLISTUSER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/mailing-list-users';

// Actions

export const getEntities: ICrudGetAllAction<IMailingListUser> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_MAILINGLISTUSER_LIST,
    payload: axios.get<IMailingListUser>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IMailingListUser> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_MAILINGLISTUSER,
    payload: axios.get<IMailingListUser>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IMailingListUser> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_MAILINGLISTUSER,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IMailingListUser> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_MAILINGLISTUSER,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IMailingListUser> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_MAILINGLISTUSER,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IMailingListUser> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_MAILINGLISTUSER,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
