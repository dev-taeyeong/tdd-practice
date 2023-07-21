package org.productimporter.suppliers.wayneenterprises;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.productimporter.DomainArgumentsSource;
import org.productimporter.Product;

class WayneEnterprisesProductImporterTest {

    @ParameterizedTest
    @DomainArgumentsSource
    void sut_projects_all_products(WayneEnterprisesProduct[] source) {
        var stub = new WayneEnterprisesProductSourceStub(source);
        var sut = new WayneEnterprisesProductImporter(stub);

        Iterable<Product> actual = sut.fetchProducts();

        Assertions.assertThat(actual).hasSize(source.length);
    }
}