package com.example.drugs.controller;

import com.example.drugs.model.Drug;
import com.example.drugs.service.DrugService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/medicines")
public class DrugManagementController {

    private final DrugService drugService;

    public DrugManagementController(DrugService drugService) {
        this.drugService = drugService;
    }

    @GetMapping
    public List<Drug> getMedicines() {
        return drugService.getMedicines();
    }

    @GetMapping("/{id}")
    public Drug getById(@PathVariable Long id) {
        return drugService.getMedicineById(id);
    }

    @PostMapping
    @org.springframework.web.bind.annotation.ResponseStatus(HttpStatus.CREATED)
    public Drug addMedicine(@RequestBody Drug drug) {
        return drugService.addMedicine(drug);
    }

    @PutMapping("/{id}")
    public Drug updateMedicine(@PathVariable Long id, @RequestBody Drug drug) {
        return drugService.updateMedicine(id, drug);
    }

    @PatchMapping("/{id}/stock")
    public Drug updateStock(@PathVariable Long id, @RequestParam Integer stock) {
        return drugService.updateStock(id, stock);
    }

    @PostMapping("/{id}/restock")
    public Drug restockMedicine(@PathVariable Long id, @RequestParam Integer quantity) {
        return drugService.restockMedicine(id, quantity);
    }

    @PostMapping("/{id}/order")
    public Drug orderMedicine(@PathVariable Long id, @RequestParam Integer quantity) {
        return drugService.orderMedicine(id, quantity);
    }

    @DeleteMapping("/{id}")
    @org.springframework.web.bind.annotation.ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMedicine(@PathVariable Long id) {
        drugService.deleteMedicine(id);
    }

    @GetMapping("/low-stock")
    public List<Drug> getLowStockMedicines(@RequestParam(required = false) Integer threshold) {
        return drugService.getLowStockMedicines(threshold);
    }

    @GetMapping("/expiring-soon")
    public List<Drug> getExpiringSoonMedicines(@RequestParam(required = false) LocalDate beforeDate) {
        return drugService.getExpiringSoonMedicines(beforeDate);
    }

    @GetMapping("/category/{category}")
    public List<Drug> getMedicinesByCategory(@PathVariable String category) {
        return drugService.getMedicinesByCategory(category);
    }

    @GetMapping("/manufacturer/{manufacturer}")
    public List<Drug> getMedicinesByManufacturer(@PathVariable String manufacturer) {
        return drugService.getMedicinesByManufacturer(manufacturer);
    }

    @GetMapping("/search")
    public List<Drug> search(@RequestParam String keyword) {
        return drugService.searchMedicines(keyword);
    }

}
