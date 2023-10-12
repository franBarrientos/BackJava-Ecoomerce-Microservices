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
import com.ecommerce.Domain.Infrastructure.DB.Spring.Repositories.PurchaseProductDboRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final PurchaseProductDboRepository purchaseProductRepository;
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

        List<PurchaseDTO> purchaseDTOs = purchasesPage.get()
                .parallel()
                .map(p -> {
                    PurchaseDTO purchaseDTO = this.purchaseDtoMapper.toDto(p);
                    purchaseDTO.setCustomer(this.userClient.getCustomerDTO(p.getCustomerId()));
                    purchaseDTO.setPurchaseProducts(p.getPurchaseProducts()
                            .stream()
                            .map(pp -> PurchaseProductDTO.builder()
                                    .id(pp.getId())
                                    .purchase(this.purchaseDtoMapper.toDto(pp.getPurchase()))
                                    .quantity(pp.getQuantity())
                                    .totalPrice(pp.getTotalPrice())
                                    .product(this.productClient.getProductDTO(pp.getProductId()))
                                    .build())
                            .collect(Collectors.toList()));
                    return purchaseDTO;
                })
                .collect(Collectors.toList());

        return new PageImpl<>(purchaseDTOs, pageable, purchasesPage.getTotalElements());
    }



    public PurchaseDTO getById(Long id) {
        Purchase purchaseDomain = this.purchaseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("purchase " + id + " not found"));

        CompletableFuture<CustomerDTO> customerFuture = CompletableFuture.supplyAsync(() -> this.userClient.getCustomerDTO(purchaseDomain.getCustomerId()));

        CompletableFuture<List<PurchaseProductDTO>> purchaseProductsFuture = CompletableFuture.supplyAsync(() ->
                purchaseDomain.getPurchaseProducts()
                        .stream()
                        .map(pp -> PurchaseProductDTO.builder()
                                .id(pp.getId())
                                .purchase(this.purchaseDtoMapper.toDto(pp.getPurchase()))
                                .quantity(pp.getQuantity())
                                .totalPrice(pp.getTotalPrice())
                                .product(this.productClient.getProductDTO(pp.getProductId()))
                                .build())
                        .collect(Collectors.toList()));

        PurchaseDTO purchaseDTO = this.purchaseDtoMapper.toDto(purchaseDomain);


        CompletableFuture<Void> combinedFuture = customerFuture.thenCombine(purchaseProductsFuture, (customer, pps) -> {
            purchaseDTO.setCustomer(customer);
            purchaseDTO.setPurchaseProducts(pps);
            return null;
        });
        combinedFuture.join();
        return purchaseDTO;
    }





  /*  public Page<PurchaseDTO> search(Integer dni, String firstName, String lastName, Pageable pageable) {
        return this.purchaseRepository.search(dni, firstName, lastName, pageable)
                .map(this.purchaseDtoMapper::toDto);
    }*/

    @Transactional
    public PurchaseDTO createOne(PurchaseAddDTO purchase) {

        CustomerDTO customerDTO = this.userClient.getCustomerDTO(purchase.getCustomerId());
        if (customerDTO == null) {
            throw new NotFoundException("customer " + purchase.getCustomerId() + " not found");
        }

        Purchase purchaseToSave = this.purchaseAddDtoMapper
                .purchaseAddDTOtoDomain(purchase);

        Purchase purchaseSaved = this.purchaseRepository.save(purchaseToSave);

        purchaseSaved.getPurchaseProducts().forEach(pp -> pp.setPurchase(purchaseSaved));

        List<PurchaseProduct> purchaseProductList =
                this.purchaseProductRepository.saveAll(purchaseSaved.getPurchaseProducts());

        purchaseSaved.setPurchaseProducts(purchaseProductList);

        PurchaseDTO purchaseDTOToReturn = this.purchaseDtoMapper.toDto(this.purchaseRepository.save(purchaseSaved));
        purchaseDTOToReturn.setCustomer(customerDTO);
        return purchaseDTOToReturn;
    }

   /* public PurchaseDTO updateById(Long id, PurchaseAddDTO purchaseToUpdate) {

        Purchase purchaseUpdated = this.purchaseAddDtoMapper
                .purchaseAddDTOtoDomainWithPurchaseOptional(purchaseToUpdate);

        Purchase purchase = this.purchaseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("purchase " + id + " not found"));

        if (purchaseUpdated.getPayment() != null) {
            purchase.setPayment(purchaseUpdated.getPayment());
        }
        if (purchaseUpdated.getPurchaseProducts() != null) {
            purchase.setPurchaseProducts(purchaseUpdated.getPurchaseProducts());
        }
        if (purchaseUpdated.getState() != null) {
            purchase.setState(purchaseUpdated.getState());
        }

        return this.purchaseDtoMapper.toDto
                (this.purchaseRepository.save(purchase));
    }*/


 /*   public SalesStadistics getStadistics() {
        return SalesStadistics.builder()
                .stadisticsProducts(this.productRepository.get5mostSales())
                .stadisticsCategories(this.categoryRepository.get5mostSales())
                .stadisticsLast10days(this.purchaseRepository.getLast10DaysStadistics())
                .build();
    }
*/

    public PurchaseDTO deleteById(long id) {
        Purchase purchase = this.purchaseRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("purchase " + id + " not found"));

        return this.purchaseDtoMapper.toDto
                (this.purchaseRepository.delete(purchase));
    }

    /*public Object createOrderMp(OrderMpAddDTO body) {
        MercadoPagoConfig.setAccessToken(this.mercadoPagoAccesToken);

        List<PurchaseProduct> products = body.getProducts()
                .stream()
                .map(p -> {
                    Product product = this.productRepository
                            .findByIdAndHasStockIsTrue(p.getProductId())
                            .orElseThrow(() -> new NotFoundException("Product " +
                                    p.getProductId() + " not found"));

                    return PurchaseProduct.builder()
                            .product(product)
                            .quantity(p.getQuantity())
                            .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(p.getQuantity())))
                            .build();
                }).collect(Collectors.toList());


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
    }*/
}
