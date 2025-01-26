export interface Paiement {
    id?: number;
    datePaiement: string;
    methodePaiement: string;
    montant: number;
    statutPaiement: string;
    transactionId: string;
    commandeId: number;
  }
  