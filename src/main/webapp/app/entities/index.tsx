import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import MailingList from './mailing-list';
import MailingListUser from './mailing-list-user';
import UserMailingList from './user-mailing-list';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}mailing-list`} component={MailingList} />
      <ErrorBoundaryRoute path={`${match.url}mailing-list-user`} component={MailingListUser} />
      <ErrorBoundaryRoute path={`${match.url}user-mailing-list`} component={UserMailingList} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
