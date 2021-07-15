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

describe('MailingList e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/mailing-lists*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('mailing-list');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load MailingLists', () => {
    cy.intercept('GET', '/api/mailing-lists*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('mailing-list');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('MailingList').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details MailingList page', () => {
    cy.intercept('GET', '/api/mailing-lists*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('mailing-list');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('mailingList');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create MailingList page', () => {
    cy.intercept('GET', '/api/mailing-lists*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('mailing-list');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('MailingList');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit MailingList page', () => {
    cy.intercept('GET', '/api/mailing-lists*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('mailing-list');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('MailingList');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should create an instance of MailingList', () => {
    cy.intercept('GET', '/api/mailing-lists*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('mailing-list');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('MailingList');

    cy.get(`[data-cy="name"]`)
      .type('integrate Pants Chips', { force: true })
      .invoke('val')
      .should('match', new RegExp('integrate Pants Chips'));

    cy.get(`[data-cy="description"]`)
      .type('Cambridgeshire parse digital', { force: true })
      .invoke('val')
      .should('match', new RegExp('Cambridgeshire parse digital'));

    cy.get(`[data-cy="timeCreated"]`).type('2021-07-12T13:31').invoke('val').should('equal', '2021-07-12T13:31');

    cy.get(`[data-cy="timeModified"]`).type('2021-07-12T02:57').invoke('val').should('equal', '2021-07-12T02:57');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/mailing-lists*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('mailing-list');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });

  it('should delete last instance of MailingList', () => {
    cy.intercept('GET', '/api/mailing-lists*').as('entitiesRequest');
    cy.intercept('GET', '/api/mailing-lists/*').as('dialogDeleteRequest');
    cy.intercept('DELETE', '/api/mailing-lists/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('mailing-list');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('mailingList').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/mailing-lists*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('mailing-list');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
});
