package com.example.takemeds.utils.mappers;

import com.example.takemeds.entities.Receipt;
import com.example.takemeds.exceptions.InvalidFrequencyException;
import com.example.takemeds.presentationModels.ReceiptPresentationModel;
import com.example.takemeds.presentationModels.UserMedicationPresentationModel;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReceiptMappers {

    private final UserMedicationMapper userMedicationMapper;

    public ReceiptMappers(UserMedicationMapper userMedicationMapper) {
        this.userMedicationMapper = userMedicationMapper;
    }

    public ReceiptPresentationModel toPresentationModel(Receipt receipt) {
        if (receipt == null) {
            return null;
        }

        List<UserMedicationPresentationModel> userMedicationPModels = receipt.getUserMedications().stream()
                .map(userMedicationMapper::toUserMedicationPresentationModel)
                .collect(Collectors.toList());

        return ReceiptPresentationModel.builder()
                .id(receipt.getId())
                .receiptTime(receipt.getReceiptTime())
                .description(receipt.getDescription())
                .isFinalized(receipt.isFinalized())
                .userMedications(userMedicationPModels)
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
                                .description(receiptPM.getDescription())
                                .isFinalized(receiptPM.isFinalized())
                .userMedications(userMedicationMapper.toUserMedicationEntities(receiptPM.getUserMedications())).build();
    }

    @SneakyThrows
    public List<Receipt> toEntities(List<ReceiptPresentationModel> receiptsPM)  {
        return receiptsPM.stream().map(this::toEntity).toList();
    }
}
