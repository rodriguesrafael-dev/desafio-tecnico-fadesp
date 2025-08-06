import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { PagamentoListaComponent } from './pagamento-lista/pagamento-lista.component';

const routes: Routes = [
  { path: '', component: PagamentoListaComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PagamentosRoutingModule { }
