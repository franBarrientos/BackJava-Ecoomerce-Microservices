
export type PurchaseAddDTO = {
    payment: string;
    customerId:  number;
    purchaseProducts?: PurchaseProductAddDTO[];
}

export type PurchaseProductAddDTO = {
    quantity: number | undefined;
    productId: number;
}
