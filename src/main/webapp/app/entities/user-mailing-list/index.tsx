import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import UserMailingList from './user-mailing-list';
import UserMailingListDetail from './user-mailing-list-detail';
import UserMailingListUpdate from './user-mailing-list-update';
import UserMailingListDeleteDialog from './user-mailing-list-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={UserMailingListUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={UserMailingListUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={UserMailingListDetail} />
      <ErrorBoundaryRoute path={match.url} component={UserMailingList} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={UserMailingListDeleteDialog} />
  </>
);

export default Routes;
