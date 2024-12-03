package com.github.victor.clientms;

import com.github.victor.clientms.web.dto.Product;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductTest {

    @Test
    void gettersAndSetters_test() {
        Product product = new Product();

        product.setId(1L);
        product.setName("Monitor");
        product.setQuantity(10);
        product.setHash("TW9uaXRvcg==");

        assertThat(product.getId()).isEqualTo(1L);
        assertThat(product.getName()).isEqualTo("Monitor");
        assertThat(product.getQuantity()).isEqualTo(10);
        assertThat(product.getHash()).isEqualTo("TW9uaXRvcg==");
    }

    @Test
    void allArgsConstructor_test() {
        Product product = new Product(1L, "Monitor", 10, "TW9uaXRvcg==");

        assertThat(product.getId()).isEqualTo(1L);
        assertThat(product.getName()).isEqualTo("Monitor");
        assertThat(product.getQuantity()).isEqualTo(10);
        assertThat(product.getHash()).isEqualTo("TW9uaXRvcg==");
    }

    @Test
    void testNoArgsConstructor_test() {
        Product product = new Product();

        assertThat(product).isNotNull();
        assertThat(product.getId()).isNull();
        assertThat(product.getName()).isNull();
        assertThat(product.getQuantity()).isNull();
        assertThat(product.getHash()).isNull();
    }

    @Test
    void toString_test() {
        Product product = new Product(1L, "Monitor", 10, "TW9uaXRvcg==");
        String toString = product.toString();

        assertThat(toString).isEqualTo("Product(id=1, name=Monitor, quantity=10, hash=TW9uaXRvcg==)");
    }

    @Test
    void notEquals_test() {
        Product product1 = new Product(1L, "Monitor", 10, "TW9uaXRvcg==");
        Product product2 = new Product(2L, "Controller", 5, "TW9auXRvgc==");

        assertThat(product1).isNotEqualTo(product2);
    }
}
