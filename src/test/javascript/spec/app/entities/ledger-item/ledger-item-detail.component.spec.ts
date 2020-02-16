import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BizioTestModule } from '../../../test.module';
import { LedgerItemDetailComponent } from 'app/entities/ledger-item/ledger-item-detail.component';
import { LedgerItem } from 'app/shared/model/ledger-item.model';

describe('Component Tests', () => {
  describe('LedgerItem Management Detail Component', () => {
    let comp: LedgerItemDetailComponent;
    let fixture: ComponentFixture<LedgerItemDetailComponent>;
    const route = ({ data: of({ ledgerItem: new LedgerItem(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [BizioTestModule],
        declarations: [LedgerItemDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(LedgerItemDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LedgerItemDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load ledgerItem on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.ledgerItem).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
