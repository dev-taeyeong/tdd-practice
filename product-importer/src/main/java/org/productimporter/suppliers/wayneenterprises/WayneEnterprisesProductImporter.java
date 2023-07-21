package org.productimporter.suppliers.wayneenterprises;

import org.productimporter.Product;
import org.productimporter.ProductImporter;

import java.util.ArrayList;

public class WayneEnterprisesProductImporter implements ProductImporter {

    public WayneEnterprisesProductImporter(WayneEnterprisesProductSource datasource) {

    }

    @Override
    public Iterable<Product> fetchProducts() {
        return new ArrayList<>();
    }
}
