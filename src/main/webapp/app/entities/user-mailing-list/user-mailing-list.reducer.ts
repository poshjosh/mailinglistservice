import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IUserMailingList, defaultValue } from 'app/shared/model/user-mailing-list.model';

export const ACTION_TYPES = {
  FETCH_USERMAILINGLIST_LIST: 'userMailingList/FETCH_USERMAILINGLIST_LIST',
  FETCH_USERMAILINGLIST: 'userMailingList/FETCH_USERMAILINGLIST',
  CREATE_USERMAILINGLIST: 'userMailingList/CREATE_USERMAILINGLIST',
  UPDATE_USERMAILINGLIST: 'userMailingList/UPDATE_USERMAILINGLIST',
  PARTIAL_UPDATE_USERMAILINGLIST: 'userMailingList/PARTIAL_UPDATE_USERMAILINGLIST',
  DELETE_USERMAILINGLIST: 'userMailingList/DELETE_USERMAILINGLIST',
  RESET: 'userMailingList/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IUserMailingList>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type UserMailingListState = Readonly<typeof initialState>;

// Reducer

export default (state: UserMailingListState = initialState, action): UserMailingListState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_USERMAILINGLIST_LIST):
    case REQUEST(ACTION_TYPES.FETCH_USERMAILINGLIST):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_USERMAILINGLIST):
    case REQUEST(ACTION_TYPES.UPDATE_USERMAILINGLIST):
    case REQUEST(ACTION_TYPES.DELETE_USERMAILINGLIST):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_USERMAILINGLIST):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_USERMAILINGLIST_LIST):
    case FAILURE(ACTION_TYPES.FETCH_USERMAILINGLIST):
    case FAILURE(ACTION_TYPES.CREATE_USERMAILINGLIST):
    case FAILURE(ACTION_TYPES.UPDATE_USERMAILINGLIST):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_USERMAILINGLIST):
    case FAILURE(ACTION_TYPES.DELETE_USERMAILINGLIST):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_USERMAILINGLIST_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_USERMAILINGLIST):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_USERMAILINGLIST):
    case SUCCESS(ACTION_TYPES.UPDATE_USERMAILINGLIST):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_USERMAILINGLIST):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_USERMAILINGLIST):
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

const apiUrl = 'api/user-mailing-lists';

// Actions

export const getEntities: ICrudGetAllAction<IUserMailingList> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_USERMAILINGLIST_LIST,
    payload: axios.get<IUserMailingList>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IUserMailingList> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_USERMAILINGLIST,
    payload: axios.get<IUserMailingList>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IUserMailingList> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_USERMAILINGLIST,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IUserMailingList> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_USERMAILINGLIST,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IUserMailingList> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_USERMAILINGLIST,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IUserMailingList> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_USERMAILINGLIST,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
