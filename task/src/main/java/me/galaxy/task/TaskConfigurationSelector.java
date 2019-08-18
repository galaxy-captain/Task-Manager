package me.galaxy.task;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;

public class TaskConfigurationSelector extends AdviceModeImportSelector<EnableTask> {

    private static final String DEFAULT_ADVICE_MODE_ATTRIBUTE_NAME = "mode";

    @Override
    protected String getAdviceModeAttributeName() {
        return DEFAULT_ADVICE_MODE_ATTRIBUTE_NAME;
    }

    @Override
    protected String[] selectImports(AdviceMode adviceMode) {

        switch (adviceMode) {
            case PROXY:
                return new String[]{ProxyTaskConfiguration.class.getName(), AbstractTaskConfiguration.class.getName()};
            default:
                return null;
        }

    }

}
