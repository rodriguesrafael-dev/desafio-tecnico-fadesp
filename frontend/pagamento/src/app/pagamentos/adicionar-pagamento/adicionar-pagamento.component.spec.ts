import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdicionarPagamentoComponent } from './adicionar-pagamento.component';

describe('AdicionarPagamentoComponent', () => {
  let component: AdicionarPagamentoComponent;
  let fixture: ComponentFixture<AdicionarPagamentoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdicionarPagamentoComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdicionarPagamentoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
