import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'pagamento-lista' },
  {
    path: 'pagamento-lista',
    loadChildren: () => import('./pagamentos/pagamentos.module').then(m => m.PagamentosModule)
  }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
