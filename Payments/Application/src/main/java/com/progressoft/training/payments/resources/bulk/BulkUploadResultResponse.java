package com.progressoft.training.payments.resources.bulk;

import com.progressoft.training.models.BulkError;
import com.progressoft.training.payments.resources.payment.PaymentResponse;
import lombok.Data;

import java.util.List;

@Data
public class BulkUploadResultResponse {
   private int createdCount;
    private int errorCount;
    private List<PaymentResponse> createdPayments;
    private List<BulkError> errorsPerRow;

}
