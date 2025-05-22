import api from "./axiosConfig";

export const fetchCustomers = async () => {
  const res = await api.get("/customers");
  return res.data;
};

export const addCustomer = async (customerData) => {
  const res = await api.post("/customers", customerData);
  return res.data;
};