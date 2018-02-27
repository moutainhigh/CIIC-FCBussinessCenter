package com.ciicsh.gto.fcbusinesscenter.tax.util.support.collector;

import java.math.BigDecimal;

@FunctionalInterface
public interface ToBigDecimalFunction<T> {
    BigDecimal applyAsBigDecimal(T value);
}