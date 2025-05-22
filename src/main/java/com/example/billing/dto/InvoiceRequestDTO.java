package com.example.billing.dto;

import java.util.List;

public class InvoiceRequestDTO {
    private Long customerId;
    private List<ItemDTO> items;

    public static class ItemDTO {
        private Long productId;
        private int quantity;

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }
    public static class Item {
        private Long productId;
        private int quantity;
    
        // Getters and Setters
        public Long getProductId() {
            return productId;
        }
    
        public void setProductId(Long productId) {
            this.productId = productId;
        }
    
        public int getQuantity() {
            return quantity;
        }
    
        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public List<ItemDTO> getItems() { return items; }
    public void setItems(List<ItemDTO> items) { this.items = items; }
}