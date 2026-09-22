package com.progressoft.training.payments.config;


import com.progressoft.training.fileparser.parser.FileParserFactory;
import com.progressoft.training.fileparser.repository.JsonTemplateRepository;
import com.progressoft.training.fileparser.usecase.*;
import com.progressoft.training.models.ParsedPayment;
import com.progressoft.training.models.Payment;
import com.progressoft.training.payments.repository.TemplateRepositoryImpl;
import com.progressoft.training.payments.service.TemplateService;
import com.progressoft.training.repository.PaymentRepository;
import com.progressoft.training.usecases.CreatePaymentUseCase;
import com.progressoft.training.usecases.UploadBulkPaymentUseCase;
import com.progressoft.training.validation.ValidatorChain;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

@Configuration
public class UseCaseConfig {
    @Bean
    @Qualifier("createPaymentValidators")
    public CreatePaymentUseCase createPaymentUseCase(ValidatorChain<Payment> chain, PaymentRepository paymentRepository) {
        return new CreatePaymentUseCase(chain, paymentRepository);
    }

    @Bean
    public UploadBulkPaymentUseCase uploadBulkPaymentUseCase(ParseFileUseCase<ParsedPayment> parseFileUseCase, ValidatorChain<Payment> chain,
                                                             PaymentRepository repo, @Value("${payment.persistdb:false}") boolean persistDb ) {
        return new UploadBulkPaymentUseCase(parseFileUseCase, chain, repo, persistDb);
    }

    @Bean
    public FileParserFactory fileParserFactory() {
        return new FileParserFactory();
    }

    @Bean
    public ParseFileUseCase<ParsedPayment> parseFileUseCase(JsonTemplateRepository jsonTemplateRepository,
                                                            FileParserFactory fileParserFactory) {
        return new ParseFileUseCase<>(
                jsonTemplateRepository,
                fileParserFactory,
                parsedRow -> {
                    Payment payment = new Payment();
                    payment.setCreditorName(parsedRow.get("creditorName"));
                    payment.setSenderAccount(parsedRow.get("senderAccount"));
                    payment.setAmount(new BigDecimal(parsedRow.get("amount")));
                    payment.setCurrency(parsedRow.get("currency"));
                    payment.setNotes(parsedRow.get("notes"));
                    payment.setReceiverAccount(parsedRow.get("receiverAccount"));
                    return new ParsedPayment(
                            parsedRow.rowNumber(),
                            payment
                    );
                }
        );
    }

    @Bean
    public JsonTemplateRepository jsonTemplateRepository() throws IOException, URISyntaxException {
        URL resource = getClass().getClassLoader().getResource("template/DefaultTemplate.json");
        return new JsonTemplateRepository(Path.of(resource.toURI()));
    }
    @Bean
    public GetTemplateUseCase getTemplateUseCase(TemplateRepositoryImpl templateRepository) {
        return new GetTemplateUseCase(templateRepository);
    }

    @Bean
    public CreateTemplateUseCase createTemplateUseCase(TemplateRepositoryImpl templateRepository) {
        return new CreateTemplateUseCase(templateRepository);
    }

    @Bean
    public ListTemplatesUseCase listTemplatesUseCase(TemplateRepositoryImpl templateRepository){
        return new ListTemplatesUseCase(templateRepository);
    }

    @Bean
    public DeleteTemplateUseCase deleteTemplateUseCase(TemplateRepositoryImpl  templateRepository) {
        return new DeleteTemplateUseCase(templateRepository);

    }

    @Bean
    public UpdateTemplateUseCase updateTemplateUseCase(TemplateRepositoryImpl templateRepository) {
        return new UpdateTemplateUseCase(templateRepository);
    }



}
