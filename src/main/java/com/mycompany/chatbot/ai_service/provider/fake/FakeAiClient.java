package com.mycompany.chatbot.ai_service.provider.fake;

import com.mycompany.chatbot.ai_service.core.AiClient;
import com.mycompany.chatbot.ai_service.core.AiMessage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
public class FakeAiClient implements AiClient {

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

        if (text.contains("password")) {
            return "Try this: go to Settings → Security → Reset password. If you don’t see it, tell me what device you’re on.";
        }
        if (text.contains("email")) {
            return "You can change email in Profile → Account settings → Email. I can guide you step-by-step if you tell me where you’re stuck.";
        }
        if (text.contains("refund") || text.contains("return")) {
            return "Refunds usually take 3–5 business days after approval. Tell me your order date and I’ll suggest the next best step.";
        }
        if (text.contains("doesn't work") || text.contains("didn't work") || text.contains("not working")) {
            return lastAssistant != null
                    ? "Okay. About my last suggestion:\n" + lastAssistant + "\n\nWhat exactly happens when you try it?"
                    : "Okay. What exactly happens when you try it?";
        }

        return "Got you. Tell me a bit more context (what happened + what result you want), and I’ll propose the best next step.";
    }
}