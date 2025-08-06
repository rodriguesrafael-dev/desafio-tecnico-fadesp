import { NgxMaskModule } from 'ngx-mask';
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatTableModule } from '@angular/material/table';

import { PagamentoListaComponent } from './pagamento-lista/pagamento-lista.component';
import { PagamentosRoutingModule } from './pagamentos-routing.module';
import { HttpClientModule } from '@angular/common/http';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { AdicionarPagamentoComponent } from './adicionar-pagamento/adicionar-pagamento.component';
import { MatDialogModule } from '@angular/material/dialog';
import { MatSnackBarModule } from '@angular/material/snack-bar';

@NgModule({
  declarations: [
    PagamentoListaComponent,
    AdicionarPagamentoComponent
  ],
  imports: [
    CommonModule,
    PagamentosRoutingModule,
    MatTableModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatSelectModule,
    MatTableModule,
    MatButtonModule,
    MatPaginatorModule,
    MatDialogModule,
    MatSnackBarModule,
    NgxMaskModule.forRoot()
  ],
  exports: [
    PagamentoListaComponent
  ]
})
export class PagamentosModule { }
