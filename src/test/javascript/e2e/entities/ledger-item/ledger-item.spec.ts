import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { LedgerItemComponentsPage, LedgerItemDeleteDialog, LedgerItemUpdatePage } from './ledger-item.page-object';

const expect = chai.expect;

describe('LedgerItem e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let ledgerItemComponentsPage: LedgerItemComponentsPage;
  let ledgerItemUpdatePage: LedgerItemUpdatePage;
  let ledgerItemDeleteDialog: LedgerItemDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load LedgerItems', async () => {
    await navBarPage.goToEntity('ledger-item');
    ledgerItemComponentsPage = new LedgerItemComponentsPage();
    await browser.wait(ec.visibilityOf(ledgerItemComponentsPage.title), 5000);
    expect(await ledgerItemComponentsPage.getTitle()).to.eq('bizioApp.ledgerItem.home.title');
    await browser.wait(ec.or(ec.visibilityOf(ledgerItemComponentsPage.entities), ec.visibilityOf(ledgerItemComponentsPage.noResult)), 1000);
  });

  it('should load create LedgerItem page', async () => {
    await ledgerItemComponentsPage.clickOnCreateButton();
    ledgerItemUpdatePage = new LedgerItemUpdatePage();
    expect(await ledgerItemUpdatePage.getPageTitle()).to.eq('bizioApp.ledgerItem.home.createOrEditLabel');
    await ledgerItemUpdatePage.cancel();
  });

  it('should create and save LedgerItems', async () => {
    const nbButtonsBeforeCreate = await ledgerItemComponentsPage.countDeleteButtons();

    await ledgerItemComponentsPage.clickOnCreateButton();

    await promise.all([
      ledgerItemUpdatePage.setDateInput('2000-12-31'),
      ledgerItemUpdatePage.setDocumentInput('document'),
      ledgerItemUpdatePage.setExplanationInput('explanation'),
      ledgerItemUpdatePage.typeSelectLastOption(),
      ledgerItemUpdatePage.setValueInput('5'),
      ledgerItemUpdatePage.setYearInput('5'),
      ledgerItemUpdatePage.clientSelectLastOption()
    ]);

    expect(await ledgerItemUpdatePage.getDateInput()).to.eq('2000-12-31', 'Expected date value to be equals to 2000-12-31');
    expect(await ledgerItemUpdatePage.getDocumentInput()).to.eq('document', 'Expected Document value to be equals to document');
    expect(await ledgerItemUpdatePage.getExplanationInput()).to.eq('explanation', 'Expected Explanation value to be equals to explanation');
    expect(await ledgerItemUpdatePage.getValueInput()).to.eq('5', 'Expected value value to be equals to 5');
    expect(await ledgerItemUpdatePage.getYearInput()).to.eq('5', 'Expected year value to be equals to 5');

    await ledgerItemUpdatePage.save();
    expect(await ledgerItemUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await ledgerItemComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last LedgerItem', async () => {
    const nbButtonsBeforeDelete = await ledgerItemComponentsPage.countDeleteButtons();
    await ledgerItemComponentsPage.clickOnLastDeleteButton();

    ledgerItemDeleteDialog = new LedgerItemDeleteDialog();
    expect(await ledgerItemDeleteDialog.getDialogTitle()).to.eq('bizioApp.ledgerItem.delete.question');
    await ledgerItemDeleteDialog.clickOnConfirmButton();

    expect(await ledgerItemComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
