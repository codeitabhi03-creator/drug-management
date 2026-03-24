package com.example.drugs.service.impl;

import com.example.drugs.exception.DrugNotFoundException;
import com.example.drugs.exception.InvalidOrderException;
import com.example.drugs.model.Drug;
import com.example.drugs.repository.DrugRepository;
import com.example.drugs.service.DrugService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DrugServiceImpl implements DrugService {

    private final DrugRepository drugRepository;

    public DrugServiceImpl(DrugRepository drugRepository) {
        this.drugRepository = drugRepository;
    }

    @Override
    public List<Drug> getMedicines() {
        return drugRepository.findAll();
    }

    @Override
    public Drug getMedicineById(Long id) {
        return drugRepository.findById(id)
                .orElseThrow(() -> new DrugNotFoundException(id));
    }

    @Override
    public Drug addMedicine(Drug drug) {
        drug.setId(null);
        return drugRepository.save(drug);
    }

    @Override
    public Drug updateMedicine(Long id, Drug updatedDrug) {
        Drug existingDrug = getMedicineById(id);
        existingDrug.setName(updatedDrug.getName());
        existingDrug.setManufacturer(updatedDrug.getManufacturer());
        existingDrug.setCategory(updatedDrug.getCategory());
        existingDrug.setPrice(updatedDrug.getPrice());
        existingDrug.setStock(updatedDrug.getStock());
        existingDrug.setExpiryDate(updatedDrug.getExpiryDate());
        return drugRepository.save(existingDrug);
    }

    @Override
    public Drug updateStock(Long id, Integer stock) {
        Drug drug = getMedicineById(id);
        drug.setStock(stock);
        return drugRepository.save(drug);
    }

    @Override
    public Drug restockMedicine(Long id, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new InvalidOrderException("Restock quantity must be greater than 0");
        }

        Drug drug = getMedicineById(id);
        Integer currentStock = drug.getStock() == null ? 0 : drug.getStock();
        drug.setStock(currentStock + quantity);
        return drugRepository.save(drug);
    }

    @Override
    public Drug orderMedicine(Long id, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new InvalidOrderException("Order quantity must be greater than 0");
        }

        Drug drug = getMedicineById(id);
        Integer currentStock = drug.getStock() == null ? 0 : drug.getStock();
        if (currentStock < quantity) {
            throw new InvalidOrderException(
                    "Insufficient stock for medicine id: " + id,
                    currentStock
            );
        }

        drug.setStock(currentStock - quantity);
        return drugRepository.save(drug);
    }

    @Override
    public List<Drug> searchMedicines(String keyword) {
        return drugRepository.findByNameContainingIgnoreCaseOrManufacturerContainingIgnoreCaseOrCategoryContainingIgnoreCase(
                keyword,
                keyword,
                keyword
        );
    }

    @Override
    public List<Drug> getLowStockMedicines(Integer threshold) {
        int effectiveThreshold = threshold == null ? 10 : threshold;
        return drugRepository.findByStockLessThan(effectiveThreshold);
    }

    @Override
    public List<Drug> getExpiringSoonMedicines(LocalDate beforeDate) {
        LocalDate effectiveDate = beforeDate == null ? LocalDate.now().plusDays(30) : beforeDate;
        return drugRepository.findByExpiryDateLessThanEqual(effectiveDate);
    }

    @Override
    public List<Drug> getMedicinesByCategory(String category) {
        return drugRepository.findByCategoryIgnoreCase(category);
    }

    @Override
    public List<Drug> getMedicinesByManufacturer(String manufacturer) {
        return drugRepository.findByManufacturerIgnoreCase(manufacturer);
    }

    @Override
    public void deleteMedicine(Long id) {
        Drug drug = getMedicineById(id);
        drugRepository.delete(drug);
    }
}
