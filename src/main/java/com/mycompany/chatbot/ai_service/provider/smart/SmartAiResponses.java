package com.mycompany.chatbot.ai_service.provider.smart;

public class SmartAiResponses {

    private SmartAiResponses() {
    }

    public static String passwordReset() {
        return "Sure — here’s the fastest way to reset your password:\n" +
                "Settings → Security → Reset password.\n" +
                "If you don’t get the reset email, check spam and make sure you entered the correct address.";
    }

    public static String login2fa() {
        return "Got it — let’s check the 2FA step.\n" +
                "Make sure your device time is set to automatic. If you use an authenticator app, try generating a fresh code.\n" +
                "Are you using SMS or an authenticator app?";
    }

    public static String emailChange() {
        return "No problem — you can update your email in:\n" +
                "Profile → Account settings → Email.\n" +
                "If verification is required, confirm the old email first.\n" +
                "What happens when you try?";
    }

    public static String billingPaymentFailed() {
        return "Looks like this may be a billing issue.\n" +
                "Please check whether online payments are enabled and whether the card has enough balance.\n" +
                "If available, try another payment method too.\n" +
                "What exact error message do you see?";
    }

    public static String subscriptionCancel() {
        return "Sure — you can cancel here:\n" +
                "Billing → Subscription → Cancel.\n" +
                "You’ll usually keep access until the end of the current billing period.\n" +
                "Do you want to cancel renewal only, or close the account too?";
    }

    public static String planUpgradeDowngrade() {
        return "You can change your plan in:\n" +
                "Billing → Plans → Choose plan.\n" +
                "Upgrades usually apply immediately, while downgrades often start next billing cycle.\n" +
                "Which plan are you switching from and to?";
    }

    public static String refund() {
        return "I can help with that.\n" +
                "Please share your order ID, purchase date, and payment method.\n" +
                "Once I have that, I can suggest the fastest refund path.";
    }

    public static String invoice() {
        return "You can usually download invoices in:\n" +
                "Billing → Invoices.\n" +
                "If you need a VAT or company invoice, tell me your billing country and I’ll guide you.";
    }

    public static String generic() {
        return "I can help with that.\n" +
                "Please tell me:\n" +
                "1) what you want to do\n" +
                "2) what you already tried\n" +
                "3) any error text you see\n" +
                "and I’ll guide you step by step.";
    }

    public static String failureFallback() {
        return "Can you describe exactly what happens when you try, including any error text or the step where it fails?";
    }

    public static String failurePasswordReset() {
        return "Do you receive the reset email? If not, what email address are you using, and did you check spam?";
    }

    public static String failureLogin2fa() {
        return "Are you using SMS 2FA or an authenticator app? Also, do you receive any code at all?";
    }

    public static String failureEmailChange() {
        return "Can you tell me whether you see a verification step? If yes, do you actually receive the email or code?";
    }

    public static String failureBillingPaymentFailed() {
        return "Can you share the exact error message and the payment method you’re using, for example card or PayPal?";
    }

    public static String failureSubscriptionCancel() {
        return "Do you want to stop renewal only, or do you want to fully close the account too?";
    }

    public static String failurePlanUpgradeDowngrade() {
        return "Which plan are you trying to switch from and to? Do you see any issue on the billing page?";
    }

    public static String failureRefund() {
        return "Can you share your order ID and purchase date? Also, was it a monthly or annual plan?";
    }

    public static String failureInvoice() {
        return "Do you need a regular receipt or a VAT/company invoice? What’s your billing country?";
    }
}
