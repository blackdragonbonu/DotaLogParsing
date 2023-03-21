package gg.bayes.challenge.service.model;

import com.google.common.collect.ImmutableList;
import lombok.Getter;

@Getter
public class TokenizerResponse {
    private final ImmutableList<String> logComponents;
    private final boolean tokenizable;

    public static final TokenizerResponse NON_TOKENIZABLE_INPUT = new TokenizerResponse(null, false);

    public TokenizerResponse(String[] logComponents, boolean tokenizable) {
        this.logComponents = logComponents == null ? null : ImmutableList.copyOf(logComponents);
        this.tokenizable = tokenizable;
    }
}
