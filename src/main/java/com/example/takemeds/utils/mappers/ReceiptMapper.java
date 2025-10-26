package com.example.takemeds.utils.mappers;

import com.example.takemeds.entities.MedicationSchedule;
import com.example.takemeds.entities.Receipt;
import com.example.takemeds.presentationModels.ReceiptPresentationModel;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReceiptMapper {

    private MedicationScheduleMapper scheduleMapper;

    public ReceiptPresentationModel toPresentationModel(Receipt receipt) {
        if (receipt == null) {
            return null;
        }

        return ReceiptPresentationModel.builder()
                .id(receipt.getId())
                .receiptTime(receipt.getReceiptTime())
                .description(receipt.getDescription())
                .build();
    }



    public List<ReceiptPresentationModel> toPresentationModelList(List<Receipt> receipts) {
        return receipts.stream()
                .map(this::toPresentationModel)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public Receipt toEntity(ReceiptPresentationModel receiptPM) {
        if (receiptPM == null) {
            return null;
        }

        return Receipt.builder().id(receiptPM.getId())
                                .description(receiptPM.getDescription()).build();
    }

    @SneakyThrows
    public List<Receipt> toEntities(List<ReceiptPresentationModel> receiptsPM)  {
        return receiptsPM.stream().map(this::toEntity).toList();
    }
}
