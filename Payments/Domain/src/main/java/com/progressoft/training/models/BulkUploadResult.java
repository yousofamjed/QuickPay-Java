package com.progressoft.training.models;

import java.util.List;

public record BulkUploadResult(int createdCount, int errorCount, List<Payment> createdPayments , List<BulkError> errorsPerRow) {
}
