import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ListarPagamento } from '../model/listar-pagamento';
import { Page } from '../model/page';
import { InativarPagamento } from '../model/inativar-pagamento';
import { AdicionarPagamento } from '../model/adicionar-pagamento';
import { ProcessarPagamento } from '../model/processar-pagamento';

@Injectable({
  providedIn: 'root'
})
export class PagamentoService {

  pagamentoEndpoint = environment.apiBaseURL;

  constructor(private httpClient: HttpClient) { }

  adicionarPagamento(pagamento: AdicionarPagamento): Observable<AdicionarPagamento> {
    return this.httpClient.post<AdicionarPagamento>(`${this.pagamentoEndpoint}/pagamentos`, pagamento);
  }

  listarPagamento(
    page: number = 0,
    size: number = 10,
    cpfCnpj?: string,
    status?: string
  ): Observable<Page<ListarPagamento>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (cpfCnpj && cpfCnpj.trim() !== '') {
      params = params.set('cpfCnpj', cpfCnpj.trim());
    }

    if (status && status.trim() !== '') {
      params = params.set('status', status.trim());
    }

    return this.httpClient.get<Page<ListarPagamento>>(`${this.pagamentoEndpoint}/pagamentos/lista-pagamentos`, { params });
  }

  processarPagamento(idPagamento: number, status: string): Observable<ProcessarPagamento> {
    const body: ProcessarPagamento = { idPagamento, status };
    return this.httpClient.put<ProcessarPagamento>(`${this.pagamentoEndpoint}/pagamentos/status`, body);
  }

  inativarPagamento(id: string): Observable<InativarPagamento> {
    return this.httpClient.delete<InativarPagamento>(`${this.pagamentoEndpoint}/pagamentos/${id}`);
  }

}
