import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { Component, OnInit, ViewChild } from '@angular/core';
import { ListarPagamento } from '../model/listar-pagamento';
import { PagamentoService } from '../service/pagamento.service';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { FormBuilder, FormGroup } from '@angular/forms';
import { AdicionarPagamentoComponent } from '../adicionar-pagamento/adicionar-pagamento.component';
import { SuccessToastComponent } from 'src/app/shared/success-toast/success-toast.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FailureToastComponent } from 'src/app/shared/failure-toast/failure-toast.component';

@Component({
  selector: 'app-pagamento-lista',
  templateUrl: './pagamento-lista.component.html',
  styleUrls: ['./pagamento-lista.component.scss']
})
export class PagamentoListaComponent implements OnInit {

  pagamentoForm !: FormGroup;

  pagamento: ListarPagamento[] = [];

  totalItems = 0;
  currentPage = 0;
  pageSize = 6;
  pageIndex = 0;

  displayedColumns: string[] = [
    'cpfCnpj',
    'metodoPagamento',
    'valor',
    'status',
    'acoes'
  ];

  @ViewChild(MatPaginator, { static: true })
  paginator!: MatPaginator;

  constructor(
    private pagamentoService: PagamentoService,
    private matDialog: MatDialog,
    private formBuilder: FormBuilder,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.paginator._intl.itemsPerPageLabel = 'Itens por pagina: ';
    this.paginator._intl.nextPageLabel = 'Proxima';
    this.paginator._intl.previousPageLabel = 'Anterior';
    this.paginator._intl.firstPageLabel = 'Primeira pagina';
    this.paginator._intl.lastPageLabel = 'Ultima pagina';
    this.paginator._intl.getRangeLabel = this.getDisplayText;

    this.pagamentoForm = this.formBuilder.group({
      consultar: [''],
      status: ['']
    });

    this.getPagamentos();

    this.pagamentoForm.valueChanges
      .pipe(debounceTime(300), distinctUntilChanged())
      .subscribe(() => {
        this.getPagamentos();
      });
  }

  getPagamentos(page: number = 0) {
    const cpfCnpj = this.pagamentoForm.get('consultar')?.value;
    const status = this.pagamentoForm.get('status')?.value;

    this.pagamentoService.listarPagamento(page, this.pageSize, cpfCnpj, status).subscribe({
      next: (response) => {
        this.pagamento = response.content;
        this.totalItems = response.totalElements;
        this.currentPage = response.currentPage;
        console.log('Dados da API para a tabela:', response);
      },
      error: (error) => {
        console.error('Erro ao buscar pagamentos:', error);
      }
    });
  }

  processarPagamento(idPagamento: string): void {
    console.log('idPagamento recebido para processamento:', idPagamento);

    const pagamento = this.pagamento.find((p: any) => p.idPagamento === idPagamento);

    if (!pagamento) {
      console.error('Pagamento não encontrado na lista local');
      this.snackBar.openFromComponent(FailureToastComponent, {
        duration: 6000,
        panelClass: ['snackbar-error'],
        data: { message: 'Pagamento não encontrado.' }
      });
      return;
    }

    console.log('Pagamento encontrado:', pagamento);
    const statusAtual = pagamento.status;
    console.log('Status atual do pagamento:', statusAtual);

    const statusPermitido = ['PENDENTE_PROCESSAMENTO', 'PROCESSADO_FALHA'];

    if (!statusPermitido.includes(statusAtual)) {
      this.snackBar.openFromComponent(FailureToastComponent, {
        duration: 6000,
        panelClass: ['snackbar-error'],
        data: { message: `Pagamentos com status 'Processado com sucesso' não podem ser processados.` }
      });
      return;
    }

    const confirmacao = confirm(
      `Deseja realmente processar o pagamento com status atual: ${statusAtual}?`
    );

    if (!confirmacao) return;

    console.log('Chamando serviço para processar pagamento com idPagamento:', idPagamento);

    this.pagamentoService.processarPagamento(pagamento.idPagamento, statusAtual).subscribe({
      next: () => {
        console.log('Requisição de processamento enviada com sucesso para o Kafka.');

        const toastMessage = `Pedido de processamento para o pagamento #${pagamento.idPagamento} enviado. O status será atualizado em breve.`;

        this.snackBar.openFromComponent(SuccessToastComponent, {
          duration: 6000,
          panelClass: ['snackbar-success'],
          data: { message: toastMessage }
        });
        setTimeout(() => {
          this.getPagamentos();
        }, 2000);
      },
      error: (error) => {
        console.error('Erro ao processar pagamento:', error);

        let errorMessage = 'Erro ao processar pagamento.';
        if (error.status === 404) {
          errorMessage = 'Pagamento não encontrado. A lista será recarregada.';
          this.getPagamentos();
        } else if (error.error && error.error.message) {
          errorMessage = error.error.message;
        }

        this.snackBar.openFromComponent(FailureToastComponent, {
          duration: 6000,
          panelClass: ['snackbar-error'],
          data: { message: errorMessage }
        });
      }
    });
  }

