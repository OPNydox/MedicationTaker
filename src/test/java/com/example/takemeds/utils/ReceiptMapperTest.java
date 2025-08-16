package com.example.takemeds.utils;

import com.example.takemeds.entities.Receipt;
import com.example.takemeds.presentationModels.ReceiptPresentationModel;
import com.example.takemeds.utils.mappers.ReceiptMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class ReceiptMapperTest {

    private ReceiptMapper receiptMapper;

    @BeforeEach
    void setup() {
        receiptMapper = new ReceiptMapper();
    }

    @Test
    @DisplayName("Should correctly map a Receipt entity to a ReceiptPresentationModel")
    void toPresentationModel_validInput_shouldMapCorrectly() {
        // Given
        Receipt receipt = Receipt.builder()
                .id(1L)
                .receiptTime(LocalDateTime.now())
                .description("Test receipt")
                .isFinalized(true)
                .build();

        // When
        ReceiptPresentationModel presentationModel = receiptMapper.toPresentationModel(receipt);

        // Then
        assertNotNull(presentationModel);
        assertEquals(1L, presentationModel.getId());
        assertEquals(receipt.getReceiptTime(), presentationModel.getReceiptTime());
        assertEquals("Test receipt", presentationModel.getDescription());
        assertTrue(presentationModel.isFinalized());
    }

    @Test
    @DisplayName("Should return null when mapping a null Receipt entity to a PM")
    void toPresentationModel_nullInput_shouldReturnNull() {
        // Given
        Receipt receipt = null;

        // When
        ReceiptPresentationModel presentationModel = receiptMapper.toPresentationModel(receipt);

        // Then
        assertNull(presentationModel);
    }

    @Test
    @DisplayName("Should correctly map a list of Receipt entities to a list of PMs")
    void toPresentationModelList_validList_shouldMapCorrectly() {
        // Given

        Receipt receipt1 = Receipt.builder().id(1L).description("Receipt 1").isFinalized(false).build();
        Receipt receipt2 = Receipt.builder().id(2L).description("Receipt 2").isFinalized(true).build();
        List<Receipt> receipts = List.of(receipt1, receipt2);


        // When
        List<ReceiptPresentationModel> resultList = receipts.stream()
                .map(receiptMapper::toPresentationModel)
                .collect(Collectors.toList());

        // Then
        assertNotNull(resultList);
        assertEquals(2, resultList.size());
        assertEquals("Receipt 1", resultList.get(0).getDescription());
        assertEquals("Receipt 2", resultList.get(1).getDescription());
    }

    @Test
    @DisplayName("Should return null when mapping a null ReceiptPresentationModel to an entity")
    void toEntity_nullInput_shouldReturnNull() {
        // Given
        ReceiptPresentationModel receiptPM = null;

        // When
        Receipt entity = receiptMapper.toEntity(receiptPM);

        // Then
        assertNull(entity);
    }

}
