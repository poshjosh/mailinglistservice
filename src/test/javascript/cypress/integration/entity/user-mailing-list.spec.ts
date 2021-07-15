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

describe('UserMailingList e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/user-mailing-lists*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('user-mailing-list');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load UserMailingLists', () => {
    cy.intercept('GET', '/api/user-mailing-lists*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('user-mailing-list');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('UserMailingList').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details UserMailingList page', () => {
    cy.intercept('GET', '/api/user-mailing-lists*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('user-mailing-list');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('userMailingList');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create UserMailingList page', () => {
    cy.intercept('GET', '/api/user-mailing-lists*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('user-mailing-list');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('UserMailingList');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit UserMailingList page', () => {
    cy.intercept('GET', '/api/user-mailing-lists*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('user-mailing-list');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('UserMailingList');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  /* this test is commented because it contains required relationships
  it('should create an instance of UserMailingList', () => {
    cy.intercept('GET', '/api/user-mailing-lists*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('user-mailing-list');
    cy.wait('@entitiesRequest')
      .then(({ request, response }) => startingEntitiesCount = response.body.length);
    cy.get(entityCreateButtonSelector).click({force: true});
    cy.getEntityCreateUpdateHeading('UserMailingList');

    cy.get(`[data-cy="status"]`).select('Unsubscribed');


    cy.get(`[data-cy="timeCreated"]`).type('2021-07-12T07:07').invoke('val').should('equal', '2021-07-12T07:07');


    cy.get(`[data-cy="timeModified"]`).type('2021-07-12T10:56').invoke('val').should('equal', '2021-07-12T10:56');

    cy.setFieldSelectToLastOfEntity('user');

    cy.setFieldSelectToLastOfEntity('mailingList');

    cy.get(entityCreateSaveButtonSelector).click({force: true});
    cy.scrollTo('top', {ensureScrollable: false});
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/user-mailing-lists*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('user-mailing-list');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });
  */

  /* this test is commented because it contains required relationships
  it('should delete last instance of UserMailingList', () => {
    cy.intercept('GET', '/api/user-mailing-lists*').as('entitiesRequest');
    cy.intercept('GET', '/api/user-mailing-lists/*').as('dialogDeleteRequest');
    cy.intercept('DELETE', '/api/user-mailing-lists/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('user-mailing-list');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({force: true});
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('userMailingList').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({force: true});
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/user-mailing-lists*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('user-mailing-list');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
  */
});
