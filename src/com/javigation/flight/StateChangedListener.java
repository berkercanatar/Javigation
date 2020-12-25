package com.javigation.flight;

public interface StateChangedListener {
    void OnStateChanged(StateMachine.StateTypes changedType, boolean isAdded);
    void OnTakeOffCompleted();
    void OnLandingCompleted();
}
