export interface ListarPagamento {
  _id: string;
  idPagamento: number;
  cpfCnpj: string;
  _numeroCartao: string;
  valor: number;
  ativo: boolean;
  metodoPagamento: string;
  status: string;
}
