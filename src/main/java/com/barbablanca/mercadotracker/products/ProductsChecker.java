package com.barbablanca.mercadotracker.products;

import com.barbablanca.mercadotracker.mailing.MailSender;
import com.barbablanca.mercadotracker.prices.PriceEntity;
import com.barbablanca.mercadotracker.users.UserEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
public class ProductsChecker {
    private final ProductRepository productRepository;
    private final RestTemplate restTemplate;
    private final MailSender mailSender;

    ProductsChecker(ProductRepository productRepository, RestTemplate restTemplate, MailSender mailSender) {
        this.productRepository = productRepository;
        this.restTemplate = restTemplate;
        this.mailSender = mailSender;
    }

    private ProductEntity findProductById(List<ProductEntity> products, String id) {
        for ( ProductEntity product : products) {
            if (product.getId().equals(id))
                return product;
        } return null;
    }

    private List<MLProductResponse> mockRequest(List<ProductEntity> products, Integer variation) {
        List<MLProductResponse> response = new ArrayList<>();
        int iter = 0;
        for (ProductEntity product : products) {
            if (variation != 0 && iter++ != 0)
                variation = 0;
            response.add(new MLProductResponse(new MLProduct(product.getId(), product.getName(), product.getImgUrl(), product.getCurPrice().getAmount() + variation, product.getCurPrice().getCurrency(), product.getUrl()), 200));
        }

        return response;
    }


    @Scheduled(fixedDelay = 36000000, initialDelay = 5000)
    public void check() throws IOException {

        List<ProductEntity> products = (List<ProductEntity>) productRepository.findAll();

        int iterations =  (new Double(Math.ceil(products.size() / 20f))).intValue();

        for ( int i = 0; i < iterations; i++) {

            int rightLimit = 20 * (i + 1);

            if (rightLimit > products.size()) {
                rightLimit = products.size() - (i * 20);
            }

            List<ProductEntity> subProducts = products.subList(20 * i, rightLimit);

            List<String> idsList = new ArrayList<>();

            for (ProductEntity product : subProducts) { idsList.add(product.getId()); }

            String ids = idsList.toString();
            ids = ids.substring(1, ids.length() - 1);
            ids = ids.replace(" ", "");

            MLProductResponse[] response = Objects.requireNonNull(
                    restTemplate.getForObject(
                            "https://api.mercadolibre.com/items?ids="+ ids +
                            "&attributes=id,title,thumbnail,price,currency_id", MLProductResponse[].class)
            );

            for (MLProductResponse responseProduct : response) {

                if (!responseProduct.getCode().equals(404)) {

                    MLProduct mlProduct = responseProduct.getBody();

                    ProductEntity localProduct = findProductById(products, mlProduct.getId());

                    if (localProduct == null) continue;

                    System.out.println(
                            "Comparing loacal product "+
                            localProduct.getId()                        +": $ " +
                            localProduct.getCurPrice().getAmount()      +" "    +
                            localProduct.getCurPrice().getCurrency()    +
                            " against ML product "+ mlProduct.getId()   +": $ " +
                            mlProduct.getPrice()                        +" "    +
                            mlProduct.getCurrency());

                    if (!mlProduct.getPrice().equals(localProduct.getCurPrice().getAmount())) {

                        localProduct.setPrevPrice(localProduct.getCurPrice());
                        localProduct.setCurPrice(new PriceEntity(
                                mlProduct.getPrice(),
                                mlProduct.getCurrency(),
                                new Date()));

                        localProduct = productRepository.save(localProduct);

                        for (UserEntity user : localProduct.getSubscribers()) {
                            mailSender.sendNotification(user, localProduct);
                        }
                    }
                }
            }
        }

    }
}
