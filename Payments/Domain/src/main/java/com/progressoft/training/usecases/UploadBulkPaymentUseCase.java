package com.progressoft.training.usecases;

import com.progressoft.training.exception.SystemViolationException;
import com.progressoft.training.fileparser.domain.ParseResult;
import com.progressoft.training.fileparser.domain.ValidationError;
import com.progressoft.training.fileparser.usecase.ParseFileUseCase;
import com.progressoft.training.models.BulkError;
import com.progressoft.training.models.BulkUploadResult;
import com.progressoft.training.models.ParsedPayment;
import com.progressoft.training.models.Payment;
import com.progressoft.training.repository.PaymentRepository;
import com.progressoft.training.validation.ValidationResult;
import com.progressoft.training.validation.ValidatorChain;

import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class UploadBulkPaymentUseCase {
    private final boolean persistDb;
    private final ParseFileUseCase<ParsedPayment> parseFileUseCase;
    private final ValidatorChain<Payment> validatorChain;
    private final PaymentRepository paymentRepository;

    public UploadBulkPaymentUseCase(ParseFileUseCase<ParsedPayment> parseFileUseCase, ValidatorChain<Payment> validatorChain, PaymentRepository paymentRepository, boolean persistDb) {
        this.parseFileUseCase = parseFileUseCase;
        this.validatorChain = validatorChain;
        this.paymentRepository = paymentRepository;
        this.persistDb = persistDb;
    }

    public BulkUploadResult execute(String templateName, Path file){
        ParseResult<ParsedPayment> result = parseFileUseCase.execute(new ParseFileUseCase.ParseFileCommand(templateName,file));

        //errors map:  key -> rownumber : value -> BulkError(has row number , errors per row)

        Map<Integer, BulkError> errors = new TreeMap<>();
        for (ValidationError error : result.errors()) {
            errors.computeIfAbsent(error.rowNumber(), row -> new BulkError(
                    row, new ArrayList<>())).errorMessages().add(String.format("%s: %s",
                    error.fieldName(), error.message()));
        }

        List<Payment> validPayments = new ArrayList<>();
        int created = 0;
        for (ParsedPayment parsedPayment : result.validRows()) {
            try {
                ValidationResult validationResult = validatorChain.validate(parsedPayment.payment());
                validationResult.throwExceptionIfViolated();

                Payment payment = parsedPayment.payment();
                payment.setCreatedAt(Instant.now());
                payment.setStatus("PENDING");
                validPayments.add(payment);
                    created++;
            } catch (SystemViolationException exception) {
                errors.computeIfAbsent(parsedPayment.rowNumber(), row -> new BulkError(
                        row, new ArrayList<>())).errorMessages().addAll(exception.getViolationMessage());
            }
        }
        List<Payment> identifiedPayments = new ArrayList<>();

        //we only save to database if all is valid
        if(errors.isEmpty() || persistDb){
            identifiedPayments = paymentRepository.saveAll(validPayments);
        }
        else
            created=0;


                        //validrows count invalid rows count  errorsPerROw
        return new BulkUploadResult(created, errors.size(), identifiedPayments , List.copyOf(errors.values()));

    }

}
