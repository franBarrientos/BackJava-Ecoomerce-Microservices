package com.ecommerce.Domain.Application.Services;

import com.ecommerce.Domain.Application.Client.ProductClient;
import com.ecommerce.Domain.Application.Client.UserClient;
import com.ecommerce.Domain.Application.Dtos.*;
import com.ecommerce.Domain.Application.Exceptions.NotFoundException;
import com.ecommerce.Domain.Application.Mappers.PurchaseAddDtoMapper;
import com.ecommerce.Domain.Application.Mappers.PurchaseDtoMapper;
import com.ecommerce.Domain.Application.Repositories.PurchaseRepository;
import com.ecommerce.Domain.Domain.Purchase;
import com.ecommerce.Domain.Domain.PurchaseProduct;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final PurchaseDtoMapper purchaseDtoMapper;
    private final PurchaseAddDtoMapper purchaseAddDtoMapper;
    private final ProductClient productClient;
    private final UserClient userClient;

    @Value("${mercado-pago.access-token}")
    private String mercadoPagoAccesToken;
    @Value("${URL_FRONT}")
    private String urlFront;
    @Value("${URL_BACK}")
    private String urlBack;

    public Page<PurchaseDTO> getAllPurchases(Pageable pageable) {
        Page<Purchase> purchasesPage = this.purchaseRepository.findAll(pageable);

        List<PurchaseDTO> purchaseDTOs = purchasesPage
                .get()
                .parallel()
                .map(this.purchaseDtoMapper::toDto)
                .collect(Collectors.toList());

        return new PageImpl<>(purchaseDTOs, pageable, purchasesPage.getTotalElements());
    }



    public PurchaseDTO getById(Long id) {
        Purchase purchaseDomain = this.purchaseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("purchase " + id + " not found"));

        PurchaseDTO purchaseDTO = this.purchaseDtoMapper.toDto(purchaseDomain);

        return purchaseDTO;
    }

    public List<PurchaseDTO> getByCustomerId(Long id) {
        return this.purchaseRepository.findAllByCustomerId(id)
                .stream()
                .map(this.purchaseDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    public boolean isOwnOfTheResource(Long idResource, Long idUser){
        Long customerId = this.purchaseRepository.findById(idResource)
                .orElseThrow(() -> new NotFoundException("purchase " + idResource + " not found"))
                .getCustomerId();

        return this.userClient.getCustomerDTO(customerId).getUser().getId().equals(idUser);
    }
    public boolean isOwnOfTheResourceCustomer(Long idResource, Long idUser){
        return this.userClient.getCustomerDTO(idResource).getUser().getId().equals(idUser);
    }



    @Transactional
    public PurchaseDTO createOne(PurchaseAddDTO purchase) {

        CustomerDTO customerDTO = this.userClient.getCustomerDTO(purchase.getCustomerId());
        if (customerDTO == null) {
            throw new NotFoundException("customer " + purchase.getCustomerId() + " not found");
        }

        Purchase purchaseToSave = this.purchaseAddDtoMapper.purchaseAddDTOtoDomain(purchase);

        return this.purchaseDtoMapper.toDto(this.purchaseRepository.save(purchaseToSave), customerDTO );

    }

    public PurchaseDTO updateById(Long id, PurchaseAddDTO purchaseToUpdate) {

        Purchase purchase = this.purchaseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("purchase " + id + " not found"));

        Purchase purchaseDomain = this.purchaseAddDtoMapper.purchaseAddDTOtoDomainWithPurchaseOptional(purchaseToUpdate);


        if (purchaseToUpdate.getPayment() != null) {
            purchase.setPayment(purchaseToUpdate.getPayment());
        }
        if (purchaseToUpdate.getPurchaseProducts() != null) {
            purchase.setPurchaseProducts(purchaseDomain.getPurchaseProducts());
        }

        PurchaseDTO purchaseToReturn = this.purchaseDtoMapper.toDto(this.purchaseRepository.save(purchase));
        purchaseToReturn.setCustomer(this.userClient.getCustomerDTO(purchase.getCustomerId()));
        purchaseToReturn.getPurchaseProducts()
                .forEach(pp->pp.setProduct(this.productClient.getProductDTO(pp.getProduct().getId())));

        return purchaseToReturn;
    }


    public SalesStatistics getStadistics(Pageable productPage, Pageable categoryPage, Pageable daysPage) {
        SalesStatistics stadistics = this.purchaseRepository.getStatistics(productPage, categoryPage, daysPage);
        stadistics.getStadisticsProducts().forEach(sp->sp.setProduct(this.productClient.getProductDTO(sp.getProduct().getId())));

        // Crear un Map para almacenar los CategoryDTO por categoryId
        Map<Long, CategoryDTO> categoryMap = new HashMap<>();

        List<CategoryDTO> topCategories = stadistics.getStadisticsProducts()
                .stream()
                .map(p -> {
                    CategoryDTO categoryDTO = p.getProduct().getCategory();
                    categoryMap.put(categoryDTO.getId(), categoryDTO);
                    return categoryDTO.getId();
                })
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(entry -> categoryMap.get(entry.getKey()))
                .collect(Collectors.toList());


        stadistics.setStadisticsCategories(topCategories.stream()
                .map(StatisticsCategory::new)
                .collect(Collectors.toList()));


        return stadistics;
    }

    public PurchaseDTO deleteById(long id) {
        Purchase purchase = this.purchaseRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("purchase " + id + " not found"));

        return this.purchaseDtoMapper.toDto
                (this.purchaseRepository.delete(purchase));
    }

    public Page<PurchaseDTO> searchPurchases(Integer dni, String firstName, String lastName, Pageable pageable) {
        List<CustomerDTO> customerDTOS = this.userClient.searchCustomers(dni, firstName, lastName);
        List<PurchaseDTO> allPurchases = customerDTOS
                .stream()
                .parallel()
                .flatMap(c -> this.purchaseRepository.findAllByCustomerId(c.getId())
                        .stream()
                        .map(cc->this.purchaseDtoMapper.toDto(cc, c)))
                .collect(Collectors.toList());


        int start = Math.min((int) pageable.getOffset(), allPurchases.size());
        int end = Math.min((start + pageable.getPageSize()), allPurchases.size());
        return new PageImpl<>(allPurchases.subList(start, end), pageable, allPurchases.size());
    }

    public Object createOrderMp(OrderMpAddDTO body) {
        MercadoPagoConfig.setAccessToken(this.mercadoPagoAccesToken);

        List<PurchaseProductDTO> products = body.getProducts()
                .stream()
                .map(this.purchaseAddDtoMapper::purchaseProductAddDTOtoDomain)
                .map(this.purchaseDtoMapper::purchaseProductToPurchaseProductDTO)
                .collect(Collectors.toList());


        PreferenceClient client = new PreferenceClient();

        List<PreferenceItemRequest> items = products
                .stream()
                .map(p -> PreferenceItemRequest.builder()
                        .title(p.getProduct().getName())
                        .description(p.getProduct().getDescription())
                        .pictureUrl(p.getProduct().getImg())
                        .quantity(p.getQuantity())
                        .currencyId("ARS")
                        .unitPrice(p.getProduct().getPrice())
                        .build())
                .collect(Collectors.toList());


        PreferenceRequest request = PreferenceRequest.builder()
                .items(items)
                .externalReference(body.getIdPurchase().toString())
                .backUrls(PreferenceBackUrlsRequest.builder()
                        .success(this.urlFront)
                        .failure(this.urlFront)
                        .pending(this.urlFront)
                        .build())
                .notificationUrl(this.urlBack + "/api/v1/purchases/webhook")
                .build();
        try {
            Preference myPreference = client.create(request);
            return new HashMap<String, String>() {{
                put("urlMercadoPago", client.get(myPreference.getId()).getInitPoint());
            }};
        } catch (MPApiException ex) {
            System.out.printf(
                    "MercadoPago Error. Status: %s, Content: %s%n",
                    ex.getApiResponse().getStatusCode(), ex.getApiResponse().getContent());
            throw new RuntimeException("mp");
        } catch (MPException ex) {
            ex.printStackTrace();
            throw new RuntimeException("mp");
        }
    }

    public void handleWebhook(String type, Long dataId, Object body) {
        try {
            System.out.println("type " + type);
            System.out.println("dataId " + dataId);
            System.out.println(body);
            if (type.equals("payment")) {
                PaymentClient client = new PaymentClient();
                Payment payment = client.get(dataId);
                Long purchaseId = Long.parseLong(payment.getExternalReference().toString());
                if (payment.getStatus().equals("approved")){

                    Purchase purchase = this.purchaseRepository.findById(purchaseId)
                            .orElseThrow(() -> new NotFoundException("purchase " + purchaseId + " not found"));

                    purchase.setState("paid");

                    this.purchaseRepository.save(purchase);
                }
            }
        } catch (MPApiException ex) {
            System.out.printf(
                    "MercadoPago Error. Status: %s, Content: %s%n",
                    ex.getApiResponse().getStatusCode(), ex.getApiResponse().getContent());
            throw new RuntimeException("mp");
        } catch (MPException ex) {
            ex.printStackTrace();
            throw new RuntimeException("mp");
        }
    }


}
