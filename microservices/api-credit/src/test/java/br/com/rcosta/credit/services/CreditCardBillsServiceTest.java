package br.com.rcosta.credit.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import br.com.rcosta.credit.dto.CreditCardBillsDto;
import br.com.rcosta.credit.models.CreditCardBillsModel;
import br.com.rcosta.credit.models.CreditCardModel;
import br.com.rcosta.credit.repositories.CreditCardBillsRepository;
import br.com.rcosta.credit.repositories.CreditCardRepository;
import jakarta.persistence.EntityNotFoundException;

public class CreditCardBillsServiceTest {

	@Mock
	private CreditCardBillsRepository creditCardBillsRepository;

	@Mock
	private CreditCardRepository creditCardRepository;

	@Mock
	private ModelMapper creditCardBillsModelMapper;

	private CreditCardBillsService creditCardBillsService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		creditCardBillsService = new CreditCardBillsService(creditCardBillsRepository, creditCardRepository,
				creditCardBillsModelMapper);
	}

	@Test
	void shouldReturnAllBills() {
		CreditCardBillsModel bill = new CreditCardBillsModel(1L, "Test Bill", LocalDate.now(), 1, 2025, 100.0, false,
				new CreditCardModel());
		CreditCardBillsDto billDto = new CreditCardBillsDto();
		when(creditCardBillsRepository.findAll()).thenReturn(List.of(bill));
		when(creditCardBillsModelMapper.map(bill, CreditCardBillsDto.class)).thenReturn(billDto);

		List<CreditCardBillsDto> result = creditCardBillsService.allBills();

		assertNotNull(result);
		assertEquals(1, result.size());
		verify(creditCardBillsRepository, times(1)).findAll();
	}

	@Test
	void shouldReturnEmptyListWhenNoBills() {
		when(creditCardBillsRepository.findAll()).thenReturn(List.of());

		List<CreditCardBillsDto> result = creditCardBillsService.allBills();

		assertNotNull(result);
		assertEquals(0, result.size());
		verify(creditCardBillsRepository, times(1)).findAll();
	}

	@Test
	void shouldAddBill() {
		CreditCardBillsDto billDto = new CreditCardBillsDto();
		billDto.setCreditCardId(1L);

		CreditCardModel creditCardModel = new CreditCardModel(1L, "Test Credit Card");
		CreditCardBillsModel billModel = new CreditCardBillsModel(1L, "Test Bill", LocalDate.now(), 1, 2025, 100.0,
				false, creditCardModel);

		when(creditCardRepository.findById(1L)).thenReturn(Optional.of(creditCardModel));
		when(creditCardBillsModelMapper.map(billDto, CreditCardBillsModel.class)).thenReturn(billModel);
		when(creditCardBillsModelMapper.map(billModel, CreditCardBillsDto.class)).thenReturn(billDto);

		CreditCardBillsDto result = creditCardBillsService.addBills(billDto);

		assertNotNull(result);
		assertEquals(billDto.getCreditCardId(), result.getCreditCardId());
		verify(creditCardBillsRepository, times(1)).save(billModel);
	}

	@Test
	void shouldThrowExceptionWhenAddingBillWithInvalidCreditCard() {
		CreditCardBillsDto billDto = new CreditCardBillsDto();
		billDto.setCreditCardId(1L);

		when(creditCardRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(EntityNotFoundException.class, () -> creditCardBillsService.addBills(billDto));
	}

	@Test
	void shouldDeleteBill() {
		CreditCardBillsModel billModel = new CreditCardBillsModel(1L, "Test Bill", LocalDate.now(), 1, 2025, 100.0,
				false, new CreditCardModel());
		when(creditCardBillsRepository.findById(1L)).thenReturn(Optional.of(billModel));

		creditCardBillsService.deleteBillById(1L);

		verify(creditCardBillsRepository, times(1)).delete(billModel);
	}

	@Test
	void shouldThrowExceptionWhenDeletingNonexistentBill() {
		when(creditCardBillsRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(EntityNotFoundException.class, () -> creditCardBillsService.deleteBillById(1L));
	}

	@Test
	void shouldGetBillById() {
		CreditCardBillsModel billModel = new CreditCardBillsModel(1L, "Test Bill", LocalDate.now(), 1, 2025, 100.0,
				false, new CreditCardModel());
		CreditCardBillsDto billDto = new CreditCardBillsDto();
		when(creditCardBillsRepository.findById(1L)).thenReturn(Optional.of(billModel));
		when(creditCardBillsModelMapper.map(billModel, CreditCardBillsDto.class)).thenReturn(billDto);

		CreditCardBillsDto result = creditCardBillsService.getBillsById(1L);

		assertNotNull(result);
		verify(creditCardBillsRepository, times(1)).findById(1L);
	}

	@Test
	void shouldThrowExceptionWhenGettingNonexistentBillById() {
		when(creditCardBillsRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(EntityNotFoundException.class, () -> creditCardBillsService.getBillsById(1L));
	}

	@Test
	void shouldGetBillsByCreditCardId() {
		CreditCardModel creditCardModel = new CreditCardModel(1L, "Test Credit Card");
		CreditCardBillsModel bill1 = new CreditCardBillsModel(1L, "Test Bill 1", LocalDate.now(), 1, 2025, 100.0, false,
				creditCardModel);
		CreditCardBillsModel bill2 = new CreditCardBillsModel(2L, "Test Bill 2", LocalDate.now(), 2, 2025, 150.0, false,
				creditCardModel);

		CreditCardBillsDto billDto1 = new CreditCardBillsDto();
		billDto1.setPaymentMonth(1);
		CreditCardBillsDto billDto2 = new CreditCardBillsDto();
		billDto2.setPaymentMonth(2);

		when(creditCardBillsRepository.findByCreditCardId(1L)).thenReturn(List.of(bill1, bill2));
		when(creditCardBillsModelMapper.map(bill1, CreditCardBillsDto.class)).thenReturn(billDto1);
		when(creditCardBillsModelMapper.map(bill2, CreditCardBillsDto.class)).thenReturn(billDto2);

		List<CreditCardBillsDto> result = creditCardBillsService.getBillsByCreditCardId(1L);

		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals(1, result.get(0).getPaymentMonth());
		assertEquals(2, result.get(1).getPaymentMonth());
		verify(creditCardBillsRepository, times(1)).findByCreditCardId(1L);
	}

	@Test
	void shouldAddNewBillsWithParcel() {
		CreditCardBillsDto billDto = new CreditCardBillsDto();
		billDto.setCreditCardId(1L);
		billDto.setPrice(300.0);
		billDto.setIsParcel(true);
		billDto.setPaymentMonth(1);
		billDto.setPaymentYear(2025);
		billDto.setName("Test Bill");

		CreditCardModel creditCardModel = new CreditCardModel(1L, "Test Credit Card");
		when(creditCardRepository.findById(1L)).thenReturn(Optional.of(creditCardModel));
		when(creditCardBillsRepository.save(any(CreditCardBillsModel.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));
		when(creditCardBillsModelMapper.map(any(CreditCardBillsModel.class), eq(CreditCardBillsDto.class)))
				.thenAnswer(invocation -> {
					CreditCardBillsModel model = invocation.getArgument(0);
					CreditCardBillsDto dto = new CreditCardBillsDto();
					dto.setName(model.getName());
					dto.setPrice(model.getPrice());
					dto.setPaymentMonth(model.getPaymentMonth());
					dto.setPaymentYear(model.getPaymentYear());
					dto.setCreditCardId(model.getCreditCard().getId());
					return dto;
				});

		ArgumentCaptor<CreditCardBillsModel> captor = ArgumentCaptor.forClass(CreditCardBillsModel.class);

		List<CreditCardBillsDto> result = creditCardBillsService.addNewBills(billDto, 3);

		assertNotNull(result);
		assertEquals(3, result.size());
		result.forEach(parcel -> {
			assertNotNull(parcel);
			assertEquals(100.0, parcel.getPrice());
		});

		verify(creditCardBillsRepository, times(3)).save(captor.capture());
		captor.getAllValues().forEach(parcel -> {
			assertNotNull(parcel.getCreditCard());
			assertEquals(creditCardModel.getId(), parcel.getCreditCard().getId());
		});
	}

	@Test
	void shouldMapEntityToDtoCorrectlyWhenAddingBill() {
	    // Preparação dos objetos
	    CreditCardBillsDto billDto = new CreditCardBillsDto();
	    billDto.setCreditCardId(1L);
	    CreditCardModel creditCardModel = new CreditCardModel(1L, "Test Credit Card");

	    // Criando o modelo de fatura
	    CreditCardBillsModel billModel = new CreditCardBillsModel(1L, "Test Bill", LocalDate.now(), 1, 2025, 100.0,
	            false, creditCardModel);

	    // Mockando o repositório para retornar o CreditCardModel
	    when(creditCardRepository.findById(1L)).thenReturn(Optional.of(creditCardModel)); // Mock do repositório
	    when(creditCardBillsModelMapper.map(billDto, CreditCardBillsModel.class)).thenReturn(billModel);
	    when(creditCardBillsModelMapper.map(billModel, CreditCardBillsDto.class)).thenReturn(billDto);

	    // Chamando o serviço
	    CreditCardBillsDto result = creditCardBillsService.addBills(billDto);

	    // Asserções
	    assertEquals(billDto.getCreditCardId(), result.getCreditCardId());
	    verify(creditCardBillsModelMapper, times(1)).map(billDto, CreditCardBillsModel.class);
	    verify(creditCardBillsModelMapper, times(1)).map(billModel, CreditCardBillsDto.class);
	}

}
