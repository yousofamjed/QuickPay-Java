package com.progressoft.training.models;

import java.util.List;

public record BulkError(int rowNumber, List<String> errorMessages) {
}
