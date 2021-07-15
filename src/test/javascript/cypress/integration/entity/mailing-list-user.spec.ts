import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('MailingListUser e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/mailing-list-users*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('mailing-list-user');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load MailingListUsers', () => {
    cy.intercept('GET', '/api/mailing-list-users*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('mailing-list-user');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('MailingListUser').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details MailingListUser page', () => {
    cy.intercept('GET', '/api/mailing-list-users*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('mailing-list-user');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('mailingListUser');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create MailingListUser page', () => {
    cy.intercept('GET', '/api/mailing-list-users*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('mailing-list-user');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('MailingListUser');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit MailingListUser page', () => {
    cy.intercept('GET', '/api/mailing-list-users*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('mailing-list-user');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('MailingListUser');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should create an instance of MailingListUser', () => {
    cy.intercept('GET', '/api/mailing-list-users*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('mailing-list-user');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('MailingListUser');

    cy.get(`[data-cy="username"]`)
      .type('Coordinator Kyat Islands', { force: true })
      .invoke('val')
      .should('match', new RegExp('Coordinator Kyat Islands'));

    cy.get(`[data-cy="lastName"]`).type('Littel', { force: true }).invoke('val').should('match', new RegExp('Littel'));

    cy.get(`[data-cy="firstName"]`).type('Johathan', { force: true }).invoke('val').should('match', new RegExp('Johathan'));

    cy.get(`[data-cy="status"]`).select('Verified');

    cy.get(`[data-cy="timeCreated"]`).type('2021-07-12T09:24').invoke('val').should('equal', '2021-07-12T09:24');

    cy.get(`[data-cy="timeModified"]`).type('2021-07-12T19:09').invoke('val').should('equal', '2021-07-12T19:09');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/mailing-list-users*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('mailing-list-user');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });

  it('should delete last instance of MailingListUser', () => {
    cy.intercept('GET', '/api/mailing-list-users*').as('entitiesRequest');
    cy.intercept('GET', '/api/mailing-list-users/*').as('dialogDeleteRequest');
    cy.intercept('DELETE', '/api/mailing-list-users/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('mailing-list-user');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('mailingListUser').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/mailing-list-users*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('mailing-list-user');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
});
