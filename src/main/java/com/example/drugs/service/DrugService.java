package com.example.drugs.service;

import com.example.drugs.model.Drug;

import java.time.LocalDate;
import java.util.List;

public interface DrugService {

    List<Drug> getMedicines();

    Drug getMedicineById(Long id);

    Drug addMedicine(Drug drug);

    Drug updateMedicine(Long id, Drug drug);

    Drug updateStock(Long id, Integer stock);

    Drug restockMedicine(Long id, Integer quantity);

    Drug orderMedicine(Long id, Integer quantity);

    List<Drug> searchMedicines(String keyword);

    List<Drug> getLowStockMedicines(Integer threshold);

    List<Drug> getExpiringSoonMedicines(LocalDate beforeDate);

    List<Drug> getMedicinesByCategory(String category);

    List<Drug> getMedicinesByManufacturer(String manufacturer);

    void deleteMedicine(Long id);
}