  inativarPagamento(id: string): void {
    console.log('ID recebido:', id);

    const pagamento = this.pagamento.find((p: any) => p.id === id || p._id === id);

    if (!pagamento) {
      console.error('Pagamento não encontrado na lista local');
      this.snackBar.openFromComponent(FailureToastComponent, {
        duration: 5000,
        panelClass: ['snackbar-error'],
        data: { message: 'Pagamento não encontrado.' }
      });
      return;
    }

    console.log('Pagamento encontrado:', pagamento);
    console.log('Status do pagamento:', pagamento.status);

    if (pagamento.status !== 'PENDENTE_PROCESSAMENTO') {
      this.snackBar.openFromComponent(FailureToastComponent, {
        duration: 6000,
        panelClass: ['snackbar-error'],
        data: { message: 'Somente pagamentos com status PENDENTE_PROCESSAMENTO podem ser desativados.' }
      });
      return;
    }

    if (confirm('Tem certeza que deseja inativar este pagamento?')) {
      console.log('Chamando serviço para inativar pagamento com ID:', id);


      this.pagamentoService.inativarPagamento(id).subscribe({
        next: (response: any) => {
          console.log('Pagamento inativado com sucesso.', response);
          this.snackBar.openFromComponent(SuccessToastComponent, {
            duration: 6000,
            panelClass: ['snackbar-success'],
            data: { message: 'Pagamento inativado com sucesso.' }
          });
          this.getPagamentos();
        },
        error: (error) => {
          console.error('Erro completo ao inativar pagamento:', error);
          console.error('Status do erro:', error.status);
          console.error('Mensagem do erro:', error.error);

          let errorMessage = 'Não foi possível inativar o pagamento';

          if (error.status === 404 || error.error?.message?.includes('não encontrado')) {
            errorMessage = 'Pagamento não encontrado. A lista será recarregada.';
            this.getPagamentos();
          } else if (error.error?.message) {
            errorMessage = error.error.message;
          }

          this.snackBar.openFromComponent(FailureToastComponent, {
            duration: 6000,
            panelClass: ['snackbar-error'],
            data: { message: errorMessage }
          });
        }
      });
    }
  }

  openDialogAdicionar() {
    this.matDialog.open(AdicionarPagamentoComponent, {
      width: '700px',
      disableClose: true
    }).afterClosed().subscribe(() => {
      this.getPagamentos();
      this.paginator.firstPage();
    });
  };

  getStatusClass(status: string): string {
    switch (status) {
      case 'PROCESSADO_SUCESSO':
        return 'status-success';
      case 'PENDENTE_PROCESSAMENTO':
        return 'status-pending';
      case 'PROCESSADO_FALHA':
        return 'status-error';
      default:
        return '';
    }
  }

  getDisplayText(page: number, pageSize: number, length: number) {
    if (length == 0 || pageSize == 0) {
      return 'Sem resultados!';
    }
    const startIndex = page * pageSize;
    const endIndex =
      startIndex < length
        ? Math.min(startIndex + pageSize, length)
        : startIndex + pageSize;
    return `Itens: ${startIndex + 1} - ${endIndex} Pagina: ${page + 1
      } de ${Math.ceil(length / pageSize)}, total: ${length} itens`;
  }

  onPageChange(event: PageEvent) {
    this.pageSize = event.pageSize;
    this.getPagamentos(event.pageIndex);
  }

}
