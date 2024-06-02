package Strategy;

public class Payment {

    private PaymentMethod method;

    public Payment(PaymentMethod method) {
        this.method = method;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }

    public void executePayment() {
        method.execute();
    }
}
