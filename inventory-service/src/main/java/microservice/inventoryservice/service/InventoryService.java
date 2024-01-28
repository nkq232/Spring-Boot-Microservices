package microservice.inventoryservice.service;

import microservice.inventoryservice.dto.InventoryResponse;

import java.util.List;

public interface InventoryService {
    public List<InventoryResponse> isInStock(List<String> skuCodes) throws InterruptedException;
}
