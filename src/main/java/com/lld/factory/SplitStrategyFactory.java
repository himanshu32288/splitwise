package com.lld.factory;

import com.lld.enums.SplitType;
import com.lld.service.SplitStrategy;
import com.lld.service.impl.EqualSplitStrategy;
import com.lld.service.impl.ExactSplit;
import com.lld.service.impl.PercentageSplit;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SplitStrategyFactory {
    public static SplitStrategy createSplitStrategy(SplitType splitType) {
        return switch (splitType) {
            case EQUAL -> new EqualSplitStrategy();
            case EXACT -> new ExactSplit();
            case PERCENTAGE -> new PercentageSplit();
        };
    }
}
