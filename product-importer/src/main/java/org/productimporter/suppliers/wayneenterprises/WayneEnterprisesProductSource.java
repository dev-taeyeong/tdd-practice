package org.productimporter.suppliers.wayneenterprises;

public interface WayneEnterprisesProductSource {

    Iterable<WayneEnterprisesProduct> fetchProducts();
}
