import { CustomerInterface } from "./customer";
import { PurchasesProductsInterface } from "./purchasesProducts";

export interface PurchaseInterface {
  id?: number;
  state: string;
  payment: string;
  customerId: CustomerInterface | number;
  purchaseProducts?: PurchasesProductsInterface[];
  totalPurchase?:number;
  createdAt?:string;
}
