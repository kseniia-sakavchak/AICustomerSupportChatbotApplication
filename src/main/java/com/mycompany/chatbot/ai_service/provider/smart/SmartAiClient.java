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
        String text = normalize(question);

        System.out.println("AI INPUT: " + text);
        String lastAssistant = history == null ? null :
                history.stream()
                        .filter(m -> "assistant".equalsIgnoreCase(m.role()))
                        .reduce((first, second) -> second)
                        .map(AiMessage::content)
                        .orElse(null);

        String currentText = text;

        String lastUserMessage = history == null ? null :
                history.stream()
                        .filter(m -> "user".equalsIgnoreCase(m.role()))
                        .map(AiMessage::content)
                        .filter(content -> !normalize(content).equals(currentText))
                        .reduce((first, second) -> second)
                        .orElse(null);

        if (isUnclearMessage(text) && lastUserMessage != null) {
            return SmartAiResponses.clarifyShortReply();
        }

        if (isFailure(text)) {
            String previousContext = lastUserMessage != null ? lastUserMessage : lastAssistant;
            Intent prevIntent = detectIntent(previousContext == null ? "" : previousContext);

            String followUp = switch (prevIntent) {
                case EMAIL_CHANGE -> SmartAiResponses.failureEmailChange();
                case BILLING_PAYMENT_FAILED -> SmartAiResponses.failureBillingPaymentFailed();
                case LOGIN_2FA -> SmartAiResponses.failureLogin2fa();
                case SUBSCRIPTION_CANCEL -> SmartAiResponses.failureSubscriptionCancel();
                case PASSWORD_RESET -> SmartAiResponses.failurePasswordReset();
                case PLAN_UPGRADE_DOWNGRADE -> SmartAiResponses.failurePlanUpgradeDowngrade();
                case REFUND -> SmartAiResponses.failureRefund();
                case INVOICE -> SmartAiResponses.failureInvoice();
                case OTHER -> SmartAiResponses.failureFallback();
                case GREETING -> "Hi! I can help you with login, billing, subscriptions, or account issues. What do you need?";
            };

            return "Okay. " + followUp;
        }

        Intent intent = detectIntent(text);

        return switch (intent) {
            case PASSWORD_RESET -> SmartAiResponses.passwordReset();
            case LOGIN_2FA -> SmartAiResponses.login2fa();
            case EMAIL_CHANGE -> SmartAiResponses.emailChange();
            case BILLING_PAYMENT_FAILED -> SmartAiResponses.billingPaymentFailed();
            case SUBSCRIPTION_CANCEL -> SmartAiResponses.subscriptionCancel();
            case PLAN_UPGRADE_DOWNGRADE -> SmartAiResponses.planUpgradeDowngrade();
            case REFUND -> SmartAiResponses.refund();
            case INVOICE -> SmartAiResponses.invoice();
            case OTHER -> SmartAiResponses.generic();
            case GREETING -> "Hi! I can help you with login, billing, subscriptions, or account issues. What do you need?";
        };
    }

    private Intent detectIntent(String text) {
        String t = normalize(text);

        Intent best = Intent.OTHER;
        int bestScore = 0;

        int s;

        s = score(t, Intent.PASSWORD_RESET,
                "forgot password", "reset password", "password reset",
                "can't login", "can’t login", "password");
        if (s > bestScore) { bestScore = s; best = Intent.PASSWORD_RESET; }

        s = score(t, Intent.LOGIN_2FA,
                "2fa", "two factor", "verification code", "verification",
                "authenticator", "sms code", "code not working");
        if (s > bestScore) { bestScore = s; best = Intent.LOGIN_2FA; }

        s = score(t, Intent.EMAIL_CHANGE,
                "change email", "change my email", "update email", "email address", "new email");
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

        s = score(t, Intent.GREETING,
                "hi", "hello", "hey", "help");
        if (s > bestScore) { bestScore = s; best = Intent.GREETING; }

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
        System.out.println("failure detected");
        String t = normalize(text);

        return t.contains("doesn't work")
                || t.contains("still not working")
                || t.contains("doesnt work")
                || t.contains("does not work")
                || t.contains("didn't work")
                || t.contains("didnt work")
                || t.contains("not working")
                || t.contains("still not working")
                || t.contains("did not help");
    }

    private boolean isUnclearMessage(String text) {
        return text.equals("it")
                || text.equals("this")
                || text.equals("that");
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
        GREETING,
        OTHER
    }
}