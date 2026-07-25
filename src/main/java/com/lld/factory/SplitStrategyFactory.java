package com.lld.factory;

import com.lld.enums.SplitType;
import com.lld.service.SplitStrategy;
import com.lld.service.impl.EqualSplitStrategy;

public class SplitStrategyFactory {
    public static SplitStrategy createSplitStrategy(SplitType splitType) {
        return switch (splitType) {
            case EQUAL -> new EqualSplitStrategy();
            case EXACT -> new EqualSplitStrategy();
            case PERCENTAGE -> new EqualSplitStrategy();
        };
    }
}
