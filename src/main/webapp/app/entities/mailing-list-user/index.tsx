import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import MailingListUser from './mailing-list-user';
import MailingListUserDetail from './mailing-list-user-detail';
import MailingListUserUpdate from './mailing-list-user-update';
import MailingListUserDeleteDialog from './mailing-list-user-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={MailingListUserUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={MailingListUserUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={MailingListUserDetail} />
      <ErrorBoundaryRoute path={match.url} component={MailingListUser} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={MailingListUserDeleteDialog} />
  </>
);

export default Routes;
