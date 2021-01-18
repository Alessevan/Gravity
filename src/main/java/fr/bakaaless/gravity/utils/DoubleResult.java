package fr.bakaaless.gravity.utils;

public class DoubleResult<T, K> {

    private T firstValue;
    private K secondValue;

    public DoubleResult() {
    }

    public T getFirstValue() {
        return this.firstValue;
    }

    public void setFirstValue(final T firstValue) {
        this.firstValue = firstValue;
    }

    public K getSecondValue() {
        return this.secondValue;
    }

    public void setSecondValue(final K secondValue) {
        this.secondValue = secondValue;
    }

    public static <T, K> DoubleResult<T, K> from(final T firstValue, final K secondValue) {
        final DoubleResult<T, K> result = new DoubleResult<>();
        result.setFirstValue(firstValue);
        result.setSecondValue(secondValue);
        return result;
    }
}
