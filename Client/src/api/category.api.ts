import { AxiosResponse } from "axios";
import apiClient from "../config/axiosClient";

const prefix = "/categories";

export const getAllCategories = (): Promise<AxiosResponse<any, any>> => {
  return apiClient.get(prefix);
};

export const createNewCategory = (
  formData: FormData
): Promise<AxiosResponse<any, any>> => {
  return apiClient.post(prefix, formData, {
    headers: {
      "Content-Type": "multipart/form-data",
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};
export const updateCategory = (
  formData: FormData,
  id:number
): Promise<AxiosResponse<any, any>> => {
  return apiClient.put(`/categories/${id}`, formData, {
    headers: {
      "Content-Type": "multipart/form-data",
      Authorization: `Bearer ${localStorage.getItem("token")}`,
    },
  });
};

