import { AxiosResponse } from "axios";
import apiClient from "../config/axiosClient";
import {PurchaseAddDTO, PurchaseProductAddDTO} from "../interfaces/purchaseAddDTO.ts";

const prefix = "/purchases";
const mpPrefix = prefix+"/create-order-mp";

export const createPurchase = (
  purchase: PurchaseAddDTO
): Promise<AxiosResponse<any, any>> => {
  return apiClient.post(prefix, purchase, {
    headers: {
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

export const createOrderMp = (
  idPurchase: number,
  products: PurchaseProductAddDTO[],
): Promise<AxiosResponse<any, any>> => {
  return apiClient.post(mpPrefix, {
    idPurchase,
    products
  });
};

export const getPurchasesByCustomer = (
  customerId: number
): Promise<AxiosResponse<any, any>> => {
  return apiClient.get(`/purchases/customer/${customerId}`,{
    headers: {
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};
