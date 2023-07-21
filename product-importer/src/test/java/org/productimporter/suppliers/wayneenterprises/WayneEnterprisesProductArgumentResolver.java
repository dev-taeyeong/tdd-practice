package org.productimporter.suppliers.wayneenterprises;

import org.productimporter.DomainArgumentResolver;

import java.util.Optional;
import java.util.UUID;

public class WayneEnterprisesProductArgumentResolver implements DomainArgumentResolver {

    @Override
    public Optional<Object> tryResolve(Class<?> parameterType) {
        if (parameterType.equals(WayneEnterprisesProduct.class)) {
            return Optional.of(generate());
        } else if (parameterType.equals(WayneEnterprisesProduct[].class)) {
            return Optional.of(new WayneEnterprisesProduct[]{generate(), generate(), generate()});
        }

        return Optional.empty();
    }

    private static WayneEnterprisesProduct generate() {
        String id = "id" + UUID.randomUUID();
        String title = "title" + UUID.randomUUID();
        int listPrice = random.nextInt(100000) + 100000;
        int sellingPrice = listPrice - random.nextInt(10000) + 10000;

        return new WayneEnterprisesProduct(id, title, listPrice, sellingPrice);
    }
}
