import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { BizioTestModule } from '../../../test.module';
import { LedgerItemUpdateComponent } from 'app/entities/ledger-item/ledger-item-update.component';
import { LedgerItemService } from 'app/entities/ledger-item/ledger-item.service';
import { LedgerItem } from 'app/shared/model/ledger-item.model';

describe('Component Tests', () => {
  describe('LedgerItem Management Update Component', () => {
    let comp: LedgerItemUpdateComponent;
    let fixture: ComponentFixture<LedgerItemUpdateComponent>;
    let service: LedgerItemService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BizioTestModule],
        declarations: [LedgerItemUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(LedgerItemUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LedgerItemUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LedgerItemService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new LedgerItem(123);
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
        const entity = new LedgerItem();
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
