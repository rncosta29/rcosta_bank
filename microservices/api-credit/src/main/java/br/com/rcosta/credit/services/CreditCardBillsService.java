package br.com.rcosta.credit.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import br.com.rcosta.credit.dto.CreditCardBillsDto;
import br.com.rcosta.credit.dto.CreditCardDto;
import br.com.rcosta.credit.models.CreditCardBillsModel;
import br.com.rcosta.credit.models.CreditCardModel;
import br.com.rcosta.credit.repositories.CreditCardBillsRepository;
import br.com.rcosta.credit.repositories.CreditCardRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class CreditCardBillsService {

    private CreditCardBillsRepository creditCardBillsRepository;
    private CreditCardRepository creditCardRepository;
    private final ModelMapper creditCardBillsModelMapper;

    public CreditCardBillsService(CreditCardBillsRepository creditCardBillsRepository, 
                                  CreditCardRepository creditCardRepository, 
                                  @Qualifier("creditCardBillsModelMapper") ModelMapper creditCardBillsModelMapper) {
        this.creditCardBillsRepository = creditCardBillsRepository;
        this.creditCardRepository = creditCardRepository;
        this.creditCardBillsModelMapper = creditCardBillsModelMapper;
    }

    public List<CreditCardBillsDto> allBills() {
        return creditCardBillsRepository.findAll().stream()
                .map(c -> creditCardBillsModelMapper.map(c, CreditCardBillsDto.class))
                .collect(Collectors.toList());
    }

    public CreditCardBillsDto addBills(CreditCardBillsDto dto) {
        CreditCardModel creditCardModel = creditCardRepository.findById(dto.getCreditCardId())
                .orElseThrow(() -> new EntityNotFoundException());

        dto.setCreditCardDto(creditCardBillsModelMapper.map(creditCardModel, CreditCardDto.class));
        CreditCardBillsModel model = creditCardBillsModelMapper.map(dto, CreditCardBillsModel.class);

        model.setCreditCard(creditCardModel);
        creditCardBillsRepository.save(model);

        return creditCardBillsModelMapper.map(model, CreditCardBillsDto.class);
    }

    public CreditCardBillsDto getBillsById(Long id) {
        CreditCardBillsModel model = creditCardBillsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException());

        return creditCardBillsModelMapper.map(model, CreditCardBillsDto.class);
    }

    public List<CreditCardBillsDto> getBillsByCreditCardId(Long creditCardId) {
        return creditCardBillsRepository.findByCreditCardId(creditCardId).stream()
                .sorted(Comparator.comparing(CreditCardBillsModel::getPaymentYear))  // Ordena pelo paymentYear em ordem crescente
                .map(c -> creditCardBillsModelMapper.map(c, CreditCardBillsDto.class))
                .collect(Collectors.toList());
    }

    public List<CreditCardBillsDto> addNewBills(CreditCardBillsDto model, Integer quantity) {
        List<CreditCardBillsModel> entities = new ArrayList<>();
        List<CreditCardBillsDto> result = new ArrayList<>();

        if (model.getIsParcel() == true) {
            int paymentMonth = model.getPaymentMonth();
            int paymentYear = model.getPaymentYear();

            // Recupera o cartão de crédito
            CreditCardModel creditCardModel = creditCardRepository.findById(model.getCreditCardId())
                    .orElseThrow(() -> new RuntimeException("Cartão de crédito não encontrado"));

            if (model.getPrice() == null || model.getPrice() <= 0) {
                throw new IllegalArgumentException("O preço informado é inválido.");
            }

            for (int i = 0; i < quantity; i++) {
                if (paymentMonth > 12) {
                    paymentMonth = 1;
                    paymentYear++;
                }

                // Cria a nova parcela
                CreditCardBillsModel entity = new CreditCardBillsModel();
                entity.setName(model.getName());
                // Arredondando o valor para evitar problemas com casas decimais
                entity.setPrice(BigDecimal.valueOf(model.getPrice()).divide(BigDecimal.valueOf(quantity), RoundingMode.HALF_UP).doubleValue());
                entity.setIsParcel(true);
                entity.setPaymentMonth(paymentMonth);
                entity.setPaymentYear(paymentYear);
                entity.setCreditCard(creditCardModel);
                entity.setDate(model.getDate());

                entities.add(entity);

                // Incrementa o mês
                paymentMonth++;

                // Ajusta o mês após o incremento
                if (paymentMonth > 12) {
                    paymentMonth = 1;
                    paymentYear++;
                }
            }

        } else {
            CreditCardModel creditCardModel = creditCardRepository.findById(model.getCreditCardId())
                    .orElseThrow(() -> new RuntimeException("Cartão de crédito não encontrado"));

            // Cria a fatura única
            CreditCardBillsModel entity = new CreditCardBillsModel();
            entity.setName(model.getName());
            entity.setPrice(model.getPrice());
            entity.setIsParcel(false);
            entity.setPaymentMonth(model.getPaymentMonth());
            entity.setPaymentYear(model.getPaymentYear());
            entity.setCreditCard(creditCardModel);
            entity.setDate(model.getDate());

            entities.add(entity);
        }

        // **Validações antes de salvar**
        validateBills(entities);

        // **Persistência após validação**
        for (CreditCardBillsModel entity : entities) {
            if (entity.getPrice() == null || model.getPrice() <= 0) {
                throw new IllegalStateException("A entidade tem o preço nulo antes da persistência.");
            }
            CreditCardBillsModel savedEntity = creditCardBillsRepository.save(entity);
            result.add(creditCardBillsModelMapper.map(savedEntity, CreditCardBillsDto.class));
        }

        return result;
    }

    public void deleteBillById(Long id) {
        try {
            // Verifica se a fatura existe antes de deletar
            CreditCardBillsModel bill = creditCardBillsRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Fatura com id " + id + " não encontrada."));

            // Realiza a exclusão
            creditCardBillsRepository.delete(bill);
        } catch (EntityNotFoundException e) {
            // Log da exceção, se necessário
            e.printStackTrace();
            throw e;  // Propaga a exceção corretamente
        } catch (Exception e) {
            // Log da exceção para depuração
            e.printStackTrace();
            throw new RuntimeException("Erro inesperado ao excluir a fatura.", e);
        }
    }

    private void validateBills(List<CreditCardBillsModel> bills) {
        for (CreditCardBillsModel bill : bills) {
            if (bill.getPrice() <= 0) {
                throw new IllegalArgumentException("O valor da parcela deve ser maior que zero.");
            }
            if (bill.getPaymentMonth() < 1 || bill.getPaymentMonth() > 12) {
                throw new IllegalArgumentException("O mês de pagamento deve estar entre 1 e 12.");
            }
        }
    }
}
