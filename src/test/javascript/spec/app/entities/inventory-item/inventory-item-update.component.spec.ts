import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { BizioTestModule } from '../../../test.module';
import { InventoryItemUpdateComponent } from 'app/entities/inventory-item/inventory-item-update.component';
import { InventoryItemService } from 'app/entities/inventory-item/inventory-item.service';
import { InventoryItem } from 'app/shared/model/inventory-item.model';

describe('Component Tests', () => {
  describe('InventoryItem Management Update Component', () => {
    let comp: InventoryItemUpdateComponent;
    let fixture: ComponentFixture<InventoryItemUpdateComponent>;
    let service: InventoryItemService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BizioTestModule],
        declarations: [InventoryItemUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(InventoryItemUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(InventoryItemUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(InventoryItemService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new InventoryItem(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new InventoryItem();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
