package com.mycompany.chatbot.ai_service.provider.smart;

import com.mycompany.chatbot.ai_service.core.AiClient;
import com.mycompany.chatbot.ai_service.core.AiMessage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component("smart")
public class SmartAiClient implements AiClient {

    @Override
    public String generateAnswer(String question, List<AiMessage> history) {
        String text = question == null ? "" : question.trim().toLowerCase(Locale.ROOT);
        text = text.replace("’", "'").replace("`", "'");

        String lastAssistant = history == null ? null :
                history.stream()
                        .filter(m -> "assistant".equalsIgnoreCase(m.role()))
                        .reduce((first, second) -> second)
                        .map(AiMessage::content)
                        .orElse(null);

        if (isFailure(text)) {
            Intent prevIntent = detectIntent(lastAssistant == null ? "" : lastAssistant);

            String followUp = switch (prevIntent) {
                case EMAIL_CHANGE ->
                        "Do you see a verification step? If yes — do you receive the email/code (check spam too)?";
                case BILLING_PAYMENT_FAILED ->
                        "What exact error do you see? And what payment method are you using (card/PayPal)?";
                case LOGIN_2FA ->
                        "Is it SMS 2FA or an authenticator app? Do you receive any code at all?";
                case SUBSCRIPTION_CANCEL ->
                        "Do you want to cancel renewal only, or close the account too?";
                case PASSWORD_RESET ->
                        "Do you receive the reset email? If not — what email address are you using and did you check spam?";
                case PLAN_UPGRADE_DOWNGRADE ->
                        "Which plan are you switching from/to? Do you see any error on the billing page?";
                case REFUND ->
                        "What’s your order ID and purchase date? Also, was it a monthly or annual plan?";
                case INVOICE ->
                        "Do you need a standard receipt or a VAT/company invoice? What’s your billing country?";
                case OTHER ->
                        "What exactly happens when you try it (error text / which step fails)?";
            };

            return lastAssistant != null
                    ? "Okay. About my last suggestion:\n" + lastAssistant + "\n\n" + followUp
                    : "Okay. " + followUp;
        }

        Intent intent = detectIntent(text);

        return switch (intent) {
            case PASSWORD_RESET -> "Password reset: Settings → Security → Reset password. If you don’t get the email, check spam and confirm your address.";
            case LOGIN_2FA -> "2FA issue: make sure your device time is set to automatic. If you're using an authenticator app, try generating a new code. What type of 2FA do you use (SMS/app)?";
            case EMAIL_CHANGE -> "Email change: Profile → Account settings → Email. If verification is required, confirm the old email first. What happens when you try?";
            case BILLING_PAYMENT_FAILED -> "Payment failed: check card balance + online payments enabled. Try another card or PayPal if available. What error message do you see?";
            case SUBSCRIPTION_CANCEL -> "To cancel: Billing → Subscription → Cancel. You’ll keep access until the end of the billing period. Do you want to cancel renewal or delete the account too?";
            case PLAN_UPGRADE_DOWNGRADE -> "Plan change: Billing → Plans → Choose plan. Upgrades apply immediately; downgrades usually apply next cycle. Which plan are you switching to?";
            case REFUND -> "Refunds: share order ID + purchase date + payment method. I’ll tell you the fastest option based on policy.";
            case INVOICE -> "Invoices: Billing → Invoices → Download. If you need VAT/company details on the invoice, tell me your billing country.";
            case OTHER -> "Got it. Tell me what you want to achieve + what you tried + any error text, and I’ll guide you step-by-step.";
        };
    }

    private Intent detectIntent(String text) {
        String t = normalize(text);

        Intent best = Intent.OTHER;
        int bestScore = 0;

        int s;

        s = score(t, Intent.PASSWORD_RESET,
                "forgot password", "reset password", "password reset",
                "can't login", "can’t login", "login", "sign in", "password");
        if (s > bestScore) { bestScore = s; best = Intent.PASSWORD_RESET; }

        s = score(t, Intent.LOGIN_2FA,
                "2fa", "two factor", "verification code", "authenticator", "sms code", "code not working");
        if (s > bestScore) { bestScore = s; best = Intent.LOGIN_2FA; }

        s = score(t, Intent.EMAIL_CHANGE,
                "change email", "update email", "email address", "new email");
        if (s > bestScore) { bestScore = s; best = Intent.EMAIL_CHANGE; }

        s = score(t, Intent.BILLING_PAYMENT_FAILED,
                "payment failed", "card declined", "failed payment", "billing", "charge", "charged", "card", "payment");
        if (s > bestScore) { bestScore = s; best = Intent.BILLING_PAYMENT_FAILED; }

        s = score(t, Intent.SUBSCRIPTION_CANCEL,
                "cancel subscription", "cancel", "stop renewal", "unsubscribe", "turn off renewal", "renewal");
        if (s > bestScore) { bestScore = s; best = Intent.SUBSCRIPTION_CANCEL; }

        s = score(t, Intent.PLAN_UPGRADE_DOWNGRADE,
                "upgrade", "downgrade", "change plan", "pricing plan", "plan");
        if (s > bestScore) { bestScore = s; best = Intent.PLAN_UPGRADE_DOWNGRADE; }

        s = score(t, Intent.REFUND,
                "refund", "money back", "chargeback", "return");
        if (s > bestScore) { bestScore = s; best = Intent.REFUND; }

        s = score(t, Intent.INVOICE,
                "invoice", "receipt", "vat", "tax", "billing receipt");
        if (s > bestScore) { bestScore = s; best = Intent.INVOICE; }

        return best;
    }

    private int score(String text, Intent intent, String... phrases) {
        int score = 0;
        for (String p : phrases) {
            if (text.contains(p)) score += 2;
        }

        if (intent == Intent.PASSWORD_RESET && (text.contains("forgot") || text.contains("reset"))) score += 2;
        if (intent == Intent.SUBSCRIPTION_CANCEL && text.contains("cancel")) score += 2;

        return score;
    }

    private String normalize(String s) {
        String t = s == null ? "" : s.trim().toLowerCase(Locale.ROOT);
        return t.replace("’", "'").replace("`", "'").replace("can’t", "can't");
    }

    private boolean isFailure(String text) {
        String t = normalize(text);

        return t.contains("doesn't work")
                || t.contains("doesnt work")
                || t.contains("does not work")
                || t.contains("didn't work")
                || t.contains("didnt work")
                || t.contains("not working")
                || t.contains("still not working")
                || t.contains("error")
                || t.contains("fails");
    }

    private enum Intent {
        PASSWORD_RESET,
        LOGIN_2FA,
        EMAIL_CHANGE,
        BILLING_PAYMENT_FAILED,
        SUBSCRIPTION_CANCEL,
        PLAN_UPGRADE_DOWNGRADE,
        REFUND,
        INVOICE,
        OTHER
    }
}