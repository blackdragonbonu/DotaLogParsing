package gg.bayes.challenge.service.validator.rules;

import gg.bayes.challenge.service.model.TokenizerResponse;
import gg.bayes.challenge.service.model.ValidatorResponse;
import gg.bayes.challenge.service.utils.DotaStringConstants;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HeroNamePresentRule implements ValidationRule<TokenizerResponse> {

    @Override
    public ValidatorResponse validate(TokenizerResponse tokenizerResponse) {
        if (tokenizerResponse == null || tokenizerResponse.equals(TokenizerResponse.NON_TOKENIZABLE_INPUT))
            return ValidatorResponse.INVALID_INPUT;
        boolean isValid = false;
        for (String component : tokenizerResponse.getLogComponents()) {
            if (component.contains(DotaStringConstants.HERO_NAME_PREFIX)) {
                isValid = true;
                break;
            }
        }
        return new ValidatorResponse(isValid);
    }
}
