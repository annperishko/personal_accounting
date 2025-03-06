export class Transaction {
  transactionType?: string;
  description?: string;
  category?: null;
  amount?: number;
  transactionDate?: string;
  userId?: number;

  constructor(json?: any) {
    if (json) {
      this.transactionType = json.transactionType;
      this.description = json.description;
      this.category = json.category;
      this.amount = json.amount;
      this.transactionDate = json.transactionDate;
      this.userId = json.userId;
    }
  }
}
