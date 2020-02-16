import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { InventoryItemComponentsPage, InventoryItemDeleteDialog, InventoryItemUpdatePage } from './inventory-item.page-object';

const expect = chai.expect;

describe('InventoryItem e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let inventoryItemComponentsPage: InventoryItemComponentsPage;
  let inventoryItemUpdatePage: InventoryItemUpdatePage;
  let inventoryItemDeleteDialog: InventoryItemDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load InventoryItems', async () => {
    await navBarPage.goToEntity('inventory-item');
    inventoryItemComponentsPage = new InventoryItemComponentsPage();
    await browser.wait(ec.visibilityOf(inventoryItemComponentsPage.title), 5000);
    expect(await inventoryItemComponentsPage.getTitle()).to.eq('bizioApp.inventoryItem.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(inventoryItemComponentsPage.entities), ec.visibilityOf(inventoryItemComponentsPage.noResult)),
      1000
    );
  });

  it('should load create InventoryItem page', async () => {
    await inventoryItemComponentsPage.clickOnCreateButton();
    inventoryItemUpdatePage = new InventoryItemUpdatePage();
    expect(await inventoryItemUpdatePage.getPageTitle()).to.eq('bizioApp.inventoryItem.home.createOrEditLabel');
    await inventoryItemUpdatePage.cancel();
  });

  it('should create and save InventoryItems', async () => {
    const nbButtonsBeforeCreate = await inventoryItemComponentsPage.countDeleteButtons();

    await inventoryItemComponentsPage.clickOnCreateButton();

    await promise.all([inventoryItemUpdatePage.setNameInput('name'), inventoryItemUpdatePage.setValueInput('5')]);

    expect(await inventoryItemUpdatePage.getNameInput()).to.eq('name', 'Expected Name value to be equals to name');
    expect(await inventoryItemUpdatePage.getValueInput()).to.eq('5', 'Expected value value to be equals to 5');

    await inventoryItemUpdatePage.save();
    expect(await inventoryItemUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await inventoryItemComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last InventoryItem', async () => {
    const nbButtonsBeforeDelete = await inventoryItemComponentsPage.countDeleteButtons();
    await inventoryItemComponentsPage.clickOnLastDeleteButton();

    inventoryItemDeleteDialog = new InventoryItemDeleteDialog();
    expect(await inventoryItemDeleteDialog.getDialogTitle()).to.eq('bizioApp.inventoryItem.delete.question');
    await inventoryItemDeleteDialog.clickOnConfirmButton();

    expect(await inventoryItemComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
