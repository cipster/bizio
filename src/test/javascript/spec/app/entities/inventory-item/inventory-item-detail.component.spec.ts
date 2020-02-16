import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BizioTestModule } from '../../../test.module';
import { InventoryItemDetailComponent } from 'app/entities/inventory-item/inventory-item-detail.component';
import { InventoryItem } from 'app/shared/model/inventory-item.model';

describe('Component Tests', () => {
  describe('InventoryItem Management Detail Component', () => {
    let comp: InventoryItemDetailComponent;
    let fixture: ComponentFixture<InventoryItemDetailComponent>;
    const route = ({ data: of({ inventoryItem: new InventoryItem(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BizioTestModule],
        declarations: [InventoryItemDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(InventoryItemDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(InventoryItemDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load inventoryItem on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.inventoryItem).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
