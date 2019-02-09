/*
 * Copyright (c) 2019.
 * shurik
 */

package com.omegaauto.shurik.mobilesklad;

public interface ObservablesContainerPropertiesSettings {
    void setOnSettingsContainerRedoListener(OnSettingsContainerRedoListener o);
    void removeObserver(OnSettingsContainerRedoListener o);
    void notifyObservers();
}
