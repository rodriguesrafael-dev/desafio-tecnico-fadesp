export interface AdicionarPagamento {
  cpfCnpj: string;
  valor: number;
  metodoPagamento: string;
  numeroCartao?: string;
}
