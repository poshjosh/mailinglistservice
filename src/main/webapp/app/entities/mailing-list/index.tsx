import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import MailingList from './mailing-list';
import MailingListDetail from './mailing-list-detail';
import MailingListUpdate from './mailing-list-update';
import MailingListDeleteDialog from './mailing-list-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={MailingListUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={MailingListUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={MailingListDetail} />
      <ErrorBoundaryRoute path={match.url} component={MailingList} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={MailingListDeleteDialog} />
  </>
);

export default Routes;
