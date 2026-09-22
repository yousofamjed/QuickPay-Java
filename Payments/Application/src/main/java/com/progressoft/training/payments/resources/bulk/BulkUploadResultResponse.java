package com.progressoft.training.payments.resources.bulk;

import com.progressoft.training.models.BulkError;
import com.progressoft.training.payments.resources.payment.PaymentResponse;
import lombok.Data;

import java.util.List;

@Data
public class BulkUploadResultResponse {
    int createdCount;
    int errorCount;
    List<PaymentResponse> createdPayments;
    List<BulkError> errorsPerRow;

}
