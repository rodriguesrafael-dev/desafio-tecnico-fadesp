import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { SalvarPagamentoDTO } from '../model/adicionar-pagamento';
import { PagamentoService } from '../service/pagamento.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SuccessToastComponent } from 'src/app/shared/success-toast/success-toast.component';
import { FailureToastComponent } from 'src/app/shared/failure-toast/failure-toast.component';

@Component({
  selector: 'app-adicionar-pagamento',
  templateUrl: './adicionar-pagamento.component.html',
  styleUrls: ['./adicionar-pagamento.component.scss']
})
export class AdicionarPagamentoComponent implements OnInit {

  pagamentoForm: FormGroup;

  constructor(
    private pagamentoService: PagamentoService,
    private dialogRef: MatDialogRef<AdicionarPagamentoComponent>,
    private formBuilder: FormBuilder,
    private snackBar: MatSnackBar
  ) {
    this.pagamentoForm = this.formBuilder.group({
      identificacao: ['', Validators.required],
      metodoPagamento: ['', Validators.required],
      numeroCartao: [''],
      valor: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.pagamentoForm.get('metodoPagamento')?.valueChanges.subscribe(metodo => {
      const numeroCartaoControl = this.pagamentoForm.get('numeroCartao');

      if (metodo === 'CARTAO_CREDITO' || metodo === 'CARTAO_DEBITO') {
        numeroCartaoControl?.setValidators(Validators.required);
      } else {
        numeroCartaoControl?.clearValidators();
      }
      numeroCartaoControl?.updateValueAndValidity();
    });
  }

  salvarPagamento(): void {
    if (this.pagamentoForm.valid) {
      const metodoPagamento = this.pagamentoForm.value.metodoPagamento;

      const isCartao = metodoPagamento === 'CARTAO_CREDITO' || metodoPagamento === 'CARTAO_DEBITO';

      const dto: SalvarPagamentoDTO = {
        cpfCnpj: this.pagamentoForm.value.identificacao,
        valor: this.pagamentoForm.value.valor,
        metodoPagamento: metodoPagamento,
      };

      if (isCartao) {
        dto.numeroCartao = this.pagamentoForm.value.numeroCartao;
      }

      this.pagamentoService.adicionarPagamento(dto).subscribe({
        next: (response) => {
          console.log('Pagamento salvo com sucesso!', response);
          this.snackBar.openFromComponent(SuccessToastComponent, {
            duration: 6000,
            panelClass: ['snackbar-success'],
            data: { message: 'Pagamento salvo com sucesso' }
          });
          this.dialogRef.close(true);
        },
        error: (error) => {
          console.error('Erro ao salvar o pagamento: ', error);
          this.snackBar.openFromComponent(FailureToastComponent, {
            duration: 6000,
            panelClass: ['snackbar-error'],
            data: { message: 'Falha ao salvar pagamento' }
          });
        }
      });
    }
  }

  limparValorMonetario(valor: string): number {
    if (!valor) return 0;
    const valorLimpo = valor.replace('R$ ', '').replace(/\./g, '').replace(',', '.');
    return parseFloat(valorLimpo);
  }

  fecharDialog(): void {
    this.dialogRef.close();
  }

}
