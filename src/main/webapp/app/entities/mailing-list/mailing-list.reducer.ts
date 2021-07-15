import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IMailingList, defaultValue } from 'app/shared/model/mailing-list.model';

export const ACTION_TYPES = {
  FETCH_MAILINGLIST_LIST: 'mailingList/FETCH_MAILINGLIST_LIST',
  FETCH_MAILINGLIST: 'mailingList/FETCH_MAILINGLIST',
  CREATE_MAILINGLIST: 'mailingList/CREATE_MAILINGLIST',
  UPDATE_MAILINGLIST: 'mailingList/UPDATE_MAILINGLIST',
  PARTIAL_UPDATE_MAILINGLIST: 'mailingList/PARTIAL_UPDATE_MAILINGLIST',
  DELETE_MAILINGLIST: 'mailingList/DELETE_MAILINGLIST',
  RESET: 'mailingList/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IMailingList>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type MailingListState = Readonly<typeof initialState>;

// Reducer

export default (state: MailingListState = initialState, action): MailingListState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_MAILINGLIST_LIST):
    case REQUEST(ACTION_TYPES.FETCH_MAILINGLIST):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_MAILINGLIST):
    case REQUEST(ACTION_TYPES.UPDATE_MAILINGLIST):
    case REQUEST(ACTION_TYPES.DELETE_MAILINGLIST):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_MAILINGLIST):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_MAILINGLIST_LIST):
    case FAILURE(ACTION_TYPES.FETCH_MAILINGLIST):
    case FAILURE(ACTION_TYPES.CREATE_MAILINGLIST):
    case FAILURE(ACTION_TYPES.UPDATE_MAILINGLIST):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_MAILINGLIST):
    case FAILURE(ACTION_TYPES.DELETE_MAILINGLIST):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_MAILINGLIST_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_MAILINGLIST):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_MAILINGLIST):
    case SUCCESS(ACTION_TYPES.UPDATE_MAILINGLIST):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_MAILINGLIST):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_MAILINGLIST):
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

const apiUrl = 'api/mailing-lists';

// Actions

export const getEntities: ICrudGetAllAction<IMailingList> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_MAILINGLIST_LIST,
    payload: axios.get<IMailingList>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IMailingList> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_MAILINGLIST,
    payload: axios.get<IMailingList>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IMailingList> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_MAILINGLIST,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IMailingList> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_MAILINGLIST,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IMailingList> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_MAILINGLIST,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IMailingList> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_MAILINGLIST,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
