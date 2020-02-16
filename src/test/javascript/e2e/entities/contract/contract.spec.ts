import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ContractComponentsPage, ContractDeleteDialog, ContractUpdatePage } from './contract.page-object';

const expect = chai.expect;

describe('Contract e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let contractComponentsPage: ContractComponentsPage;
  let contractUpdatePage: ContractUpdatePage;
  let contractDeleteDialog: ContractDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Contracts', async () => {
    await navBarPage.goToEntity('contract');
    contractComponentsPage = new ContractComponentsPage();
    await browser.wait(ec.visibilityOf(contractComponentsPage.title), 5000);
    expect(await contractComponentsPage.getTitle()).to.eq('bizioApp.contract.home.title');
    await browser.wait(ec.or(ec.visibilityOf(contractComponentsPage.entities), ec.visibilityOf(contractComponentsPage.noResult)), 1000);
  });

  it('should load create Contract page', async () => {
    await contractComponentsPage.clickOnCreateButton();
    contractUpdatePage = new ContractUpdatePage();
    expect(await contractUpdatePage.getPageTitle()).to.eq('bizioApp.contract.home.createOrEditLabel');
    await contractUpdatePage.cancel();
  });

  it('should create and save Contracts', async () => {
    const nbButtonsBeforeCreate = await contractComponentsPage.countDeleteButtons();

    await contractComponentsPage.clickOnCreateButton();

    await promise.all([contractUpdatePage.setIdentifierInput('identifier'), contractUpdatePage.clientSelectLastOption()]);

    expect(await contractUpdatePage.getIdentifierInput()).to.eq('identifier', 'Expected Identifier value to be equals to identifier');

    await contractUpdatePage.save();
    expect(await contractUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await contractComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Contract', async () => {
    const nbButtonsBeforeDelete = await contractComponentsPage.countDeleteButtons();
    await contractComponentsPage.clickOnLastDeleteButton();

    contractDeleteDialog = new ContractDeleteDialog();
    expect(await contractDeleteDialog.getDialogTitle()).to.eq('bizioApp.contract.delete.question');
    await contractDeleteDialog.clickOnConfirmButton();

    expect(await contractComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
